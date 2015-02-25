package in.freewind.fastlinks.chrome_app.main.profile

import com.xored.scalajs.react.util.TypedEventListeners
import com.xored.scalajs.react.{TypedReactSpec, scalax}
import in.freewind.fastlinks.common.Editable
import in.freewind.fastlinks.{Link, LinkGroup}

object ProfileLinkGroup extends TypedReactSpec with TypedEventListeners {

  case class State(deleting: Boolean = false)

  case class Props(allowEditing: Boolean, group: LinkGroup, updateLinkGroup: Option[LinkGroup] => Unit)

  override def getInitialState(self: This) = State()

  implicit class Closure(self: This) {
    import self._
    val askForDeleting = button.onClick(e => {
      setState(state.copy(deleting = true))
    })

    val cancelDeleting = button.onClick(e => {
      setState(state.copy(deleting = false))
    })

    val deleteLinkGroup = button.onClick(e => {
      props.updateLinkGroup(None)
    })

    def updateGroupName(group: LinkGroup)(newName: String): Unit = {
      val newGroup = group.copy(name = newName)
      props.updateLinkGroup(Some(newGroup))
    }

    def updateLinksOfGroup(linkGroup: LinkGroup)(newLinks: Seq[Link]): Unit = {
      val newGroup = linkGroup.copy(links = newLinks)
      props.updateLinkGroup(Some(newGroup))
    }

  }

  @scalax
  override def render(self: This) = {
    import self._
    val allowEditing = self.props.allowEditing
    val group = self.props.group
    <div className="project-group">
      <span>
        {Editable.Input(allowEditing, Some(group.name), self.updateGroupName(group), Some("group-name"))}
        {
          if (state.deleting) {
            <div>
              <div>Are you sure delete this link group?</div>
              <button onClick={self.deleteLinkGroup}>Ok</button>
              <button onClick={self.cancelDeleting}>Cancel</button>
            </div>
          } else {
            <button onClick={self.askForDeleting}>Delete this link group</button>
          }
        }
      </span>
      {ProfileLinks(ProfileLinks.Props(allowEditing, group.links, self.updateLinksOfGroup(group)))}
    </div>
  }

}
