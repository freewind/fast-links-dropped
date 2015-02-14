package in.freewind.windlinks.pages.main

import com.xored.scalajs.react.util.TypedEventListeners
import com.xored.scalajs.react.{TypedReactSpec, scalax}

object Search extends TypedReactSpec with TypedEventListeners {

  case class State()

  case class Props(onSearch: String => Unit)

  override def getInitialState(self: This) = State()

  implicit class Closure(self: This) {
    val onChange = input.onChange(e => {
      val value = e.target.value
      self.props.onSearch(value)
    })

  }

  @scalax
  override def render(self: This) = {
    <input placeholder="Search" onChange={self.onChange}></input>
  }

}
