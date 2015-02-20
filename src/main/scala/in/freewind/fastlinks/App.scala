package in.freewind.fastlinks

import com.xored.scalajs.react.React
import in.freewind.fastlinks.pages.MainPage
import org.scalajs.dom
import org.scalajs.dom.HTMLElement

import scala.scalajs.js.JSApp
import scala.scalajs.js.annotation.JSExport

object App extends JSApp {

  @JSExport
  def hello(parent: HTMLElement) = {
    React.renderComponent(
      Hello(Hello.Props()),
      parent
    )
  }

  override def main(): Unit = {
    React.renderComponent(
      MainPage(MainPage.Props()),
      dom.document.getElementById("content").asInstanceOf[HTMLElement]
    )
  }

}
