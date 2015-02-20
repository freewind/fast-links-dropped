package in.freewind.windlinks.pages.main

import com.xored.scalajs.react.util.TypedEventListeners
import com.xored.scalajs.react.{TypedReactSpec, scalax}
import org.scalajs.dom.HTMLInputElement
import org.scalajs.dom.extensions.Ajax

import scala.concurrent.ExecutionContext.Implicits.global

object Setup extends TypedReactSpec with TypedEventListeners {

  case class State(working: Boolean = false)

  case class Props(fetchedData: String => Unit)

  override def getInitialState(self: This) = State()

  implicit class Closure(self: This) {

    import self._

    val fetchData = input.onKeyUp(e => {
      val url = refs("url").getDOMNode().asInstanceOf[HTMLInputElement].value.trim
      Ajax.get(url).onSuccess { case xhr =>
        props.fetchedData(xhr.responseText)
      }
    })

    val openSetup = element.onClick(e => {
      setState(state.copy(working = !state.working))
    })

  }

  @scalax
  override def render(self: This) = {
    <div className="setup">
      {
        if (self.state.working) {
          <div onClick={self.openSetup}>setup [-]</div>
          <div>
            <input placeholder="url of projects data" ref="url" />
            <button onClick={self.fetchData}>Fetch</button>
          </div>
        } else {
          <div onClick={self.openSetup}>setup [+]</div>
        }
      }
    </div>
  }

}
