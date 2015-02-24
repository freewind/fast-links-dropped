package in.freewind.fastlinks.common

import com.xored.scalajs.react.util.{ClassName, TypedEventListeners}
import com.xored.scalajs.react.{ReactDOM, TypedReactSpec, scalax}
import org.scalajs.dom.HTMLInputElement

object Editable extends TypedReactSpec with TypedEventListeners {

  sealed trait InputType

  case class State(editing: Boolean = false)

  case class Props(useTextarea: Boolean,
                   allowEditing: Boolean,
                   value: String,
                   onOk: String => Unit,
                   className: Option[String],
                   normalPlaceholder: Option[ReactDOM],
                   editingPlaceholder: Option[ReactDOM])

  def Input(allowEditing: Boolean, value: String, onOk: String => Unit, className: Option[String] = None,
            normalPlaceholder: Option[ReactDOM] = None, editingPlaceholder: Option[ReactDOM] = None) = {
    apply(Props(useTextarea = false, allowEditing, value, onOk, className, normalPlaceholder, editingPlaceholder))
  }

  def Textarea(allowEditing: Boolean, value: String, onOk: String => Unit, className: Option[String] = None,
               normalPlaceholder: Option[ReactDOM] = None, editingPlaceholder: Option[ReactDOM] = None) = {
    apply(Props(useTextarea = true, allowEditing, value, onOk, className, normalPlaceholder, editingPlaceholder))
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
      e.preventDefault()
      setState(state.copy(editing = true), () => {
        val node = getEditingNode
        node.value = props.value
        node.focus()
      })
    })

    val doNothing = element.onClick(e => ())

    val update = button.onClick(e => {
      val value = getEditingNode.value.trim
      props.onOk(value)
      setState(state.copy(editing = false))
    })

    val cancel = button.onClick(e => {
      setState(state.copy(editing = false))
    })

    // FIXME find a better way
    private def getEditingNode: HTMLInputElement = {
      refs("editing").getDOMNode().firstChild.asInstanceOf[HTMLInputElement]
    }

  }

  @scalax
  override def render(self: This) = {
    import self._
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
            <span className="editing" ref="editing">
              {
                self.props.editingPlaceholder.getOrElse(
                  if (useTextarea) {
                    <textarea defaultValue={originValue}  />
                  } else {
                    <input defaultValue={originValue} />
                  }
                )
              }
              <button onClick={self.update}>Update</button>
              <button onClick={self.cancel}>Cancel</button>
            </span>
          case false =>
            val op = if (self.props.allowEditing) self.startEditing else self.doNothing
            <span onClick={op} className="normal">
              {props.normalPlaceholder.getOrElse(props.value)}
            </span>
        }
      }
    </span>
  }


}
