package in.freewind.fastlinks.common

import com.xored.scalajs.react.{scalax, TypedReactSpec}
import com.xored.scalajs.react.util.TypedEventListeners
import in.freewind.fastlinks.chrome_app.AppBackend

object Dialog extends TypedReactSpec with TypedEventListeners {

  case class DialogContext(message: String, onOk: () => Unit)
  case class State()

  case class Props(context: DialogContext, backend: AppBackend)

  override def getInitialState(self: This) = State()

  implicit class Closure(self: This) {
    import self._
    val onOk = button.onClick(e => {
      props.context.onOk()
      props.backend.hideDialog()
    })

    val onCancel = button.onClick(e => {
      props.backend.hideDialog()
    })
  }

  @scalax
  override def render(self: This) = {
    import self._
    <div className="dialog">
      <div>{props.context.message}</div>
      <div>
        <button onClick={self.onOk}>OK</button>
        <button onClick={self.onCancel}>Cancel</button>
      </div>
    </div>
  }

}

