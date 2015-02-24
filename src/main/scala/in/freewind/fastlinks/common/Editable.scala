package in.freewind.fastlinks.common

import com.xored.scalajs.react.util.{ClassName, TypedEventListeners}
import com.xored.scalajs.react.{TypedReactSpec, scalax}
import org.scalajs.dom.HTMLInputElement

object Editable extends TypedReactSpec with TypedEventListeners {

  private val Key = "input"

  sealed trait InputType

  case class State(editing: Boolean = false)

  case class Props(useTextarea: Boolean,
                   allowEditing: Boolean,
                   value: String,
                   onOk: String => Unit,
                   className: Option[String])

  def Input(allowEditing: Boolean, value: String, onOk: String => Unit, className: Option[String] = None) = {
    apply(Props(useTextarea = false, allowEditing, value, onOk, className))
  }

  def Textarea(allowEditing: Boolean, value: String, onOk: String => Unit, className: Option[String] = None) = {
    apply(Props(useTextarea = true, allowEditing, value, onOk, className))
  }

  override def getInitialState(self: This) = State()

  override def componentWillReceiveProps(self: This, nextProps: Props): Unit = {
    import self._
    if (!nextProps.allowEditing) {
      setState(state.copy(editing = false))
    }
  }

  implicit class Closure(self: This) {

    import self._

    val startEditing = element.onClick(e => {
      setState(state.copy(editing = true), () => {
        setValue(Key, props.value)
        self.refs(Key).getDOMNode().focus()
      })
    })

    val doNothing = element.onClick(e => ())

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
    val className = ClassName(
      "editable" -> true,
      self.props.className.getOrElse("") -> true
    )
    <span className={className}>
      {
        self.state.editing match {
          case true =>
            <span className="editing">
              {
                if (useTextarea) {
                  <textarea defaultValue={originValue} ref={Key} />
                } else {
                  <input defaultValue={originValue} ref={Key} />
                }
              }
              <button onClick={self.update}>Update</button>
              <button onClick={self.cancel}>Cancel</button>
            </span>
          case false =>
            val op = if (self.props.allowEditing) self.startEditing else self.doNothing
            <span onClick={op} className="normal">
              {originValue}
            </span>
        }
      }
    </span>
  }


}
