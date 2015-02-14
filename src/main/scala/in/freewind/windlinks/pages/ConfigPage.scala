package in.freewind.windlinks.pages

import com.xored.scalajs.react.util.TypedEventListeners
import com.xored.scalajs.react.{TypedReactSpec, scalax}
import in.freewind.windlinks.pages.config.{ProjectList, ProjectProfile}
import in.freewind.windlinks.{Project, SampleData}

object ConfigPage extends TypedReactSpec with TypedEventListeners {

  case class State(projects: Seq[Project] = SampleData.projects, currentProject: Option[Project] = SampleData.projects.headOption)

  case class Props()

  override def getInitialState(self: This) = State()

  implicit class Closure(self: This) {

    import self._

    def onSelectProject(project: Project) = {
      setState(state.copy(currentProject = Some(project)))
    }

    def onNewProject(projectName: String) = {
      val newProject = Project(projectName)
      setState(state.copy(projects = newProject +: state.projects, currentProject = Some(newProject)))
    }
  }

  @scalax
  override def render(self: This) = {
    val projects = self.state.projects
    val projectProfile = self.state.currentProject match {
      case Some(p) => ProjectProfile(ProjectProfile.Props(p))
      case _ => <div></div>
    }

    <div id="config-page">
      <div className="project-list">
        {ProjectList(ProjectList.Props(projects, self.onSelectProject, self.onNewProject))}
      </div>
      <div className="project-profile">
        {projectProfile}
      </div>
    </div>
  }

}

