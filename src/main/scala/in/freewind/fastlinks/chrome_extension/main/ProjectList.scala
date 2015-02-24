package in.freewind.fastlinks.chrome_extension.main

import com.xored.scalajs.react.util.TypedEventListeners
import com.xored.scalajs.react.{TypedReactSpec, scalax}
import in.freewind.fastlinks.Project

object ProjectList extends TypedReactSpec with TypedEventListeners {

  case class State()

  case class Props(projects: Seq[Project])

  override def getInitialState(self: This) = State()

  @scalax
  override def render(self: This) = {
    <div className="all-links">
      {
        self.props.projects.map(p =>
          OneProject(OneProject.Props(p))
        )
      }
    </div>
  }

}
