package in.freewind.windlinks.pages.main

import com.xored.scalajs.react.util.TypedEventListeners
import com.xored.scalajs.react.{TypedReactSpec, scalax}
import in.freewind.windlinks.{Link, Project}

object Links extends TypedReactSpec with TypedEventListeners {

  case class State()

  case class Props(projects: Seq[Project])

  override def getInitialState(self: This) = State()

  @scalax
  override def render(self: This) = {
    def showLink(link: Link) = s"${link.name}: ${link.url}"
    <div>
      {self.props.projects.map(p =>
      <div>
        <div>
          {p.name}
        </div>{p.links.map(link =>
        <div>
          {showLink(link)}
        </div>)}
      </div>
    )}
    </div>
  }

}
