package in.freewind.fastlinks.chrome_extension

import com.xored.scalajs.react.util.{ClassName, TypedEventListeners}
import com.xored.scalajs.react.{TypedReactSpec, scalax}
import in.freewind.fastlinks.chrome_extension.main.OneProject
import in.freewind.fastlinks.{Link, Project}
import org.scalajs.dom.HTMLInputElement
import org.scalajs.dom.extensions.KeyCode

object FastLinks extends TypedReactSpec with TypedEventListeners {

  private val RefSearch = "search"
  private val RefHighlightItem = "search-highlight-item"
  private val HighlightClass = "highlight-search-item"

  case class State(keyword: Option[String] = None,
                   searchResults: Seq[Result] = Nil,
                   highlightSearchItem: Option[Result] = None)

  case class Props(projects: Seq[Project])

  override def getInitialState(self: This) = State()

  implicit class Closure(self: This) {

    import self._

    private def getKeyword: String = {
      refs(RefSearch).getDOMNode().asInstanceOf[HTMLInputElement].value.trim
    }

    val onSearch = input.onChange(e => {
      val keyword = Option(getKeyword).filterNot(_.isEmpty)
      val results = keyword.map(filterSearchResult(self.props.projects)).getOrElse(Nil)

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

  override def componentDidMount(self: FastLinks.This): Unit = {
    self.refs(RefSearch).getDOMNode().focus()
  }

  @scalax
  override def render(self: This) = {
    val searchResults = self.state.searchResults
    <div>
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
                self.props.projects.map(p =>
                  OneProject(OneProject.Props(p))
                )
              }
            </div>
        }
      }
    </div>
  }

  case class Result(projectName: String, link: Link, weight: Int)

  private def filterSearchResult(projects: Seq[Project])(keyword: String): Seq[Result] = {
    def contain(s1: String, s2: String): Boolean = {
      s1.toLowerCase.contains(s2.toLowerCase)
    }
    def matches(project: Project, link: Link, keyword: String): Int = {
      if (contain(link.url, keyword)) 3
      else if (link.name.exists(contain(_, keyword))) 2
      else if (contain(project.name, keyword)) 1
      else 0
    }

    val results = for {
      project <- projects
      link <- project.basicLinks ++: project.moreLinkGroups.flatMap(_.links)
    } yield Result(project.name, link, matches(project, link, keyword))

    results.filter(_.weight > 0).sortBy(r => -r.weight)
  }

}
