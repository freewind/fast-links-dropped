package in.freewind.windlinks.main

import com.xored.scalajs.react.util.TypedEventListeners
import com.xored.scalajs.react.{TypedReactSpec, scalax}
import in.freewind.windlinks.{Link, Project}

object Main extends TypedReactSpec with TypedEventListeners {

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
    val content = Seq(
      Search(Search.Props(self.onSearch)),
      self.state.keyword match {
        case Some(keyword) => SearchResult(SearchResult.Props(filteredLinks(projects, keyword)))
        case _ => Links(Links.Props(projects))
      })

    <div>
      {content}
    </div>
  }

  def filteredLinks(projects: Seq[Project], keyword: String): Seq[Project] = {
    for {
      project <- projects
      links = project.links.filter(_.url.contains(keyword))
    } yield Project(project.title, links)
  }

  private val projects = Seq(
    Project("scala", Seq(
      Link(name = "official", url = "http://www.scala-lang.org/", description = Some("Official website")),
      Link(name = "typesafe", url = "https://typesafe.com", description = Some("The company supports Scala")))),
    Project("scalajs", Seq(
      Link("official", url = "http://www.scala-js.org/"))),
    Project("react", Seq(
      Link("official", url = "http://facebook.github.io/react/")))
  )
}
