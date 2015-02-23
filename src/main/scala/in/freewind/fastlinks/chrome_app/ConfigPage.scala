package in.freewind.fastlinks.chrome_app

import com.xored.scalajs.react.util.TypedEventListeners
import com.xored.scalajs.react.{TypedReactSpec, scalax}
import in.freewind.fastlinks.chrome_app.config.{Header, ProjectList, ProjectProfile}
import in.freewind.fastlinks.libs.Chrome
import in.freewind.fastlinks.wrappers.chrome.DirectoryEntry
import in.freewind.fastlinks.{MetaFiles, Category, Meta, Project}

import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

object ConfigPage extends TypedReactSpec with TypedEventListeners {

  case class State(meta: Option[Meta] = None,
                   currentCategory: Option[Category] = None,
                   currentProject: Option[Project] = None,
                   storageData: Option[AppStorageData] = None)

  case class Props(goToMainPage: () => Unit)

  override def getInitialState(self: This) = {
    import self._
    AppStorageData.load().foreach {
      case Some(data@AppStorageData(Some(localPath), Some(localId))) =>
        setState(state.copy(storageData = Some(data)))
        Chrome.fileSystem.isRestorable(localId).foreach {
          case true => Chrome.fileSystem.restore(localId).foreach { entry =>
            val dir = entry.asInstanceOf[DirectoryEntry]
            MetaFiles.readMeta(dir).foreach(m => setState(state.copy(meta = Some(m))))
          }
          case _ => println("#### data dir can't be restored")
        }
      case _ =>
    }
    State()
  }


  implicit class Closure(self: This) {

    import self._

    def onSelectProject(project: Project) = {
      setState(state.copy(currentProject = Some(project)))
    }

    def onNewProject(projectName: String) = {
      val newProject = Project(projectName)
      setState(state.copy(
        currentCategory = self.state.currentCategory.map(c => c.copy(projects = c.projects :+ newProject)),
        currentProject = Some(newProject)))
    }

    def updateProject(oldProject: Project, newProject: Project): Unit = {
      setState(state.copy(
        currentCategory = self.state.currentCategory.map(c => c.copy(projects = c.projects.replace(oldProject, newProject))),
        currentProject = Some(newProject)))
    }

    def selectCategory(category: Category): Unit = {
      setState(state.copy(currentCategory = Some(category), currentProject = category.projects.headOption))
    }

    def saveStorageData(storageData: AppStorageData): Unit = {
      AppStorageData.save(storageData).foreach(data => setState(state.copy(storageData = Some(data))))
    }

  }

  @scalax
  override def render(self: This) = {
    val (currentCategory, currentProject) = (self.state.currentCategory, self.state.currentProject)
    <div id="config-page">
      { Header(Header.Props(self.state.meta, self.selectCategory, self.saveStorageData, self.props.goToMainPage)) }
      <div className="project-list">
        {ProjectList(ProjectList.Props(currentCategory, currentProject, self.onSelectProject, self.onNewProject))}
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

