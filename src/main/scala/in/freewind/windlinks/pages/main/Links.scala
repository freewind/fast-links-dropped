package in.freewind.windlinks.pages.main

import com.xored.scalajs.react.util.TypedEventListeners
import com.xored.scalajs.react.{TypedReactSpec, scalax}
import in.freewind.windlinks.{Link, Project}

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
              <span className="link-url">{link.url}</span>
            </div>
          )
        }
      </div>
    }

    <div>
      {
        self.props.projects.map(p =>
          <div>
            <div className="project-name">
              <span>{p.name}</span>
              <span>{p.stars.map(n => 1 to n map "*").getOrElse("")}</span>
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
