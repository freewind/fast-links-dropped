package in.freewind.fastlinks.libs

import java.io.IOException

import in.freewind.fastlinks._
import in.freewind.fastlinks.wrappers.chrome._
import org.scalajs.dom
import org.scalajs.dom._

import scala.concurrent.{Future, Promise}
import scala.scalajs.js

object Chrome {

  val fileSystem = new {
    def chooseDirectory(): Future[(Entry, String)] = {
      val p = Promise[(Entry, String)]()
      chrome.fileSystem.chooseEntry(js.Dynamic.literal("type" -> "openDirectory"), (dir: Entry) => {
        chrome.fileSystem.getDisplayPath(dir.asInstanceOf[DirectoryEntry], (path: String) => {
          p.success((dir, path)).unit()
        })
      })
      p.future
    }

    def isRestorable(id: String): Future[Boolean] = {
      val p = Promise[Boolean]()
      chrome.fileSystem.isRestorable(id, (isRestorable: Boolean) => {
        p.success(isRestorable).unit()
      })
      p.future
    }

    def restoreDir(id: String): Future[DirectoryEntry] = {
      val p = Promise[DirectoryEntry]()
      chrome.fileSystem.restoreEntry(id, (entry: Entry) => {
        p.success(entry.asInstanceOf[DirectoryEntry]).unit()
      })
      p.future
    }

    def retainDir(entry: DirectoryEntry): String = {
      chrome.fileSystem.retainEntry(entry)
    }

    def readFile(dir: DirectoryEntry, fileName: String): Future[String] = {
      val p = Promise[String]()
      dir.getFile(fileName, js.Dynamic.literal(), (entry: FileEntry) => {
        if (entry != null) {
          entry.file((file: FileEntry) => {
            val reader = new FileReader()
            reader.onloadend = (event: ProgressEvent) => {
              p.success(reader.result.toString).unit()
            }
            reader.onerror = (event: Event) => {
              p.failure(new IOException("read file error")).unit()
            }
            reader.readAsText(file).unit()
          })
        }.unit()
      })
      p.future
    }

    // FIXME javascript file api is so hard !!!
    def overrideFile(dir: DirectoryEntry, fileName: String, content: String): Future[Unit] = {
      val p = Promise[Unit]()
      chrome.fileSystem.getWritableEntry(dir, (entry: Entry) => {
        entry.getFile(fileName, js.Dynamic.literal("create" -> "true"),
          (file: FileEntry) => {
            file.createWriter((writer: FileWriter) => {
              val blob = new Blob(js.Array(content))
              writer.onwriteend = (event: ProgressEvent) => {
                writer.onwriteend = null
                writer.truncate(content.length)
                p.success(()).unit()
              }
              writer.onerror = (event: Event) => {
                p.failure(new IOException("write file error")).unit()
              }
              writer.write(blob, js.Dynamic.literal("type" -> "text/javascript"))
            })
          },
          (error: FileError) => dom.console.dir(error)
        )
      })
      p.future
    }
  }

  val storage = new {
    def load[T](key: String, fromJson: String => T): Future[Option[T]] = {
      val p = Promise[Option[T]]()
      chrome.storage.local.get(js.Array(key), (items: js.Dictionary[String]) => {
        p.success(items.get(key).map(fromJson)).unit()
      })
      p.future
    }

    def save[T](key: String, data: T, toJson: T => String): Future[T] = {
      val p = Promise[T]()
      chrome.storage.local.set(js.Dynamic.literal(key -> toJson(data)), () => {
        p.success(data).unit()
      })
      p.future
    }
  }
}
