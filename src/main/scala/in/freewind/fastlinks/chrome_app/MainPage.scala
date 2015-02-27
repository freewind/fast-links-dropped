package in.freewind.fastlinks.chrome_app

import com.xored.scalajs.react.util.TypedEventListeners
import com.xored.scalajs.react.{TypedReactSpec, scalax}
import in.freewind.fastlinks.chrome_app.main.{MoveUpSupport, Header, ProjectList, ProjectProfile}
import in.freewind.fastlinks.common.{Dialog, Search}
import in.freewind.fastlinks.{Category, Meta, Project}

object MainPage extends TypedReactSpec with TypedEventListeners {

  case class State(currentCategory: Option[Category] = None,
                   currentProject: Option[Project] = None)

  case class Props(meta: Option[Meta] = None, allowEditing: Boolean, dialogContext: Option[Dialog.DialogContext], appBackend: AppBackend)

  override def getInitialState(self: This) = State()

  override def componentWillReceiveProps(self: MainPage.This, nextProps: MainPage.This#Props): Unit = {
    import self._
    if (state.currentCategory.isEmpty) {
      val firstCategory = nextProps.meta.flatMap(_.categories.headOption)
      val currentProject = firstCategory.flatMap(_.projects.headOption)
      setState(state.copy(currentCategory = firstCategory, currentProject = currentProject))
    }
  }

  implicit class Closure(self: This) extends MoveUpSupport {

    import self._

    val backend = self.props.appBackend
    def onSelectProject(project: Project) = {
      setState(state.copy(currentProject = Some(project)))
    }

    def updateCurrentProjects(newProjects: Seq[Project], currentProject: Option[Project]): Unit = {
      for {
        meta <- props.meta
        currentCategory <- state.currentCategory
        newCurrentCategory = currentCategory.copy(projects = newProjects)
      } {
        setState(state.copy(currentCategory = Some(newCurrentCategory), currentProject = currentProject), () => {
          backend.updateMeta(meta.copy(categories = meta.categories.replace(currentCategory, newCurrentCategory)))
        })
      }
    }

    def updateProject(oldProject: Project, newProject: Project): Unit = {
      for {
        meta <- props.meta
        currentCategory <- state.currentCategory
        newCurrentCategory = currentCategory.copy(projects = currentCategory.projects.replace(oldProject, newProject))
      } {
        setState(state.copy(currentCategory = Some(newCurrentCategory), currentProject = Some(newProject)), () => {
          backend.updateMeta(meta.copy(categories = meta.categories.replace(currentCategory, newCurrentCategory)))
        })
      }
    }

    def selectCategory(category: Category): Unit = {
      setState(state.copy(currentCategory = Some(category), currentProject = category.projects.headOption))
    }

    // FIXME remove duplication
    def deleteProject(project: Project) = () => {
      for {
        meta <- props.meta
        currentCategory <- state.currentCategory
        newCurrentCategory = currentCategory.copy(projects = currentCategory.projects.remove(project))
      } {
        setState(state.copy(currentCategory = Some(newCurrentCategory), currentProject = newCurrentCategory.projects.headOption), () => {
          backend.updateMeta(meta.copy(categories = meta.categories.replace(currentCategory, newCurrentCategory)))
        })
      }

    }

    def deleteCategory(category: Category): Unit = {
      for {
        meta <- props.meta
        categories = meta.categories.remove(category)
      } {
        if (state.currentCategory == Some(category)) {
          setState(state.copy(currentCategory = categories.headOption, currentProject = categories.headOption.flatMap(_.projects.headOption)), () => {
            backend.updateMeta(meta.copy(categories = categories))
          })
        } else {
          backend.updateMeta(meta.copy(categories = categories))
        }
      }
    }

    def newCategory(name: String): Unit = {
      val newCategory = new Category(name, projects = Nil)
      for {
        meta <- props.meta
        categories = meta.categories :+ newCategory
      } {
        setState(state.copy(currentCategory = Some(newCategory), currentProject = None), () => {
          backend.updateMeta(meta.copy(categories = categories))
        })
      }
    }

    def moveCategoryUp(category: Category): Unit = {
      props.meta.foreach { meta =>
        backend.updateMeta(meta = meta.copy(categories = moveUp(meta.categories, category)))
      }
    }

    def moveCategoryDown(category: Category): Unit = {
      props.meta.foreach { meta =>
        backend.updateMeta(meta = meta.copy(categories = moveUp(meta.categories.reverse, category).reverse))
      }
    }
  }

  @scalax
  override def render(self: This) = {
    import self._
    val appBackend = self.props.appBackend
    val (currentCategory, currentProject) = (self.state.currentCategory, self.state.currentProject)
    <div id="main-page">
      { Header(Header.Props(props.meta, self.state.currentCategory, props.allowEditing, self.selectCategory, self.deleteCategory, self.newCategory, self.moveCategoryUp, self.moveCategoryDown, appBackend)) }
      { Search(Search.Props(state.currentCategory.map(_.projects).getOrElse(Nil))) }
      <div className="sidebar">
        <div className="project-list">
          {
            val projects = currentCategory.map(_.projects).getOrElse(Nil)
            ProjectList(ProjectList.Props(projects, currentProject, self.props.allowEditing, self.onSelectProject, self.updateCurrentProjects))
          }
        </div>
      </div>
      <div className="project-profile">
        {
          self.state.currentProject match {
            case Some(project) => ProjectProfile(ProjectProfile.Props(props.allowEditing, project, self.updateProject, self.deleteProject(project), props.appBackend))
            case _ => None
          }
        }
      </div>
      { props.dialogContext.map(ctx => Dialog(Dialog.Props(ctx, appBackend))) }
    </div>
  }

}

