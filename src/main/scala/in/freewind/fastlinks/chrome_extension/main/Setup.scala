package in.freewind.fastlinks.chrome_extension.main

import com.xored.scalajs.react.util.TypedEventListeners
import com.xored.scalajs.react.{TypedReactSpec, scalax}
import org.scalajs.dom.HTMLInputElement
import org.scalajs.dom.extensions.Ajax

import scala.concurrent.ExecutionContext.Implicits.global

object Setup extends TypedReactSpec with TypedEventListeners {

  case class State(working: Boolean = false, loading: Boolean = false)

  case class Props(onDataFetched: (String, String) => Unit, dataUrl: Option[String] = None)

  override def getInitialState(self: This) = State()

  implicit class Closure(self: This) {

    import self._

    val fetchData = input.onClick(e => {
      val url = refs("url").getDOMNode().asInstanceOf[HTMLInputElement].value.trim
      setState(state.copy(loading = true))
      val future = Ajax.get(url)
      future.onSuccess { case xhr =>
        props.onDataFetched(url, xhr.responseText)
        setState(state.copy(loading = false))
      }
      future.onFailure { case _ =>
        setState(state.copy(loading = false))
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
            <div>
              <input placeholder="url of projects data" ref="url" defaultValue={dataUrl.getOrElse("")} className="url"/>
              {
                self.state.loading match {
                  case true => <span className="loading"></span>
                  case false => <button onClick={self.fetchData}>Fetch</button>
                }
              }
            </div>
            <div className="help-links">
              <a href="https://github.com/freewind/fast-links" target="_blank">project link</a>
              |
              <a href="https://github.com/freewind/fast-links" target="_blank">how to edit</a>
            </div>
          </div>
        } else {
          <div onClick={self.openSetup} className="banner">setup [+]</div>
        }
      }
    </div>
  }

}
