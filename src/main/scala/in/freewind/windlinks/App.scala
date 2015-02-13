package in.freewind.windlinks

import com.xored.scalajs.react.React
import in.freewind.windlinks.main.Main
import org.scalajs.dom.HTMLElement

import scala.scalajs.js.annotation.JSExport

@JSExport("App")
object App {

  @JSExport
  def hello(parent: HTMLElement) = {
    React.renderComponent(
      Hello(Hello.Props()),
      parent
    )
  }

  @JSExport
  def main(parent: HTMLElement) = {
    React.renderComponent(
      Main(Main.Props()),
      parent
    )
  }


}
