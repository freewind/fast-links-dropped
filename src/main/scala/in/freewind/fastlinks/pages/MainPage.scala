package in.freewind.fastlinks.pages

import com.xored.scalajs.react.util.{ClassName, TypedEventListeners}
import com.xored.scalajs.react.{TypedReactSpec, scalax}
import in.freewind.fastlinks._
import in.freewind.fastlinks.pages.main.{Links, Setup}
import in.freewind.fastlinks.wrappers.chrome.chrome._
import org.scalajs.dom.HTMLInputElement

import scala.scalajs.js

object MainPage extends TypedReactSpec with TypedEventListeners {

  private val RefSearch = "search"
  private val RefHighlightItem = "search-highlight-item"
  private val HighlightClass = "highlight-search-item"

  val StorageKeyData = "in.freewind.fastlinks.storage.data"
  val StorageKeyUrl = "in.freewind.fastlinks.storage.url"

  case class State(projects: Seq[Project] = Nil,
                   keyword: Option[String] = None,
                   searchResults: Seq[Result] = Nil,
                   highlightSearchItem: Option[Result] = None,
                   dataUrl: Option[String] = None)

  case class Props()

  override def getInitialState(self: This) = {
    storage.local.get(js.Array(StorageKeyData, StorageKeyUrl), (items: js.Dictionary[String]) => {
      val projects = items.get(StorageKeyData).map(DataConverter.parse).getOrElse(Nil)
      val dataUrl = items.get(StorageKeyUrl)
      self.setState(self.state.copy(projects = projects, dataUrl = dataUrl))
    })
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
        case Keycode.Up => moveSelectedLink(-1)
        case Keycode.Down => moveSelectedLink(1)
        case Keycode.Enter => clickOnHighlightLink()
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
      storage.local.set(
        scalajs.js.Dynamic.literal(StorageKeyUrl -> url, StorageKeyData -> json),
        () => self.setState(state.copy(projects = DataConverter.parse(json), dataUrl = Some(url)))
      )
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

  override def componentDidMount(self: MainPage.This): Unit = {
    self.refs(RefSearch).getDOMNode().focus()
  }

  @scalax
  override def render(self: This) = {
    val searchResults = self.state.searchResults
    val projects = self.state.projects
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
          case _ => Links(Links.Props(projects))
        }
      }
      {Setup(Setup.Props(self.onDataFetched, self.state.dataUrl))}
    </div>
  }

  case class Result(projectName: String, link: Link)

  private def filterSearchResult(projects: Seq[Project])(keyword: String): Seq[Result] = {
    def matches(url: String, keyword: String): Boolean = {
      url.toLowerCase.contains(keyword.toLowerCase)
    }

    for {
      project <- projects
      link <- project.basicLinks ++: project.moreLinkGroups.flatMap(_.links)
      if matches(link.url, keyword)
    } yield Result(project.name, link)
  }

}
