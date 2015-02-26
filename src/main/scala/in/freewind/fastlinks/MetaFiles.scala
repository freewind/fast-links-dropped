package in.freewind.fastlinks

import in.freewind.fastlinks.libs.Chrome
import in.freewind.fastlinks.wrappers.JsBeautifier
import in.freewind.fastlinks.wrappers.chrome.{FileEntry, Entry, FileError, DirectoryEntry}
import upickle._

import scala.concurrent.{Promise, Future}
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import scala.scalajs.js

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
    saveMetaFiles(dir, meta).flatMap(_ => deleteUnusedFiles(dir, meta))
  }

  private def saveMetaFiles(dir: DirectoryEntry, meta: Meta): Future[Unit] = {
    val p = Promise[Unit]()
    val metaContent = write(_Meta_(meta.categories.map(c => c.name)))
    val files = ("_meta_.json" -> metaContent) +: meta.categories.map(c => (c.name + ".json") -> write(c))
    val futures = files.map { case (fileName, content) =>
      Chrome.fileSystem.overrideFile(dir, fileName, JsBeautifier.js_beautify.apply(content))
    }
    Future.sequence(futures).foreach(_ => p.success(()))
    p.future
  }

  private def deleteUnusedFiles(dir: DirectoryEntry, meta: Meta): Future[Unit] = {
    val p = Promise[Unit]()
    val categoryFileNames = meta.categories.map(_.name + ".json")
    var totalFiles = Seq[FileEntry]()
    val reader = dir.createReader()
    def readFiles(): Unit = {
      reader.readEntries((files: js.Array[Entry]) => {
        if (files.isEmpty) {
          val unused = totalFiles.filterNot(f => f.name == "_meta_.json" || categoryFileNames.contains(f.name))
          Future.sequence(unused.map(Chrome.fileSystem.removeFile)).foreach(_ => p.success())
        } else {
          totalFiles = totalFiles.toList ::: files.map(_.asInstanceOf[FileEntry]).toList
          readFiles()
        }
      }, (error: FileError) => println(error))
    }
    readFiles()
    p.future
  }

}

case class _Meta_(categories: Seq[String])
