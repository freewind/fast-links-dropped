package in.freewind.fastlinks.chrome_app.main

import com.xored.scalajs.react.util.{ClassName, TypedEventListeners}
import com.xored.scalajs.react.{TypedReactSpec, scalax}
import in.freewind.fastlinks.{Category, Project}
import org.scalajs.dom.extensions.KeyCode

object ProjectList extends TypedReactSpec with TypedEventListeners {

  case class State()

  case class Props(currentCategory: Option[Category],
                   currentProject: Option[Project],
                   allowEditing: Boolean,
                   onSelectProject: Project => Unit,
                   onNewProject: String => Unit)

  override def getInitialState(self: This) = State()

  implicit class Closure(self: This) {
    def selectProject(project: Project) = button.onClick(e => {
      self.props.onSelectProject(project)
    })

    def newProject = input.onKeyUp(e => {
      val value = e.target.value.trim
      if (!value.isEmpty && e.which == KeyCode.enter) {
        self.props.onNewProject(value)
      }
    })
  }

  private def showStars(stars: Option[Int]) = stars match {
    case Some(n) => (1 to n).toList.map(_ => "★")
    case _ => ""
  }

  @scalax
  override def render(self: This) = {
    val projects = self.props.currentCategory.toList.flatMap(_.projects)
    <div>
      {
        projects.map { p =>
          val className = ClassName("current-project" -> (Some(p) == self.props.currentProject))
          <div>
            <button onClick={self.selectProject(p)} className={className}>
              {p.name}
            </button>
            <span className="stars">{showStars(p.stars)}</span>
          </div>
        }
      }
      {
        if (self.props.allowEditing) {
          <div>
            <input type="text" onKeyUp={self.newProject} placeholder="new project"/>
          </div>
        } else None
      }
    </div>
  }

}
