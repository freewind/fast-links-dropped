package in.freewind.windlinks.pages.config.profile

import com.xored.scalajs.react.{scalax, TypedReactSpec}
import com.xored.scalajs.react.util.TypedEventListeners
import in.freewind.windlinks.Link
import in.freewind.windlinks.RichString
import org.scalajs.dom.HTMLInputElement

object NewLink extends TypedReactSpec with TypedEventListeners {

  case class State(adding: Boolean = false)

  case class Props(newLink: Link => Unit)

  override def getInitialState(self: This) = State()

  implicit class Closure(self: This) {

    import self._

    val startAdding = button.onClick(e => {
      setState(state.copy(adding = true), () => {
        Seq("name", "url", "desc").foreach(setValue(_, ""))
      })
    })

    val add = button.onClick(e => {
      val newLink = new Link(getValue("name").empty2option, getValue("url"), Option(getValue("desc")).filterNot(_.isEmpty))
      self.props.newLink(newLink)
    })

    val cancel = button.onClick(e => {
      setState(state.copy(adding = false))
    })

    private def getValue(key: String) = refs(key).getDOMNode().asInstanceOf[HTMLInputElement].value.trim

    private def setValue(key: String, value: String) = refs(key).getDOMNode().asInstanceOf[HTMLInputElement].value = value

  }

  @scalax
  override def render(self: This) = {
    if (self.state.adding) {
      <div>
        <div>
          <div><input ref="name" /></div>
          <div><input ref="url"/></div>
          <div><textarea ref="desc" /></div>
          <button onClick={self.add}>Ok</button>
          <button onClick={self.cancel}>Cancel</button>
        </div>
      </div>
    } else {
      <div>
      <button onClick={self.startAdding}>+</button>
      </div>
    }
  }

}
