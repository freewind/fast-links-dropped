package in.freewind.windlinks.pages.main

import com.xored.scalajs.react.util.TypedEventListeners
import com.xored.scalajs.react.{TypedReactSpec, scalax}
import in.freewind.windlinks.Project

object Links extends TypedReactSpec with TypedEventListeners {

  case class State()

  case class Props(projects: Seq[Project])

  override def getInitialState(self: This) = State()

  @scalax
  override def render(self: This) = {
    <div>
      {
        self.props.projects.map(p =>
          <div>
            <div>{p.name}</div>
            <div>
              {
                p.basicLinks.map(link => <div>{link.name}: {link.url}</div>)
              }
            </div>
          </div>
        )
      }
    </div>
  }

}
