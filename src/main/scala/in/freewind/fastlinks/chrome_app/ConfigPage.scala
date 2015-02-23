package in.freewind.fastlinks.chrome_app

import com.xored.scalajs.react.util.TypedEventListeners
import com.xored.scalajs.react.{TypedReactSpec, scalax}
import in.freewind.fastlinks.chrome_app.config.{Header, ProjectList, ProjectProfile}
import in.freewind.fastlinks.libs.Chrome
import in.freewind.fastlinks.wrappers.chrome.DirectoryEntry
import in.freewind.fastlinks.{Category, Meta, Project}
import upickle._

import scala.concurrent.{Future, Promise}
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

object ConfigPage extends TypedReactSpec with TypedEventListeners {

  case class State(meta: Option[Meta] = None,
                   currentCategory: Option[Category] = None,
                   currentProject: Option[Project] = None,
                   storageData: Option[AppStorageData] = None)

  case class Props()

  private def readMeta(dir: DirectoryEntry): Future[Meta] = {
    val p = Promise[Meta]()
    Chrome.fileSystem.readFile(dir, "_meta_.json").map(read[_Meta_]).foreach { _meta_ =>
      val categoryFutures = _meta_.categories.map(category =>
        Chrome.fileSystem.readFile(dir, category.filename).map(read[Seq[Project]])
          .map(ps => new Category(category.name, ps, category.description))
      )
      Future.sequence(categoryFutures).foreach { categories =>
        p.success(new Meta(categories))
      }
    }
    p.future
  }

  override def getInitialState(self: This) = {
    import self._
    AppStorageData.load().foreach {
      case Some(data@AppStorageData(Some(localPath), Some(localId))) =>
        setState(state.copy(storageData = Some(data)))
        Chrome.fileSystem.isRestorable(localId).foreach {
          case true => Chrome.fileSystem.restore(localId).foreach { entry =>
            val dir = entry.asInstanceOf[DirectoryEntry]
            readMeta(dir).foreach(m => setState(state.copy(meta = Some(m))))
          }
          case _ => println("#### no restored")
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
      { Header(Header.Props(self.state.meta, self.selectCategory, self.saveStorageData)) }
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

case class _Meta_(categories: Seq[_Category_])

case class _Category_(name: String, filename: String, description: Option[String])
