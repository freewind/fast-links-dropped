package in.freewind.fastlinks

import com.xored.scalajs.react.{TypedReactSpec, scalax}

object Hello extends TypedReactSpec {

  case class State()

  case class Props()

  override def getInitialState(self: This) = new State()

  @scalax
  override def render(self: This) = {
    <div>Hello!</div>
  }

}
