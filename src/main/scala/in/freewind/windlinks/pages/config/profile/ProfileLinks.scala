package in.freewind.windlinks.pages.config.profile

import com.xored.scalajs.react.util.TypedEventListeners
import com.xored.scalajs.react.{TypedReactSpec, scalax}
import in.freewind.windlinks.Link

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

  override def render(self: This) = {
    @scalax val workaround = {

      val links = self.props.links.map(link => ProfileLink(ProfileLink.Props(link, self.updateLink(link))))

      val newLink = NewLink(NewLink.Props(self.newLink))

      <div className="group-links">
        {links}
        {newLink}
      </div>
    }
    workaround
  }


}

