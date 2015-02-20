package in.freewind.fastlinks.pages.config

import com.xored.scalajs.react.util.{ClassName, TypedEventListeners}
import com.xored.scalajs.react.{TypedReactSpec, scalax}
import in.freewind.fastlinks.{Keycode, Project}

object ProjectList extends TypedReactSpec with TypedEventListeners {

  case class State()

  case class Props(projects: Seq[Project],
                   currentProject: Option[Project],
                   onSelectProject: Project => Unit,
                   onNewProject: String => Unit)

  override def getInitialState(self: This) = State()

  implicit class Closure(self: This) {
    def selectProject(project: Project) = button.onClick(e => {
      self.props.onSelectProject(project)
    })

    def newProject = input.onKeyUp(e => {
      val value = e.target.value.trim
      if (!value.isEmpty && e.which == Keycode.Enter) {
        self.props.onNewProject(value)
      }
    })
  }

  @scalax
  override def render(self: This) = {
    <div>
      {
        self.props.projects.map { p =>
          val className = ClassName("current-project" -> (Some(p) == self.props.currentProject))
          <div>
            <button onClick={self.selectProject(p)} className={className}>
              {p.name}
            </button>
          </div>
        }
      }
      <div>
        <input type="text" onKeyUp={self.newProject} placeholder="new project"/>
      </div>
    </div>
  }

}
