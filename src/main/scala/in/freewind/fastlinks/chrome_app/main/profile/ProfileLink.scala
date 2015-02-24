package in.freewind.fastlinks.chrome_app.main.profile

import in.freewind.fastlinks.RichString
import com.xored.scalajs.react.util.TypedEventListeners
import com.xored.scalajs.react.{TypedReactSpec, scalax}
import in.freewind.fastlinks.Link
import in.freewind.fastlinks.common.Editable

object ProfileLink extends TypedReactSpec with TypedEventListeners {

  case class State(showDescription: Boolean = false)

  case class Props(allowEditing: Boolean, link: Link, updateLink: Link => Unit)

  override def getInitialState(self: This) = State()

  implicit class Closure(self: This) {

    import self._

    val link = props.link

    def updateName(newName: String): Unit = {
      props.updateLink(link.copy(name = newName.empty2option))
    }

    def updateUrl(newUrl: String): Unit = {
      props.updateLink(link.copy(url = newUrl))
    }

    def updateDesc(newDesc: String): Unit = {
      props.updateLink(link.copy(description = Option(newDesc).filterNot(_.isEmpty)))
    }

    val toggleDescription = element.onClick(e => {
      setState(state.copy(showDescription = !state.showDescription))
    })

  }

  @scalax
  override def render(self: This) = {
    val link = self.props.link
    val allowEditing = self.props.allowEditing
    <div className="link">
      <div>
        {
          link.name.map(name => <span className="link-name">{Editable.Input(allowEditing, name, self.updateName)}</span>)
        }
        <span className="link-url">{Editable.Input(allowEditing, link.url, self.updateUrl)}</span>
        {
          link.description.map(_ => <span onClick={self.toggleDescription} className="link-show-description">[+]</span>)
        }
      </div>
      {
        if (self.state.showDescription) {
          <div className="link-description">{Editable.Textarea(allowEditing, link.description.getOrElse(""), self.updateDesc)}</div>
        } else None
      }
    </div>
  }

}
