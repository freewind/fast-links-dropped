package in.freewind.windlinks.pages.config

import com.xored.scalajs.react.{scalax, TypedReactSpec}
import com.xored.scalajs.react.util.TypedEventListeners
import org.scalajs.dom.HTMLInputElement

object ProjectDescription extends TypedReactSpec with TypedEventListeners {

  case class State(editing: Boolean = false)

  case class Props(desc: Option[String], updateDesc: String => Unit)

  override def getInitialState(self: This) = State()

  implicit class Closure(self: This) {

    val startEditing = element.onClick(e => {
      self.setState(self.state.copy(editing = true))
    })

    val updateDesc = button.onClick(e => {
      val value = self.refs("textarea").getDOMNode().asInstanceOf[HTMLInputElement].value.trim
      self.props.updateDesc(value)
      self.setState(self.state.copy(editing = false))
    })
  }


  override def render(self: This) = {
    // issue: https://github.com/xored/scala-js-react/issues/15
    @scalax
    val workaround = {
      val desc = self.props.desc

      <div className="project-desc">
        {self.state.editing match {
        case true => <div>
          <textarea ref="textarea" defaultValue={desc.getOrElse("")}/>
          <button onClick={self.updateDesc}>Update</button>
        </div>
        case false => <div onClick={self.startEditing}>
          {desc.getOrElse("click to add description ...")}
        </div>
      }}
      </div>
    }

    workaround
  }


}
