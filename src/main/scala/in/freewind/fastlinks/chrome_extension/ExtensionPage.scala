package in.freewind.fastlinks.chrome_extension

import com.xored.scalajs.react.util.{ClassName, TypedEventListeners}
import com.xored.scalajs.react.{TypedReactSpec, scalax}
import in.freewind.fastlinks.chrome_extension.main.{OneProject, Setup}
import in.freewind.fastlinks.{DataConverter, Link, Project}
import org.scalajs.dom.HTMLInputElement
import org.scalajs.dom.extensions.KeyCode
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

object ExtensionPage extends TypedReactSpec with TypedEventListeners {

  private val RefSearch = "search"
  private val RefHighlightItem = "search-highlight-item"
  private val HighlightClass = "highlight-search-item"

  case class State(projects: Seq[Project] = Nil,
                   keyword: Option[String] = None,
                   searchResults: Seq[Result] = Nil,
                   highlightSearchItem: Option[Result] = None,
                   dataUrl: Option[String] = None,
                   storageData: Option[ExtensionStorageData] = None)

  case class Props()

  override def getInitialState(self: This) = {
    for {
      dataOpt <- ExtensionStorageData.load()
      data <- dataOpt
    } self.setState(self.state.copy(projects = data.projects, dataUrl = data.dataUrl, storageData = Some(data)))

    State()
  }

  implicit class Closure(self: This) {

    import self._

    private def getKeyword: String = {
      refs(RefSearch).getDOMNode().asInstanceOf[HTMLInputElement].value.trim
    }

    val onSearch = input.onChange(e => {
      val keyword = Option(getKeyword).filterNot(_.isEmpty)
      val results = keyword.map(filterSearchResult(self.state.projects)).getOrElse(Nil)

      setState(state.copy(keyword = keyword,
        searchResults = results,
        highlightSearchItem = results.headOption
      ))
    })

    val onKeyUp = input.onKeyUp(e => {
      e.which match {
        case KeyCode.up => moveSelectedLink(-1)
        case KeyCode.down => moveSelectedLink(1)
        case KeyCode.enter => clickOnHighlightLink()
        case _ =>
      }
    })

    val onClearSearch = button.onClick(e => {
      val search = refs(RefSearch).getDOMNode()
      search.asInstanceOf[HTMLInputElement].value = ""
      search.focus()
      onSearch.apply(null)
    })

    def onDataFetched(url: String, json: String): Unit = {
      val storageData = new ExtensionStorageData(projects = DataConverter.parse(json), dataUrl = Some(url))
      ExtensionStorageData.save(storageData).foreach { data =>
        self.setState(state.copy(projects = data.projects, dataUrl = data.dataUrl))
      }
    }

    private def clickOnHighlightLink(): Unit = {
      refs(RefHighlightItem).getDOMNode().click()
    }

    private def moveSelectedLink(step: Int): Unit = {
      for {
        hl <- state.highlightSearchItem
        index = state.searchResults.indexOf(hl)
        total = state.searchResults.length
        newIndex = (index + step + total) % total
      } setState(state.copy(highlightSearchItem = Some(state.searchResults(newIndex))))
    }
  }

  override def componentDidMount(self: ExtensionPage.This): Unit = {
    self.refs(RefSearch).getDOMNode().focus()
  }

  @scalax
  override def render(self: This) = {
    val searchResults = self.state.searchResults
    <div id="main-page">
      <div className="search-panel">
        <input className="search" placeholder="Search" onChange={self.onSearch} onKeyUp={self.onKeyUp} ref={RefSearch}/>
        <span onClick={self.onClearSearch} className="clear-search">[X]</span>
      </div>
      {
        self.state.keyword match {
          case Some(keyword) =>
            <div className="search-results">
              {
                searchResults.map { case item =>
                  val isHighlight = Some(item) == self.state.highlightSearchItem
                  val className = ClassName("result-item" -> true, HighlightClass -> isHighlight)
                  val refHighlightLink = if (isHighlight) RefHighlightItem else ""
                  <div className={className}>
                    <span className="result-name">[{item.projectName}{item.link.name.map(":" + _).getOrElse("")}]</span>
                    <a href={item.link.url} target="_blank" ref={refHighlightLink}>
                      <span className="link-url">{item.link.url}</span>
                    </a>
                  </div>
                }
              }
            </div>
          case _ =>
            <div className="all-links">
              {
                self.state.projects.map(p =>
                  OneProject(OneProject.Props(p))
                )
              }
            </div>
        }
      }
      {Setup(Setup.Props(self.onDataFetched, self.state.dataUrl))}
    </div>
  }

  case class Result(projectName: String, link: Link)

  private def filterSearchResult(projects: Seq[Project])(keyword: String): Seq[Result] = {
    def contain(s1: String, s2: String): Boolean = {
      s1.toLowerCase.contains(s2.toLowerCase)
    }
    def matches(link: Link, keyword: String): Boolean = {
      contain(link.url, keyword) || link.name.exists(contain(_, keyword))
    }

    for {
      project <- projects
      link <- project.basicLinks ++: project.moreLinkGroups.flatMap(_.links)
      if matches(link, keyword)
    } yield Result(project.name, link)
  }

}
