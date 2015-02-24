package in.freewind.fastlinks.chrome_app

import com.xored.scalajs.react.util.TypedEventListeners
import com.xored.scalajs.react.{TypedReactSpec, scalax}
import in.freewind.fastlinks.chrome_app.config.{Header, ProjectList, ProjectProfile}
import in.freewind.fastlinks.common.Search
import in.freewind.fastlinks.{Category, Meta, Project}

object ConfigPage extends TypedReactSpec with TypedEventListeners {

  case class State(currentCategory: Option[Category] = None,
                   currentProject: Option[Project] = None)

  case class Props(meta: Option[Meta] = None, allowEditing: Boolean, appBackend: AppBackend)

  override def getInitialState(self: This) = State()

  implicit class Closure(self: This) {

    import self._

    val backend = self.props.appBackend
    def onSelectProject(project: Project) = {
      setState(state.copy(currentProject = Some(project)))
    }

    def onNewProject(projectName: String) = {
      for {
        meta <- props.meta
        currentCategory <- state.currentCategory
        newProject = Project(projectName)
        newCurrentCategory = currentCategory.copy(projects = currentCategory.projects :+ newProject)
      } {
        setState(state.copy(currentCategory = Some(newCurrentCategory), currentProject = Some(newProject)))
        backend.updateMeta(meta.copy(categories = meta.categories.replace(currentCategory, newCurrentCategory)))
      }
    }

    def updateProject(oldProject: Project, newProject: Project): Unit = {
      for {
        meta <- props.meta
        currentCategory <- state.currentCategory
        newCurrentCategory = currentCategory.copy(projects = currentCategory.projects.replace(oldProject, newProject))
      } {
        setState(state.copy(currentCategory = Some(newCurrentCategory), currentProject = Some(newProject)))
        backend.updateMeta(meta.copy(categories = meta.categories.replace(currentCategory, newCurrentCategory)))
      }
    }

    def selectCategory(category: Category): Unit = {
      setState(state.copy(currentCategory = Some(category), currentProject = category.projects.headOption))
    }

  }

  @scalax
  override def render(self: This) = {
    import self._
    val appBackend = self.props.appBackend
    val (currentCategory, currentProject) = (self.state.currentCategory, self.state.currentProject)
    <div id="config-page">
      { Header(Header.Props(props.meta, self.state.currentCategory, props.allowEditing, self.selectCategory, appBackend)) }
      <div className="project-list">
        { Search(Search.Props(state.currentCategory.map(_.projects).getOrElse(Nil))) }
        { ProjectList(ProjectList.Props(currentCategory, currentProject, self.onSelectProject, self.onNewProject)) }
      </div>
      <div className="project-profile">
        {
          self.state.currentProject match {
            case Some(project) => ProjectProfile(ProjectProfile.Props(props.allowEditing, project, self.updateProject))
            case _ => <div></div>
          }
        }
      </div>
    </div>
  }

}

