package in.freewind.fastlinks.pages.main

import com.xored.scalajs.react.util.TypedEventListeners
import com.xored.scalajs.react.{TypedReactSpec, scalax}
import in.freewind.fastlinks.{Link, Project}

object Links extends TypedReactSpec with TypedEventListeners {

  case class State()

  case class Props(projects: Seq[Project])

  override def getInitialState(self: This) = State()

  @scalax
  override def render(self: This) = {
    def showLinks(links: Seq[Link]) = {
      <div className="links">
        {
          links.map(link =>
            <div>
              {
                link.name.map(linkName => <span className="link-name">[{linkName}]</span>)
              }
              <a className="link-url" href={link.url} target="_blank">{link.url}</a>
            </div>
          )
        }
      </div>
    }

    def showStars(stars: Option[Int]) = {
      stars match {
        case Some(n) => (1 to n).toList.map(_ => "*")
        case _ => <span></span>
      }
    }

    <div className="all-links">
      {
        self.props.projects.map(p =>
          <div>
            <div className="project-name">
              <span>{p.name}</span>
              <span>{showStars(p.stars)}</span>
            </div>
            <div className="basic-links">
              { showLinks(p.basicLinks) }
            </div>
            {
              p.moreLinkGroups.map(group =>
                <div className="group-links">
                  <div className="group-name">{group.name}</div>
                  { showLinks(group.links) }
                </div>
              )
            }
          </div>
        )
      }
    </div>
  }

}
