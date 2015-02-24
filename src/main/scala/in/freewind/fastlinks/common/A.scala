package in.freewind.fastlinks.common

import com.xored.scalajs.react.util.TypedEventListeners
import com.xored.scalajs.react.{scalax, TypedReactSpec}

object A extends TypedReactSpec with TypedEventListeners {

  case class State()
  case class Props(url: String, text: String)

  override def getInitialState(self: This) = State()

  @scalax
  override def render(self: This) = {
    <a href={self.props.url} target="_blank">{self.props.text}</a>
  }

}
