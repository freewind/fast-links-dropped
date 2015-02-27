package in.freewind.fastlinks.common

import com.xored.scalajs.react.export._
import com.xored.scalajs.react.util.{ClassName, TypedEventListeners}
import com.xored.scalajs.react.{export, ReactDOM, TypedReactSpec, scalax}
import in.freewind.fastlinks.common.SearchResults.{LinkResult, CategoryResult}
import in.freewind.fastlinks.{Category, Link, Project}
import org.scalajs.dom.HTMLInputElement
import org.scalajs.dom.extensions.KeyCode

object Search extends TypedReactSpec with TypedEventListeners {

  private val RefSearch = "search"
  private val RefHighlightItem = "search-highlight-item"
  private val HighlightClass = "highlight-search-item"

  case class State(keyword: Option[String] = None,
                   searchResults: Seq[CategoryResult] = Nil,
                   highlightSearchItem: Option[LinkResult] = None)

  case class Props(categories: Seq[Category], nonSearch: Option[ReactDOM] = None)

  override def getInitialState(self: This) = State()

  override def exports(): List[Export] = export.export1("focus", focus) :: super.exports()

  def focus(self: This): Unit = {
    self.refs(RefSearch).getDOMNode().focus()
  }

  implicit class Closure(self: This) {

    import self._

    private def getKeyword: String = {
      refs(RefSearch).getDOMNode().asInstanceOf[HTMLInputElement].value.trim
    }

    val onSearch = input.onChange(e => {
      val keyword = Option(getKeyword).filterNot(_.isEmpty)
      val results = keyword.map(filterSearchResult(self.props.categories)).getOrElse(Nil)

      setState(state.copy(keyword = keyword,
        searchResults = results,
        highlightSearchItem = results.headOption.flatMap(_.links.headOption)
      ))
    })

    val onClearSearch = button.onClick(_ => {
      val search = refs(RefSearch).getDOMNode()
      search.asInstanceOf[HTMLInputElement].value = ""
      search.focus()
      onSearch.apply(null)
    })

    val onKeyUp = input.onKeyUp(e => {
      e.which match {
        case KeyCode.up => moveSelectedLink(-1)
        case KeyCode.down => moveSelectedLink(1)
        case KeyCode.enter => clickOnHighlightLink()
        case KeyCode.escape => onClearSearch.apply(null)
        case _ =>
      }
    })

    private def clickOnHighlightLink(): Unit = {
      refs(RefHighlightItem).getDOMNode().click()
    }

    private def moveSelectedLink(step: Int): Unit = {
      val allLinks = state.searchResults.flatMap(_.links)

      for {
        hl <- state.highlightSearchItem
        index = allLinks.indexOf(hl)
        total = allLinks.length
        newIndex = (index + step + total) % total
      } setState(state.copy(highlightSearchItem = Some(allLinks(newIndex))))
    }
  }

  override def componentDidMount(self: This): Unit = {
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
                searchResults.map { category =>
                  <div className="category-result">
                    <div className="category-name">{category.name}</div>
                    {
                      category.links.map { case item =>
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
                }
              }
            </div>
          case _ => self.props.nonSearch
        }
      }
    </div>
  }

  private def filterSearchResult(categories: Seq[Category])(keyword: String): Seq[CategoryResult] = {
    def contain(s1: String, s2: String): Boolean = {
      s1.toLowerCase.contains(s2.toLowerCase)
    }
    def matches(project: Project, link: Link, keyword: String): Int = {
      if (contain(link.url, keyword)) 3
      else if (link.name.exists(contain(_, keyword))) 2
      else if (contain(project.name, keyword)) 1
      else 0
    }

    categories.flatMap { category =>
      val links = for {
        project <- category.projects
        link <- project.linkGroups.flatMap(_.links)
        weight = matches(project, link, keyword)
        if weight > 0
      } yield LinkResult(project.name, link, weight)

      links.sortBy(l => -l.weight) match {
        case Nil => None
        case ll => Some(CategoryResult(category.name, ll))
      }
    }
  }

}

object SearchResults {
  case class CategoryResult(name: String, links: Seq[LinkResult])
  case class LinkResult(projectName: String, link: Link, weight: Int)
}

