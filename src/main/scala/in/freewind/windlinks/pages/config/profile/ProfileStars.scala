package in.freewind.windlinks.pages.config.profile

import com.xored.scalajs.react.{TypedReactSpec, scalax}
import com.xored.scalajs.react.util.TypedEventListeners
import org.scalajs.dom.HTMLInputElement

object ProfileStars extends TypedReactSpec with TypedEventListeners {

  val RefInput = "input"

  case class State(editing: Boolean = false)

  case class Props(value: Option[Int], updateStars: Int => Unit)

  override def getInitialState(self: This) = State()

  implicit class Closure(self: This) {

    import self._

    val startEditing = element.onClick(e => {
      setState(state.copy(editing = true), () => {
        self.refs(RefInput).getDOMNode().focus()
      })
    })

    val update = button.onClick(e => {
      val value = refs(RefInput).getDOMNode().asInstanceOf[HTMLInputElement].value.trim
      props.updateStars(value.toInt)
      setState(state.copy(editing = false))
    })

    val cancel = button.onClick(e => {
      setState(state.copy(editing = false))
    })
  }

  @scalax
  override def render(self: This) = {
    val value = self.props.value
    if (self.state.editing) {
      <div>
        <input type="text" defaultValue={value.map(_.toString).getOrElse("")}  ref={RefInput}/>
        <span>(1 ~ 5)</span>
        <button onClick={self.update}>Update</button>
        <button onClick={self.cancel}>Cancel</button>
      </div>
    } else {
      value match {
        case Some(v) =>
          <div onClick={self.startEditing}>
            {
              (1 to v).map(_ => <span>*</span>)
            }
          </div>
        case _ =>
          <div onClick={self.startEditing}>no stars. click to set</div>
      }
    }
  }

}

