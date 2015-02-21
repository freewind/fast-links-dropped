package in.freewind.fastlinks.pages.main

import com.xored.scalajs.react.{TypedReactSpec, scalax}
import com.xored.scalajs.react.util.TypedEventListeners
import in.freewind.fastlinks.Link

object OneLink extends TypedReactSpec with TypedEventListeners {

  case class State(showDescription: Boolean = false)

  case class Props(link: Link)

  override def getInitialState(self: This) = State()

  implicit class Closure(self: This) {

    import self._

    val showDescription = element.onClick(e => {
      setState(state.copy(showDescription = !state.showDescription))
    })
  }

  @scalax
  override def render(self: This) = {
    val link = self.props.link
    <div>
      <div>
        {
          link.name.map(linkName => <span className="link-name">[{linkName}]</span>)
        }
        <a className="link-url" href={link.url} target="_blank">{link.url}</a>
        {
          link.description.map(_ =>
            <span onClick={self.showDescription} className="show-description">[+]</span>
          )
        }
      </div>
      {
        Some(self.state.showDescription).filter(_ == true).map(_ =>
          link.description.map(_ => <div className="link-description">{link.description}</div>)
        )
      }
    </div>
  }

}
