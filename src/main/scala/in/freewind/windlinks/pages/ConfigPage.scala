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

    def updateDesc(project: Project, newDesc: String) = {
      val projects = state.projects
      val newProject = project.copy(description = Option(newDesc).filterNot(_.isEmpty))

      val newProjects = projects.map {
        case p if p == project => newProject
        case p => p
      }
      setState(state.copy(projects = newProjects, currentProject = Some(newProject)))
    }

    def updateProjectName(oldName: String, newName: String): Unit = {
      val projects = state.projects
      projects.find(_.name == oldName).foreach { oldProject =>
        val newProject = oldProject.copy(name = newName)

        val newProjects = projects.map {
          case p if p == oldProject => newProject
          case p => p
        }
        setState(state.copy(projects = newProjects, currentProject = Some(newProject)))
      }
    }

  }

  @scalax
  override def render(self: This) = {
    val projects = self.state.projects
    val projectProfile = self.state.currentProject match {
      case Some(p) => ProjectProfile(ProjectProfile.Props(p, self.updateDesc, self.updateProjectName))
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

