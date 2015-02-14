package in.freewind.windlinks.pages.config

import com.xored.scalajs.react.util.TypedEventListeners
import com.xored.scalajs.react.{TypedReactSpec, scalax}
import org.scalajs.dom.HTMLInputElement

object ProjectName extends TypedReactSpec with TypedEventListeners {

  case class State(editing: Boolean = false)

  case class Props(name: String, updateProjectName: (String, String) => Unit)

  override def getInitialState(self: This) = State()

  implicit class Closure(self: This) {

    import self._

    val startEditing = input.onChange(e => {
      setState(state.copy(editing = true))
    })

    val updateName = button.onClick(e => {
      val value = refs("input").getDOMNode().asInstanceOf[HTMLInputElement].value.trim
      props.updateProjectName(props.name, value)
      setState(state.copy(editing = false))
    })
  }

  @scalax
  override def render(self: This) = {
    val name = self.props.name

    <div className="project-name">
      {self.state.editing match {
      case true => <div>
        <input defaultValue={name} ref="input"/>
        <button onClick={self.updateName}>Update</button>
      </div>
      case false => <div onClick={self.startEditing}>
        {name}
      </div>
    }}
    </div>
  }

}
