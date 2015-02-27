package in.freewind.fastlinks.chrome_extension.main

import com.xored.scalajs.react.util.TypedEventListeners
import com.xored.scalajs.react.{TypedReactSpec, scalax}
import in.freewind.fastlinks.common.Stars
import in.freewind.fastlinks.{Link, Project}

object OneProject extends TypedReactSpec with TypedEventListeners {

  case class State(showDescription: Boolean = false)

  case class Props(project: Project)

  override def getInitialState(self: This) = State()

  implicit class Closure(self: This) {

    import self._

    val showDescription = element.onClick(e => {
      setState(state.copy(showDescription = !state.showDescription))
    })
  }

  @scalax
  override def render(self: This) = {
    val p = self.props.project

    def showLinks(links: Seq[Link]) = {
      <div className="links">{ links.map(link => OneLink(OneLink.Props(link))) }</div>
    }

    <div className="project">
      <div className="project-name">
        <span>{p.name}</span>
        { Stars(Stars.Props(allowEditing = false, count = p.stars)) }
        {
          p.description.map(_ =>
            <span onClick={self.showDescription} className="show-description">[+]</span>
          )
        }
      </div>
      {
        Some(self.state.showDescription).filter(_ == true).map(_ =>
          p.description.map(_ => <div className="project-description">{p.description}</div>)
        )
      }
      {
        p.linkGroups.map(group =>
          <div className="group-links">
            <div className="group-name">{group.name}</div>
            { showLinks(group.links) }
          </div>
        )
      }
    </div>
  }

}
