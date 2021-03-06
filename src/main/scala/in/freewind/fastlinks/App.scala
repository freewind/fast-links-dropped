package in.freewind.fastlinks

import com.xored.scalajs.react.React
import in.freewind.fastlinks.chrome_app.AppEntry
import in.freewind.fastlinks.chrome_extension.ExtensionEntry
import org.scalajs.dom.HTMLElement

import scala.scalajs.js.annotation.JSExport

@JSExport("App")
object App {

  @JSExport
  def extension(parent: HTMLElement) = {
    React.renderComponent(
      ExtensionEntry(ExtensionEntry.Props()),
      parent
    )
  }

  @JSExport
  def app(parent: HTMLElement) = {
    React.renderComponent(
      AppEntry(AppEntry.Props()),
      parent
    )
  }

}
