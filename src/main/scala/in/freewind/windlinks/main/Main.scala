package in.freewind.windlinks.main

import com.xored.scalajs.react.util.TypedEventListeners
import com.xored.scalajs.react.{scalax, TypedReactSpec}

object Main extends TypedReactSpec with TypedEventListeners {

  case class State()

  case class Props()

  override def getInitialState(self: This) = State()
  @scalax
  override def render(self: This) = {
    Search(Search.Props())
  }
}
