package in.freewind.fastlinks.wrappers.chrome

import scalajs.js

trait Chrome extends js.Object {
  val storage: ChromeStorage = js.native
  val fileSystem: ChromeFileSystem = js.native
}

trait ChromeFileSystem extends js.Object {
  def chooseEntry(options: js.Any, callback: js.Function2[Entry, js.Array[FileEntry], Unit] = ???): Unit = js.native
  def getDisplayPath(entry: Entry, callback: js.Function1[String, Unit] = ???): Unit = js.native
}

trait Entry extends js.Object

trait FileEntry extends Entry

trait DirectoryEntry extends Entry {
  val fullPath: String = js.native
  val name: String = js.native
  val isDirectory: Boolean = js.native
  val isFile: Boolean = js.native
}

trait ChromeStorage extends js.Object {
  val local: StorageLocal = js.native
}

trait StorageLocal extends js.Object {
  def set(map: js.Any, callback: js.Function0[Unit] = ???): Unit = js.native
  def get(key: js.Array[String], callback: js.Function1[js.Dictionary[String], Unit] = ???): Unit = js.native
}
