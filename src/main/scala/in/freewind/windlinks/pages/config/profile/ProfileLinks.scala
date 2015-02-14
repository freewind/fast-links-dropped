package in.freewind.windlinks.pages.config.profile

import com.xored.scalajs.react.{scalax, TypedReactSpec}
import com.xored.scalajs.react.util.TypedEventListeners
import in.freewind.windlinks.Link

object ProfileLinks extends TypedReactSpec with TypedEventListeners {

  case class State(editing: Boolean = false)

  case class Props(groupName: Option[String], links: Seq[Link], updateLink: (Link, Link) => Unit)

  override def getInitialState(self: This) = State()

  implicit class Closure(self: This) {

    import self._

    val onChange = input.onChange(e => {
      println(e.value)
    })

    val startEditing = element.onClick(e => {
      setState(state.copy(editing = true))
    })

  }

  override def render(self: This) = {
    @scalax val workaround = {
      val groupName = self.props.groupName match {
        case Some(name) => <div>{name}</div>
        case _ => <div></div>
      }

      val links = self.props.links.map(link => ProfileLink(ProfileLink.Props(link, self.props.updateLink)))

      <div className="project-links">
        {groupName}
        {links}
      </div>
    }
    workaround
  }

}

