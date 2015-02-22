package in.freewind.fastlinks.chrome_app

import com.xored.scalajs.react.util.TypedEventListeners
import com.xored.scalajs.react.{TypedReactSpec, scalax}
import in.freewind.fastlinks.chrome_app.config.{ProjectProfile, ProjectList}
import in.freewind.fastlinks.{Project, SampleData}

object ConfigPage extends TypedReactSpec with TypedEventListeners {

  case class State(projects: Seq[Project] = SampleData.projects,
                   currentProject: Option[Project] = SampleData.projects.headOption)

  case class Props()

  override def getInitialState(self: This) = State()

  implicit class Closure(self: This) {

    import self._

    def onSelectProject(project: Project) = {
      setState(state.copy(currentProject = Some(project)))
    }

    def onNewProject(projectName: String) = {
      val newProject = Project(projectName)
      setState(state.copy(projects = state.projects :+ newProject, currentProject = Some(newProject)))
    }

    def updateProject(oldProject: Project, newProject: Project): Unit = {
      val newProjects = state.projects.replace(oldProject, newProject)
      setState(state.copy(projects = newProjects, currentProject = Some(newProject)))
    }

  }

  @scalax
  override def render(self: This) = {
    val (projects, currentProject) = (self.state.projects, self.state.currentProject)
    <div id="config-page">
      <div className="project-list">
        {ProjectList(ProjectList.Props(projects, currentProject, self.onSelectProject, self.onNewProject))}
      </div>
      <div className="project-profile">
        {
          self.state.currentProject match {
            case Some(p) => ProjectProfile(ProjectProfile.Props(p, self.updateProject))
            case _ => <div></div>
          }
        }
      </div>
    </div>
  }

}
