package in.freewind.windlinks.pages.main

import com.xored.scalajs.react.util.TypedEventListeners
import com.xored.scalajs.react.{TypedReactSpec, scalax}
import in.freewind.windlinks.{Link, Project}

object Links extends TypedReactSpec with TypedEventListeners {

  case class State()

  case class Props(projects: Seq[Project])

  override def getInitialState(self: This) = State()


  override def render(self: This) = {
    @scalax val workaround = {
      def showLinks(links: Seq[Link]) = links.map(link => <div>{link.name}: {link.url}</div>)
      def showProject(p: Project) = {
        <div>
          <div>{p.name}</div>
          <div>{showLinks(p.basicLinks)}</div>
        </div>
      }
      val projects = self.props.projects.map(showProject)
      <div>{projects}</div>
    }
    workaround
  }

}
