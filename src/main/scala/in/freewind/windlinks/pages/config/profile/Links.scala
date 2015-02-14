package in.freewind.windlinks.pages.config.profile

import com.xored.scalajs.react.{scalax, TypedReactSpec}
import com.xored.scalajs.react.util.TypedEventListeners
import in.freewind.windlinks.Link

object Links extends TypedReactSpec with TypedEventListeners {

  case class State()

  case class Props(groupName: Option[String], links: Seq[Link])

  override def getInitialState(self: This) = State()

  implicit class Closure(self: This) {

    val onChange = input.onChange(e => {
      println(e.value)
    })
  }

  override def render(self: This) = {
    @scalax val workaround = {
      val groupName = self.props.groupName match {
        case Some(name) => <div>{name}</div>
        case _ => <div></div>
      }

      val links = self.props.links.map { link =>
        <div>
          <div>{link.name} - {link.url}</div>
          <div>
            {link.description.getOrElse("")}
          </div>
        </div>
      }

      <div className="project-links">
        {groupName}
        {links}
      </div>
    }
    workaround
  }

}

