package in.freewind.windlinks.pages.main

import com.xored.scalajs.react.util.TypedEventListeners
import com.xored.scalajs.react.{TypedReactSpec, scalax}
import in.freewind.windlinks.{Link, Project}

object SearchResult extends TypedReactSpec with TypedEventListeners {

  case class State()

  case class Props(filtered: Seq[Project])

  override def getInitialState(self: This) = State()

  override def render(self: This) = {
    @scalax val workaround = {
      val results = self.props.filtered.collect({
        case Project(name, basicLinks, moreGroups, _) =>
          val allLinks = basicLinks ++: moreGroups.flatMap(_.links)
          allLinks.map(Result(name, _))
      }).flatten

      val links = results.map(r => <li> [{r.projectName}] {r.link.url} - {r.link.name} </li>)

      <ul>
        {links}
      </ul>
    }
    workaround
  }

  case class Result(projectName: String, link: Link)

}
