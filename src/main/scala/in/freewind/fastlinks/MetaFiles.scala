package in.freewind.fastlinks

import in.freewind.fastlinks.libs.Chrome
import in.freewind.fastlinks.wrappers.JsBeautifier
import in.freewind.fastlinks.wrappers.chrome.DirectoryEntry
import upickle._

import scala.concurrent.{Promise, Future}
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

object MetaFiles {

  def readMeta(dir: DirectoryEntry): Future[Meta] = {
    val p = Promise[Meta]()
    Chrome.fileSystem.readFile(dir, "_meta_.json").map(read[_Meta_]).foreach { _meta_ =>
      val categoryFutures = _meta_.categories.map(category =>
        Chrome.fileSystem.readFile(dir, category + ".json").map(read[Category])
      )
      Future.sequence(categoryFutures).foreach { categories =>
        p.success(new Meta(categories))
      }
    }
    p.future
  }

  def saveMeta(dir: DirectoryEntry, meta: Meta): Future[Unit] = {
    val p = Promise[Unit]()
    val metaContent = write(_Meta_(meta.categories.map(c => c.name)))
    val files = ("_meta_.json" -> metaContent) +: meta.categories.map(c => (c.name + ".json") -> write(c))
    val futures = files.map { case (fileName, content) =>
      Chrome.fileSystem.overrideFile(dir, fileName, JsBeautifier.js_beautify.apply(content))
    }
    Future.sequence(futures).foreach(_ => p.success(()))
    p.future
  }

}

case class _Meta_(categories: Seq[String])
