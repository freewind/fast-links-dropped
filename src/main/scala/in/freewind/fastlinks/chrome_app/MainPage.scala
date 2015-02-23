package in.freewind.fastlinks.chrome_app

import com.xored.scalajs.react.util.TypedEventListeners
import com.xored.scalajs.react.{TypedReactSpec, scalax}
import in.freewind.fastlinks._
import in.freewind.fastlinks.chrome_app.main.Header
import in.freewind.fastlinks.chrome_extension.FastLinks

object MainPage extends TypedReactSpec with TypedEventListeners {

  case class State(currentCategory: Option[Category] = None, currentProject: Option[Project] = None)

  case class Props(meta: Option[Meta] = None, appBackend: AppBackend)

  override def getInitialState(self: This) = {
    import self._
    props.meta.map { meta =>
      val currentCategory = meta.categories.headOption
      val currentProject = currentCategory.flatMap(_.projects.headOption)
      State(currentCategory = currentCategory, currentProject = currentProject)
    }.getOrElse(State())
  }

  @scalax
  override def render(self: This) = {
    <div>
      { Header(Header.Props(self.props.appBackend)) }
      <div>
        {
          FastLinks(FastLinks.Props(projects = self.state.currentCategory.map(_.projects).getOrElse(Nil)))
        }
      </div>
    </div>
  }

}
