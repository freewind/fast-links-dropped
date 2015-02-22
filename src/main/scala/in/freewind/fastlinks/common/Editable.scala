package in.freewind.fastlinks.common

import com.xored.scalajs.react.util.TypedEventListeners
import com.xored.scalajs.react.{TypedReactSpec, scalax}
import org.scalajs.dom.HTMLInputElement

object Editable extends TypedReactSpec with TypedEventListeners {

  sealed trait InputType

  case class State(editing: Boolean = false)

  case class Props(useTextarea: Boolean = false, value: String, onOk: String => Unit)

  def Input(value: String, onOk: String => Unit) = {
    apply(Props(useTextarea = false, value, onOk))
  }
  def Textarea(value: String, onOk: String => Unit) = {
    apply(Props(useTextarea = true, value, onOk))
  }

  override def getInitialState(self: This) = State()

  val Key = "input"

  implicit class Closure(self: This) {

    import self._

    val startEditing = input.onClick(e => {
      setState(state.copy(editing = true), () => {
        setValue(Key, props.value)
        self.refs(Key).getDOMNode().focus()
      })
    })

    val update = button.onClick(e => {
      val value = getValue(Key)
      props.onOk(value)
      setState(state.copy(editing = false))
    })

    val cancel = button.onClick(e => {
      setState(state.copy(editing = false))
    })

    private def getValue(key: String): String = {
      refs(key).getDOMNode().asInstanceOf[HTMLInputElement].value.trim
    }

    private def setValue(key: String, newValue: String): Unit = {
      refs(key).getDOMNode().asInstanceOf[HTMLInputElement].value = newValue
    }

  }

  @scalax
  override def render(self: This) = {
    val originValue = self.props.value
    val useTextarea = self.props.useTextarea
    <div className="editable">
      {
        self.state.editing match {
          case true =>
            <div>
              {
                if (useTextarea) {
                  <textarea defaultValue={originValue} ref={Key} />
                } else {
                  <input defaultValue={originValue} ref={Key} />
                }
              }
              <button onClick={self.update}>Update</button>
              <button onClick={self.cancel}>Cancel</button>
            </div>
          case false =>
            <div onClick={self.startEditing}>
              {originValue}
            </div>
        }
      }
    </div>
  }


}
