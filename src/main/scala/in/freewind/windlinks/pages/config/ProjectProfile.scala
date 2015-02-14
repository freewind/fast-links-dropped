package in.freewind.windlinks.pages.config

import com.xored.scalajs.react.{scalax, TypedReactSpec}
import com.xored.scalajs.react.util.TypedEventListeners
import in.freewind.windlinks.Project

object ProjectProfile extends TypedReactSpec with TypedEventListeners {

  case class State()

  case class Props(project: Project)

  override def getInitialState(self: This) = State()

  implicit class Closure(self: This) {
  }

  @scalax
  override def render(self: This) = {
    val project = self.props.project
    <div>
      <div>
        {project.name}
      </div>
      <div>
        {project.description}
      </div>
      <div>
        {project.links.map(link =>
        <div>
          {link.url}
        </div>)}
      </div>
    </div>
  }

}
