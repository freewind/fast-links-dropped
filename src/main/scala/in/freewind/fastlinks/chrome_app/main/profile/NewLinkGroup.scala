package in.freewind.fastlinks.chrome_app.main.profile

import com.xored.scalajs.react.{scalax, TypedReactSpec}
import com.xored.scalajs.react.util.TypedEventListeners

object NewLinkGroup extends TypedReactSpec with TypedEventListeners {

  case class State()

  case class Props(newGroupName: String => Unit)

  override def getInitialState(self: This) = State()

  implicit class Closure(self: This) {
    import self._
    val startEditing = button.onClick(e => {
      props.newGroupName("new link group name")
    })
  }

  @scalax
  override def render(self: This) = {
     <div><button onClick={self.startEditing}>+</button></div>
  }

}
