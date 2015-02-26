package in.freewind.fastlinks.chrome_app.main

import com.xored.scalajs.react.util.{ClassName, TypedEventListeners}
import com.xored.scalajs.react.{TypedReactSpec, scalax}
import in.freewind.fastlinks.Project
import in.freewind.fastlinks.common.Stars
import org.scalajs.dom.extensions.KeyCode

object ProjectList extends TypedReactSpec with TypedEventListeners {

  case class State()

  case class Props(projects: Seq[Project],
                   currentProject: Option[Project],
                   allowEditing: Boolean,
                   onSelectProject: Project => Unit,
                   updateCurrentProjects: (Seq[Project], Option[Project]) => Unit)

  override def getInitialState(self: This) = State()

  implicit class Closure(self: This) {
    import self._
    def selectProject(project: Project) = button.onClick(e => {
      props.onSelectProject(project)
    })

    def newProject = input.onKeyUp(e => {
      val value = e.target.value.trim
      if (!value.isEmpty && e.which == KeyCode.enter) {
        val newProject = Project(value)
        props.updateCurrentProjects(props.projects :+ newProject, Some(newProject))
      }
    })

    def moveUp(project: Project) = () => {
      val newProjects = swap(props.projects, project)
      props.updateCurrentProjects(newProjects, props.currentProject)
    }

    def moveDown(project: Project) = () => {
      val newProjects = swap(props.projects.reverse, project).reverse
      props.updateCurrentProjects(newProjects, props.currentProject)
    }

    private def swap(projects: Seq[Project], target: Project): Seq[Project] = {
      projects.span(_ != target) match {
        case (Nil, right) => right
        case (left, right) => left.init ++ Seq(right.head, left.last) ++ right.tail
      }
    }
  }


  @scalax
  override def render(self: This) = {
    val projects = self.props.projects
    val allowEditing = self.props.allowEditing
    <div>
      {
        projects.map { p =>
          val className = ClassName(
            "project" -> true,
            "current-project" -> (Some(p) == self.props.currentProject)
          )
          <div className={className} onClick={self.selectProject(p)}>
            <span className="project-name">
              {p.name}
            </span>
            { Stars(Stars.Props(allowEditing = false, count = p.stars)) }
            {
              if (allowEditing) {
                Move(Move.Props(self.moveUp(p), self.moveDown(p)))
              } else None
            }
          </div>
        }
      }
      {
        if (allowEditing) {
          <div>
            <input type="text" onKeyUp={self.newProject} placeholder="new project"/>
          </div>
        } else None
      }
    </div>
  }

}
