package in.freewind.windlinks.pages.config.profile

import com.xored.scalajs.react.{TypedReactSpec, scalax}
import com.xored.scalajs.react.util.TypedEventListeners

object ProfileStars extends TypedReactSpec with TypedEventListeners {

  case class State()

  case class Props()

  override def getInitialState(self: This) = State()

  implicit class Closure(self: This) {

    val onChange = input.onChange(e => {
      println(e.value)
    })
  }

  @scalax
  override def render(self: This) = {
    <div>stars</div>
  }

}
