package in.freewind.fastlinks.chrome_app.main.profile

import com.xored.scalajs.react.util.TypedEventListeners
import com.xored.scalajs.react.{TypedReactSpec, scalax}
import in.freewind.fastlinks.chrome_app.AppBackend
import in.freewind.fastlinks.common.Dialog.DialogContext
import in.freewind.fastlinks.common.Editable
import in.freewind.fastlinks.{Link, LinkGroup}

object ProfileLinkGroup extends TypedReactSpec with TypedEventListeners {

  case class State()

  case class Props(allowEditing: Boolean, group: LinkGroup, updateLinkGroup: Option[LinkGroup] => Unit,
                   moveLinkUp: (Link) => Unit, moveLinkDown: (Link) => Unit, backend: AppBackend)

  override def getInitialState(self: This) = State()

  implicit class Closure(self: This) {
    import self._
    val askForDeleting = button.onClick(e => {
      props.backend.showDialog(DialogContext("Are you sure to delete this link group?", () => props.updateLinkGroup(None)))
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
          if (allowEditing) {
            <button onClick={self.askForDeleting}>Delete this link group</button>
          } else None
        }
      </span>
      {ProfileLinks(ProfileLinks.Props(allowEditing, group.links, self.updateLinksOfGroup(group), props.moveLinkUp, props.moveLinkDown, props.backend))}
    </div>
  }

}
