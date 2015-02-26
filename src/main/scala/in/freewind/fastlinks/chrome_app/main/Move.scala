package in.freewind.fastlinks.chrome_app.main

import com.xored.scalajs.react.util.TypedEventListeners
import com.xored.scalajs.react.{scalax, TypedReactSpec}

object Move extends TypedReactSpec with TypedEventListeners {

  case class State()

  case class Props(moveUp: () => Unit, moveDown: () => Unit)

  override def getInitialState(self: This) = State()

  implicit class Closure(self: This) {
    import self._
    val moveUp = element.onClick(e => {
      e.stopPropagation()
      props.moveUp()
    })
    val moveDown = element.onClick(e => {
      e.stopPropagation()
      props.moveDown()
    })
  }

  @scalax
  override def render(self: This) = {
    <span className="move">
      <span className="move-up" onClick={self.moveUp}>⬆︎</span>
      <span className="move-down" onClick={self.moveDown}>⬇︎</span>
    </span>
  }

}
