package in.freewind.fastlinks.chrome_app

import com.xored.scalajs.react.util.TypedEventListeners
import com.xored.scalajs.react.{TypedReactSpec, scalax}
import in.freewind.fastlinks._
import in.freewind.fastlinks.chrome_app.main.Header

object MainPage extends TypedReactSpec with TypedEventListeners {

  case class State(meta: Option[Meta] = None)

  case class Props(goToConfigPage: () => Unit)

  override def getInitialState(self: This) = {
    State()
  }

  @scalax
  override def render(self: This) = {
    Header(Header.Props(self.props.goToConfigPage))
  }

}
