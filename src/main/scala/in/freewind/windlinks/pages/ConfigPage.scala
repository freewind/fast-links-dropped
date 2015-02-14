package in.freewind.windlinks.pages

import com.xored.scalajs.react.util.TypedEventListeners
import com.xored.scalajs.react.{TypedReactSpec, scalax}
import in.freewind.windlinks.pages.config.{ProjectList, ProjectProfile}
import in.freewind.windlinks.{Project, SampleData}

object ConfigPage extends TypedReactSpec with TypedEventListeners {

  case class State(currentProject: Option[Project] = None)

  case class Props()

  override def getInitialState(self: This) = State()

  implicit class Closure(self: This) {

    import self._

    def onSelectProject(project: Project) = {
      setState(state.copy(currentProject = Some(project)))
    }
  }

  @scalax
  override def render(self: This) = {
    val projectProfile = self.state.currentProject match {
      case Some(p) => ProjectProfile(ProjectProfile.Props(p))
      case _ => <div></div>
    }

    <div>
      <div>
        {ProjectList(ProjectList.Props(projects, self.onSelectProject))}
      </div>
      <div>
        {projectProfile}
      </div>
    </div>
  }

  private val projects = SampleData.projects

}

