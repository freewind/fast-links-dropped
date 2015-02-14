package in.freewind.windlinks.pages.main

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
      case Project(name, links, _) => links.map(Result(name, _))
    }).flatten

    def showResult(r: Result) = s"[${r.projectName}] ${r.link.url} - ${r.link.name}"

    <ul>
      {results.map(r =>
      <li>
        {showResult(r)}
      </li>)}
    </ul>
  }

  case class Result(projectName: String, link: Link)

}
