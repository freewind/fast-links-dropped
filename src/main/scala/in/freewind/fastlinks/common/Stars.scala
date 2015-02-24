package in.freewind.fastlinks.common

import com.xored.scalajs.react.{scalax, TypedReactSpec}
import com.xored.scalajs.react.util.TypedEventListeners

object Stars extends TypedReactSpec with TypedEventListeners {

  case class State()

  case class Props(count: Option[Int])

  override def getInitialState(self: This) = State()

  @scalax
  override def render(self: This) = {
    <span className="stars">{showStars(self.props.count)}</span>
  }

  private def showStars(stars: Option[Int]) = stars match {
    case Some(n) => (1 to n).toList.map(_ => "★")
    case _ => ""
  }

}
