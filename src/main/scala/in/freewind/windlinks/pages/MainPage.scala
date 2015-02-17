package in.freewind.windlinks.pages

import com.xored.scalajs.react.util.TypedEventListeners
import com.xored.scalajs.react.{TypedReactSpec, scalax}
import in.freewind.windlinks.pages.main.{Links, Search, SearchResult}
import in.freewind.windlinks.{Project, SampleData}

object MainPage extends TypedReactSpec with TypedEventListeners {

  case class State(keyword: Option[String])

  case class Props()

  override def getInitialState(self: This) = State(None)

  implicit class Closure(self: This) {

    import self._

    def onSearch(keyword: String): Unit = {
      val newKeyword = Option(keyword).map(_.trim)
      setState(state.copy(newKeyword.filterNot(_.isEmpty)))
    }
  }

  @scalax
  override def render(self: This) = {
    <div>
      {Search(Search.Props(self.onSearch))}
      {
        self.state.keyword match {
          case Some(keyword) => SearchResult(SearchResult.Props(filteredLinks(projects, keyword)))
          case _ => Links(Links.Props(projects))
        }
      }
    </div>
  }

  def filteredLinks(projects: Seq[Project], keyword: String): Seq[Project] = {
    for {
      project <- projects
      links = project.basicLinks.filter(_.url.contains(keyword))
    } yield Project(project.name, links)
  }

  private val projects = SampleData.projects

}
