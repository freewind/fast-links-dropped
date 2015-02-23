package in.freewind.fastlinks

import in.freewind.fastlinks.libs.Chrome
import in.freewind.fastlinks.wrappers.chrome.DirectoryEntry
import upickle._

import scala.concurrent.{Promise, Future}
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

object MetaFiles {

  def readMeta(dir: DirectoryEntry): Future[Meta] = {
    val p = Promise[Meta]()
    Chrome.fileSystem.readFile(dir, "_meta_.json").map(read[_Meta_]).foreach { _meta_ =>
      val categoryFutures = _meta_.categories.map(category =>
        Chrome.fileSystem.readFile(dir, category.filename).map(read[Seq[Project]])
          .map(ps => new Category(category.name, ps, category.description))
      )
      Future.sequence(categoryFutures).foreach { categories =>
        p.success(new Meta(categories))
      }
    }
    p.future
  }

}

case class _Meta_(categories: Seq[_Category_])

case class _Category_(name: String, filename: String, description: Option[String])
