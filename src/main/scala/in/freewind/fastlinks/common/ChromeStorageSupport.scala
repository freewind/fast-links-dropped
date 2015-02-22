package in.freewind.fastlinks.common

import in.freewind.fastlinks.wrappers.chrome._

import scala.concurrent.{Future, Promise}
import scala.scalajs.js
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

trait ChromeStorageSupport[T] {
  val Key: String
  def fromJson(json: String): T
  def toJson(obj: T): String

  def load(): Future[Option[T]] = {
    val p = Promise[Option[T]]()
    chrome.storage.local.get(js.Array(Key), (items: js.Dictionary[String]) => {
      p.success(items.get(Key).map(fromJson))
      ()
    })
    p.future
  }

  def save(data: T): Future[T] = {
    val p = Promise[T]()
    chrome.storage.local.set(js.Dynamic.literal(Key -> toJson(data)), () => {
      p.success(data)
      ()
    })
    p.future
  }
}
