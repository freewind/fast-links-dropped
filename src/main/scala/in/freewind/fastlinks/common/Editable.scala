package in.freewind.fastlinks.common

import com.xored.scalajs.react.util.{ClassName, TypedEventListeners}
import com.xored.scalajs.react.{ReactDOM, TypedReactSpec, scalax}
import org.scalajs.dom.HTMLInputElement

object Editable extends TypedReactSpec with TypedEventListeners {

  sealed trait InputType

  case class State(editing: Boolean = false)

  case class Props(useTextarea: Boolean,
                   allowEditing: Boolean,
                   value: Option[String],
                   onOk: String => Unit,
                   className: Option[String],
                   normalPlaceholder: Option[ReactDOM],
                   editingPlaceholder: Option[ReactDOM],
                   editing: Boolean)

  def Input(allowEditing: Boolean, value: Option[String], onOk: String => Unit, className: Option[String] = None,
            normalPlaceholder: Option[ReactDOM] = None, editingPlaceholder: Option[ReactDOM] = None, editing: Boolean = false) = {
    apply(Props(useTextarea = false, allowEditing, value, onOk, className, normalPlaceholder, editingPlaceholder, editing))
  }

  def Textarea(allowEditing: Boolean, value: Option[String], onOk: String => Unit, className: Option[String] = None,
               normalPlaceholder: Option[ReactDOM] = None, editingPlaceholder: Option[ReactDOM] = None, editing: Boolean = false) = {
    apply(Props(useTextarea = true, allowEditing, value, onOk, className, normalPlaceholder, editingPlaceholder, editing))
  }

  override def getInitialState(self: This) = State(editing = self.props.editing)

  override def componentWillReceiveProps(self: This, nextProps: Props): Unit = {
    import self._
    if (!nextProps.allowEditing) {
      setState(state.copy(editing = false))
    }
  }

  implicit class Closure(self: This) {

    import self._

    val startEditing = element.onClick(e => {
      e.stopPropagation()
      setState(state.copy(editing = true), () => {
        val node = getEditingNode
        props.value.foreach(v => node.value = v)
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
                    <textarea defaultValue={originValue.getOrElse("")} />
                  } else {
                    <input defaultValue={originValue.getOrElse("")} />
                  }
                )
              }
              <button onClick={self.update}>Update</button>
              <button onClick={self.cancel}>Cancel</button>
            </span>
          case false =>
            val (op, defaultValue) = if (self.props.allowEditing) (self.startEditing, "no content, click to edit") else (self.doNothing, "")
            <span onClick={op} className="normal">
              {props.normalPlaceholder.getOrElse(props.value.getOrElse(defaultValue))}
            </span>
        }
      }
    </span>
  }


}
