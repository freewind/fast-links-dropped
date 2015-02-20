package in.freewind.windlinks.wrappers.chrome

import scalajs.js

trait Chrome extends js.Object {
  val storage: ChromeStorage = js.native
}

trait ChromeStorage extends js.Object {
  val local: StorageLocal = js.native
}

trait StorageLocal extends js.Object {
  def set(map: js.Any, callback: js.Function0[Unit] = ???): Unit = js.native
  def get(key: js.Array[String], callback: js.Function1[js.Dictionary[String], Unit] = ???): Unit = js.native
}
