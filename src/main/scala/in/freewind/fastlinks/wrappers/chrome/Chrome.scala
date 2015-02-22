package in.freewind.fastlinks.wrappers.chrome

import org.scalajs.dom.Blob

import scalajs.js

trait Chrome extends js.Object {
  val storage: ChromeStorage = js.native
  val fileSystem: ChromeFileSystem = js.native
}

trait ChromeFileSystem extends js.Object {
  def chooseEntry(options: js.Any, callback: js.Function1[Entry, Unit] = ???): Unit = js.native
  def getDisplayPath(entry: Entry, callback: js.Function1[String, Unit] = ???): Unit = js.native
  def retainEntry(entry: Entry): String = js.native
  def isRestorable(id: String, callback: js.Function1[Boolean, Unit] = ???): Unit = js.native
  def restoreEntry(id: String, callback: js.Function1[Entry, Unit] = ???): Unit = js.native
  def getWritableEntry(entry: Entry, callback: js.Function1[Entry, Unit] = ???): Unit = js.native
  def isWritableEntry(entry: Entry, callback: js.Function1[Boolean, Unit] = ???): Unit = js.native
}

trait Entry extends js.Object {
  val fullPath: String = js.native
  val name: String = js.native
  val isDirectory: Boolean = js.native
  val isFile: Boolean = js.native
  def getFile(fileName: String, options: js.Any, callback: js.Function1[FileEntry, Unit] = ???): Unit = js.native
  def createWriter(callback: js.Function1[FileWriter, Unit] = ???): Unit = js.native
}

trait FileEntry extends Entry {
  def file(callback: js.Function1[FileEntry, Unit] = ???): Unit = js.native
}

trait DirectoryEntry extends Entry {

}

trait FileWriter extends js.Object {
  def write(blob: Blob, options: js.Any): Unit = js.native
}

trait FileEvent extends js.Object {
  val target: FileEventTarget = js.native
}

trait FileEventTarget extends js.Object {
  val result: String = js.native
}

trait ChromeStorage extends js.Object {
  val local: StorageLocal = js.native
}

trait StorageLocal extends js.Object {
  def set(map: js.Any, callback: js.Function0[Unit] = ???): Unit = js.native
  def get(key: js.Array[String], callback: js.Function1[js.Dictionary[String], Unit] = ???): Unit = js.native
}
