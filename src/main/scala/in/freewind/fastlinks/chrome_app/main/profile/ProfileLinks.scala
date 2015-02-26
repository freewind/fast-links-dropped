package in.freewind.fastlinks.chrome_app.main.profile

import com.xored.scalajs.react.util.TypedEventListeners
import com.xored.scalajs.react.{TypedReactSpec, scalax}
import in.freewind.fastlinks.Link
import in.freewind.fastlinks.chrome_app.AppBackend

object ProfileLinks extends TypedReactSpec with TypedEventListeners {

  case class State(editing: Boolean = false)

  case class Props(allowEditing: Boolean, links: Seq[Link], updateLinks: Seq[Link] => Unit, backend: AppBackend)

  override def getInitialState(self: This) = State()

  implicit class Closure(self: This) {

    import self._

    val startAdding = element.onClick(e => {
      setState(state.copy(editing = true))
    })

    def newLink(link: Link): Unit = {
      props.updateLinks(props.links :+ link)
      cancel()
    }

    def updateLink(oldLink: Link)(newLink: Link): Unit = {
      props.updateLinks(props.links.replace(oldLink, newLink))
    }

    def deleteLink(link: Link) = () => {
      props.updateLinks(props.links.filterNot(_ == link))
    }

    def cancel(): Unit = {
      setState(state.copy(editing = false))
    }

  }

  @scalax
  override def render(self: This) = {
    import self._
    <div className="group-links">
      {
        props.links.map(link =>
          ProfileLink(ProfileLink.Props(props.allowEditing, link, self.updateLink(link), self.deleteLink(link), props.backend))
        )
      }
      {
        if (props.allowEditing) {
          if (self.state.editing) {
            LinkForm.New(self.newLink, self.cancel)
          } else {
            <div>
              <button onClick={self.startAdding}>+ new link</button>
            </div>
          }
        } else None
      }
    </div>
  }


}

