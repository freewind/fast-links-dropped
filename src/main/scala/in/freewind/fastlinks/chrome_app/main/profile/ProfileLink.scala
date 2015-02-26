package in.freewind.fastlinks.chrome_app.main.profile

import com.xored.scalajs.react.util.TypedEventListeners
import com.xored.scalajs.react.{TypedReactSpec, scalax}
import in.freewind.fastlinks.Link
import in.freewind.fastlinks.chrome_app.AppBackend
import in.freewind.fastlinks.common.Dialog

object ProfileLink extends TypedReactSpec with TypedEventListeners {

  case class State(editing: Boolean = false, showDescription: Boolean = false)

  case class Props(allowEditing: Boolean, link: Link, updateLink: Link => Unit, deleteLink: () => Unit, backend: AppBackend)

  override def getInitialState(self: This) = State()

  implicit class Closure(self: This) {

    import self._

    val toggleDescription = element.onClick(e => {
      setState(state.copy(showDescription = !state.showDescription))
    })

    val startEditing = element.onClick(e => {
      e.preventDefault()
      setState(state.copy(editing = true))
    })

    val doNothing = element.onClick(e => ())

    def updateLink(link: Link) = {
      setState(state.copy(editing = false))
      props.updateLink(link)
    }

    def cancel(): Unit = {
      setState(state.copy(editing = false))
    }

    val askForDeleting = button.onClick(e => {
      props.backend.showDialog(Dialog.DialogContext("Are you sure delete this link?", props.deleteLink))
    })

  }

  @scalax
  override def render(self: This) = {
    import self._
    val link = self.props.link
    val allowEditing = self.props.allowEditing
    val clickOp = if (allowEditing) self.startEditing else self.doNothing
    <div className="link">
      {
        if (state.editing) {
          LinkForm.Edit(link, self.updateLink, self.cancel)
        } else {
          <div>
            <span onClick={clickOp}>
              {
                if (link.showUrl) {
                  <span>
                    { link.name.map(name => <span className="link-name">{name}</span>) }
                    <span><a href={link.url} target="_blank">{link.url}</a></span>
                  </span>
                } else {
                  <a href={link.url} target="_blank">{link.name}</a>
                }
              }
              {
                link.description.map(_ => <span onClick={self.toggleDescription} className="link-show-description">[+]</span>)
              }
            </span>
            {
              if (allowEditing) {
                <span>
                  <button onClick={self.askForDeleting}>delete link</button>
                </span>
              } else None
            }

          </div>
        }
      }
      {
        if (self.state.showDescription) {
          <div className="link-description">{link.description}</div>
        } else None
      }
    </div>
  }

}
