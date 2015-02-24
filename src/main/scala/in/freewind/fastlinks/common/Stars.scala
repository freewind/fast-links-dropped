package in.freewind.fastlinks.common

import com.xored.scalajs.react.{scalax, TypedReactSpec}
import com.xored.scalajs.react.util.TypedEventListeners

object Stars extends TypedReactSpec with TypedEventListeners {

  case class State()

  case class Props(allowEditing: Boolean, count: Option[Int])

  override def getInitialState(self: This) = State()

  @scalax
  override def render(self: This) = {
    import self.props
    props.count match {
      case Some(n) => <span className="stars"> {(1 to n).toList.map(_ => "★")}</span>
      case _ => if (props.allowEditing) <span>no stars yet, click to set</span> else <span></span>
    }
  }

}
