package in.freewind.fastlinks.chrome_app

import com.xored.scalajs.react.{scalax, TypedReactSpec}
import com.xored.scalajs.react.util.TypedEventListeners

object AppEntry extends TypedReactSpec with TypedEventListeners {

  sealed trait Page

  case object SelectedMainPage extends Page

  case object SelectedConfigPage extends Page

  case class State(currentPage: Page)

  case class Props()

  override def getInitialState(self: This) = State(SelectedMainPage)

  implicit class Closure(self: This) {

    import self._

    def selectPage(page: Page) = () => {
      setState(state.copy(currentPage = page))
    }

  }

  @scalax
  override def render(self: This) = {
    self.state.currentPage match {
      case SelectedMainPage => MainPage(MainPage.Props(goToConfigPage = self.selectPage(SelectedConfigPage)))
      case SelectedConfigPage => ConfigPage(ConfigPage.Props(goToMainPage = self.selectPage(SelectedMainPage)))
    }
  }

}
