package in.freewind.fastlinks.wrappers

import scala.scalajs.js

object JsBeautifier extends js.GlobalScope {

  val js_beautify: JsBeautifierSupport = js.native

}

trait JsBeautifierSupport extends js.Object {
  def apply(content: String): String = js.native
}
