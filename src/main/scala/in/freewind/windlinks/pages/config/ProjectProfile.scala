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
      <div className="project-name">
        {project.name}
      </div>
      <div className="project-desc">
        {project.description match {
        case Some(desc) => <div>
          {desc}
        </div>
        case _ => <div>click to add description ...</div>
      }}
      </div>
      <div className="project-links">
        {project.links.map(link =>
        <div>
          {link.url}
        </div>)}
      </div>
    </div>
  }

}
