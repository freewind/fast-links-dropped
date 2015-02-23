package in.freewind.fastlinks.chrome_app.main

import com.xored.scalajs.react.{scalax, TypedReactSpec}
import com.xored.scalajs.react.util.TypedEventListeners

object Header extends TypedReactSpec with TypedEventListeners {

  case class State()

  case class Props(goToConfigPage: () => Unit)

  override def getInitialState(self: This) = State()

  implicit class Closure(self: This) {

    val goToConfigPage = button.onClick(e => {
      self.props.goToConfigPage()
    })
  }

  @scalax
  override def render(self: This) = {
    <div>
      <button onClick={self.goToConfigPage}>Go to config page</button>
    </div>
  }

}
