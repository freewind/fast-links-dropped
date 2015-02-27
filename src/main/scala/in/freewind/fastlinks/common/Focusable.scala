package in.freewind.fastlinks.common

import com.xored.scalajs.react.ReactDOMRef

import scala.scalajs.js

trait Focusable extends js.Object {
  def focus(): Unit = js.native
}

object Focusable {
  def cast(ref: ReactDOMRef): Option[Focusable] = {
    if (ref.hasOwnProperty("focus")) Some(ref.asInstanceOf[Focusable])
    else None
  }
}
