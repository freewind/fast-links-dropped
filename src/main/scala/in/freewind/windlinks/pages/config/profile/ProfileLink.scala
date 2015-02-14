package in.freewind.windlinks.pages.config.profile

import com.xored.scalajs.react.{TypedReactSpec, scalax}
import com.xored.scalajs.react.util.TypedEventListeners
import in.freewind.windlinks.Link
import org.scalajs.dom.HTMLInputElement

object ProfileLink extends TypedReactSpec with TypedEventListeners {

  case class State(editing: Boolean = false)

  case class Props(link: Link, updateLink: (Link, Link) => Unit)

  override def getInitialState(self: This) = State()

  implicit class Closure(self: This) {

    import self._

    val startEditing = element.onClick(e => {
      setState(state.copy(editing = true), initForm)
    })

    val update = button.onClick(e => {
      val newLink = new Link(getValue("name"), getValue("url"), Option(getValue("desc")).filterNot(_.isEmpty))
      props.updateLink(props.link, newLink)
      setState(state.copy(editing = false))
    })

    val cancel = button.onClick(e => {
      setState(state.copy(editing = false))
    })

    private def initForm(): Unit = {
      setValue("name", props.link.name)
      setValue("url", props.link.url)
      setValue("desc", props.link.description.getOrElse(""))
    }

    private def getValue(key: String) = refs(key).getDOMNode().asInstanceOf[HTMLInputElement].value.trim

    private def setValue(key: String, value: String) = refs(key).getDOMNode().asInstanceOf[HTMLInputElement].value = value
  }

  override def render(self: This) = {
    @scalax val workaround = {
      val link = self.props.link
      if (self.state.editing) {
        <div>
          <div><input ref="name" /></div>
          <div><input ref="url"/></div>
          <div><input ref="desc" /></div>
          <button onClick={self.update}>Update</button>
          <button onClick={self.cancel}>Cancel</button>
        </div>
      } else {
        <div onClick={self.startEditing}>
          <div>{link.name} - {link.url}</div>
          <div>
            {link.description.getOrElse("")}
          </div>
        </div>
      }
    }
    workaround
  }

}
