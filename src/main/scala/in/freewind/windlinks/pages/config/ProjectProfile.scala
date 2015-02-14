package in.freewind.windlinks.pages.config

import com.xored.scalajs.react.{scalax, TypedReactSpec}
import com.xored.scalajs.react.util.TypedEventListeners
import in.freewind.windlinks.Project

object ProjectProfile extends TypedReactSpec with TypedEventListeners {

  case class State()

  case class Props(project: Project, updateDescOfProject: (Project, String) => Unit)

  override def getInitialState(self: This) = State()

  implicit class Closure(self: This) {
    def updateDesc(desc: String) = {
      self.props.updateDescOfProject(self.props.project, desc)
    }
  }

  @scalax
  override def render(self: This) = {
    val project = self.props.project
    val desc = ProjectDescription(ProjectDescription.Props(project.description, self.updateDesc))
    <div>
      <div className="project-name">
        {project.name}
      </div>{desc}<div className="project-links">
      {project.links.map(link =>
        <div>
          {link.url}
        </div>)}
    </div>
    </div>
  }

}
