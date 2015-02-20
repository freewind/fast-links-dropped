package in.freewind.windlinks.pages

import com.xored.scalajs.react.util.{ClassName, TypedEventListeners}
import com.xored.scalajs.react.{TypedReactSpec, scalax}
import in.freewind.windlinks._
import in.freewind.windlinks.pages.main.Links
import org.scalajs.dom.HTMLInputElement
import org.scalajs.dom.extensions.Ajax
import in.freewind.windlinks.wrappers.chrome.chrome._
import scalajs.js

import scala.concurrent.ExecutionContext.Implicits.global

object MainPage extends TypedReactSpec with TypedEventListeners {

  private val RefSearch = "search"
  private val RefSearchResult = "search-result"
  private val RefHighlightItem = "search-highlight-item"
  private val HighlightClass = "highlight-search-item"

  private val StorageKeyData = "in.freewind.windlinks.storage.data"
  private val StorageKeyUrl = "in.freewind.windlinks.storage.url"

  case class State(projects: Seq[Project] = Nil,
                   keyword: Option[String] = None,
                   searchResults: Seq[Result] = Nil,
                   highlightSearchItem: Option[Result] = None)

  case class Props()

  override def getInitialState(self: This) = {
    storage.local.get(js.Array(StorageKeyData), (items: js.Dictionary[String]) => {
      items.get(StorageKeyData).map(DataConverter.parse).foreach(projects =>
        self.setState(self.state.copy(projects = projects))
      )
    })
    State()
  }

  implicit class Closure(self: This) {

    import self._

    val onSearch = input.onChange(e => {
      val keyword = Option(e.target.value).map(_.trim).filterNot(_.isEmpty)
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

    val fetchData = input.onKeyUp(e => {
      val url = refs("url").getDOMNode().asInstanceOf[HTMLInputElement].value.trim
      Ajax.get(url).onSuccess { case xhr =>
        val json = xhr.responseText
        storage.local.set(
          scalajs.js.Dynamic.literal(StorageKeyData -> json),
          () => self.setState(state.copy(projects = DataConverter.parse(json)))
        )
      }
    })

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
      <input className="search" placeholder="Search" onChange={self.onSearch} onKeyUp={self.onKeyUp} ref={RefSearch}/>
      {
        self.state.keyword match {
          case Some(keyword) =>
            <ul ref={RefSearchResult}>
              {
                searchResults.map { case item =>
                  val isHighlight = Some(item) == self.state.highlightSearchItem
                  val className = ClassName(HighlightClass -> isHighlight)
                  val refHighlightLink = if (isHighlight) RefHighlightItem else ""
                  <li className={className}>
                    <a href={item.link.url} target="_blank" ref={refHighlightLink}>[{item.projectName}] {item.link.url} - {item.link.name}</a>
                  </li>
                }
              }
            </ul>
          case _ => Links(Links.Props(projects))
        }
      }
      <div className="setup">
        <input placeholder="DataUrl" ref="url" />
        <button onClick={self.fetchData}>Fetch</button>
      </div>
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
