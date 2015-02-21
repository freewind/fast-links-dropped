package in.freewind.fastlinks.pages.main

import com.xored.scalajs.react.util.TypedEventListeners
import com.xored.scalajs.react.{TypedReactSpec, scalax}
import org.scalajs.dom.HTMLInputElement
import org.scalajs.dom.extensions.Ajax

import scala.concurrent.ExecutionContext.Implicits.global

object Setup extends TypedReactSpec with TypedEventListeners {

  case class State(working: Boolean = false)

  case class Props(onDataFetched: (String, String) => Unit, dataUrl: Option[String] = None)

  override def getInitialState(self: This) = State()

  implicit class Closure(self: This) {

    import self._

    val fetchData = input.onKeyUp(e => {
      val url = refs("url").getDOMNode().asInstanceOf[HTMLInputElement].value.trim
      Ajax.get(url).onSuccess { case xhr =>
        props.onDataFetched(url, xhr.responseText)
      }
    })

    val openSetup = element.onClick(e => {
      setState(state.copy(working = !state.working))
    })

  }

  @scalax
  override def render(self: This) = {
    val dataUrl = self.props.dataUrl
    <div className="setup">
      {
        if (self.state.working) {
          <div onClick={self.openSetup} className="banner">setup [-]</div>
          <div className="setup-body">
            <input placeholder="url of projects data" ref="url" defaultValue={dataUrl.getOrElse("")} className="url"/>
            <button onClick={self.fetchData}>Fetch</button>
          </div>
        } else {
          <div onClick={self.openSetup} className="banner">setup [+]</div>
        }
      }
    </div>
  }

}
