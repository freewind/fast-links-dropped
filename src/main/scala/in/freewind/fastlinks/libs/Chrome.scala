package in.freewind.fastlinks.libs

import in.freewind.fastlinks.wrappers.chrome._

import scala.concurrent.{Promise, Future}
import scala.scalajs.js

object Chrome {

  val fileSystem = new {
    def chooseDirectory(): Future[(Entry, String)] = {
      val p = Promise[(Entry, String)]()
      chrome.fileSystem.chooseEntry(js.Dynamic.literal("type" -> "openDirectory"), (dir: Entry) => {
        chrome.fileSystem.getDisplayPath(dir.asInstanceOf[DirectoryEntry], (path: String) => {
          p.success((dir, path))
          ()
        })
      })
      p.future
    }

    def isRestorable(id: String): Future[Boolean] = {
      val p = Promise[Boolean]()
      chrome.fileSystem.isRestorable(id, (isRestorable: Boolean) => {
        p.success(isRestorable)
        ()
      })
      p.future
    }

    def restore(id: String): Future[Entry] = {
      val p = Promise[Entry]()
      chrome.fileSystem.restoreEntry(id, (entry: Entry) => {
        p.success(entry)
        ()
      })
      p.future
    }

    def retain(entry: Entry): String = {
      chrome.fileSystem.retainEntry(entry)
    }
  }

  val storage = new {
    def load[T](key: String, fromJson: String => T): Future[Option[T]] = {
      val p = Promise[Option[T]]()
      chrome.storage.local.get(js.Array(key), (items: js.Dictionary[String]) => {
        p.success(items.get(key).map(fromJson))
        ()
      })
      p.future
    }

    def save[T](key: String, data: T, toJson: T => String): Future[T] = {
      val p = Promise[T]()
      chrome.storage.local.set(js.Dynamic.literal(key -> toJson(data)), () => {
        p.success(data)
        ()
      })
      p.future
    }
  }
}
