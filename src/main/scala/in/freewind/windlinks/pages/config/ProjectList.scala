package in.freewind.windlinks.pages.config

import com.xored.scalajs.react.util.TypedEventListeners
import com.xored.scalajs.react.{TypedReactSpec, scalax}
import in.freewind.windlinks.Project

object ProjectList extends TypedReactSpec with TypedEventListeners {

  case class State()

  case class Props(projects: Seq[Project], onSelectProject: (Project) => Unit)

  override def getInitialState(self: This) = State()

  implicit class Closure(self: This) {
    def selectProject(project: Project) = button.onClick(e => {
      self.props.onSelectProject(project)
    })
  }

  @scalax
  override def render(self: This) = {
    <div>
      {self.props.projects.map(p =>
      <div>
        <button onClick={self.selectProject(p)}>
          {p.name}
        </button>
      </div>)}
    </div>
  }

}
