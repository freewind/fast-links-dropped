package in.freewind.windlinks.main

import com.xored.scalajs.react.util.TypedEventListeners
import com.xored.scalajs.react.{TypedReactSpec, scalax}
import in.freewind.windlinks.{Link, Project}

object SearchResult extends TypedReactSpec with TypedEventListeners {

  case class State()

  case class Props(filtered: Seq[Project])

  override def getInitialState(self: This) = State()

  @scalax
  override def render(self: This) = {
    val results = self.props.filtered.collect({
      case Project(title, links) => links.map(Result(title, _))
    }).flatten

    <ul>
      {results.map(r => <li>[{r.projectTitle}] {r.link.url} - {r.link.name}</li>)}
    </ul>
  }

  case class Result(projectTitle: String, link: Link)

}
