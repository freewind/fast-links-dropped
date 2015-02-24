package in.freewind.fastlinks.chrome_app.main.profile

import com.xored.scalajs.react.util.TypedEventListeners
import com.xored.scalajs.react.{TypedReactSpec, scalax}

object EditingStars extends TypedReactSpec with TypedEventListeners {

  case class State(value: Int)

  case class Props(defaultValue: Option[Int])

  override def getInitialState(self: This) = {
    State(self.props.defaultValue.getOrElse(0))
  }

  implicit class Closure(self: This) {
    import self._
    val onChange = input.onChange(e => {
      setState(state.copy(value = e.target.value.toInt))
    })
  }

  @scalax
  override def render(self: This) = {
    <input type="range" min="0" max="5" defaultValue={self.state.value} step="1" onChange={self.onChange} />
  }

}

