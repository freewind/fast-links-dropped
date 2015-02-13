package in.freewind.windlinks

import com.xored.scalajs.react.React
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

}
