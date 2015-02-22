package in.freewind.fastlinks.chrome_app

import com.xored.scalajs.react.util.TypedEventListeners
import com.xored.scalajs.react.{TypedReactSpec, scalax}
import in.freewind.fastlinks._

object AppPage extends TypedReactSpec with TypedEventListeners {

  case class State(meta: Option[Meta] = None)

  case class Props()

  override def getInitialState(self: This) = {
    State()
  }

  implicit class Closure(self: This) {
    val onChange = input.onChange(e => {
      println(e.value)
    })
  }

  @scalax
  override def render(self: This) = {
    <input placeholder="Search" onChange={self.onChange}></input>
  }

}
