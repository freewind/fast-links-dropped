package in.freewind.fastlinks.pages.config.profile

import com.xored.scalajs.react.util.TypedEventListeners
import com.xored.scalajs.react.{TypedReactSpec, scalax}
import in.freewind.fastlinks.Link

object ProfileLinks extends TypedReactSpec with TypedEventListeners {

  case class State(editing: Boolean = false)

  case class Props(links: Seq[Link], updateLinks: Seq[Link] => Unit)

  override def getInitialState(self: This) = State()

  implicit class Closure(self: This) {

    import self._

    val startEditing = element.onClick(e => {
      setState(state.copy(editing = true))
    })

    def newLink(link: Link): Unit = {
      props.updateLinks(props.links :+ link)
    }

    def updateLink(oldLink: Link)(newLink: Link): Unit = {
      props.updateLinks(props.links.replace(oldLink, newLink))
    }
  }

  @scalax
  override def render(self: This) = {
    <div className="group-links">
      {
        self.props.links.map(link =>
          ProfileLink(ProfileLink.Props(link, self.updateLink(link)))
        )
      }
      {
        NewLink(NewLink.Props(self.newLink))
      }
    </div>
  }


}

