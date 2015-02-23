package in.freewind.fastlinks.chrome_app.main

import com.xored.scalajs.react.{scalax, TypedReactSpec}
import com.xored.scalajs.react.util.TypedEventListeners
import in.freewind.fastlinks.chrome_app.AppBackend

object Header extends TypedReactSpec with TypedEventListeners {

  case class State()

  case class Props(appBackend: AppBackend)

  override def getInitialState(self: This) = State()

  implicit class Closure(self: This) {

    val goToConfigPage = button.onClick(e => {
      self.props.appBackend.goToConfigPage()
    })
  }

  @scalax
  override def render(self: This) = {
    <div>
      <button onClick={self.goToConfigPage}>Go to config page</button>
    </div>
  }

}
