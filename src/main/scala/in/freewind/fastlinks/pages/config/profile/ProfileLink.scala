package in.freewind.fastlinks.pages.config.profile

import in.freewind.fastlinks.RichString
import com.xored.scalajs.react.util.TypedEventListeners
import com.xored.scalajs.react.{TypedReactSpec, scalax}
import in.freewind.fastlinks.Link
import in.freewind.fastlinks.pages.common.Editable

object ProfileLink extends TypedReactSpec with TypedEventListeners {

  case class State()

  case class Props(link: Link, updateLink: Link => Unit)

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

  }

  @scalax
  override def render(self: This) = {
    val link = self.props.link
    <div className="link">
      <div>{Editable.Input(link.name.getOrElse(""), self.updateName)}</div>
      <div>{Editable.Input(link.url, self.updateUrl)}</div>
      <div>{Editable.Textarea(link.description.getOrElse(""), self.updateDesc)}</div>
    </div>
  }

}
