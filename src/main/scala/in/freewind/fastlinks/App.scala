package in.freewind.fastlinks

import com.xored.scalajs.react.React
import in.freewind.fastlinks.chrome_app.{ConfigPage, AppPage}
import in.freewind.fastlinks.chrome_extension.ExtensionPage
import org.scalajs.dom.HTMLElement

import scala.scalajs.js.annotation.JSExport

@JSExport("App")
object App {

  @JSExport
  def extension(parent: HTMLElement) = {
    React.renderComponent(
      ExtensionPage(ExtensionPage.Props()),
      parent
    )
  }

  @JSExport
  def app(parent: HTMLElement) = {
    React.renderComponent(
      ConfigPage(ConfigPage.Props()),
      parent
    )
  }

}
