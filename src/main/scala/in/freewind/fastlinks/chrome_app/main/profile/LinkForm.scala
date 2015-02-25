package in.freewind.fastlinks.chrome_app.main.profile

import com.xored.scalajs.react.{scalax, TypedReactSpec}
import com.xored.scalajs.react.util.TypedEventListeners
import in.freewind.fastlinks.Link
import in.freewind.fastlinks.RichString
import org.scalajs.dom.HTMLInputElement

object LinkForm extends TypedReactSpec with TypedEventListeners {

  case class State()

  case class Props(link: Option[Link] = None,
                   newLink: Option[Link => Unit] = None,
                   updateLink: Option[(Link) => Unit] = None,
                   cancel: () => Unit)

  def New(newLink: Link => Unit, cancel: () => Unit) = {
    LinkForm(LinkForm.Props(newLink = Some(newLink), cancel = cancel))
  }

  def Edit(link: Link, updateLink: (Link) => Unit, cancel: () => Unit) = {
    LinkForm(LinkForm.Props(link = Some(link), updateLink = Some(updateLink), cancel = cancel))
  }

  override def getInitialState(self: This) = State()

  implicit class Closure(self: This) {

    import self._

    val addOrUpdate = button.onClick(e => {
      val newLink = new Link(getValue("name").empty2option, getValue("url"), Option(getValue("desc")).filterNot(_.isEmpty), isChecked("showUrl"))
      println(newLink)
      props.link match {
        case Some(existingLink) => props.updateLink.foreach(_(newLink))
        case _ => props.newLink.foreach(_(newLink))
      }
    })

    private def getValue(key: String) = getNode(key).value.trim
    private def isChecked(key: String) = getNode(key).checked
    private def getNode(key: String) = refs(key).getDOMNode().asInstanceOf[HTMLInputElement]

  }

  @scalax
  override def render(self: This) = {
    val link = self.props.link
    <div>
      <div>
        <div><input ref="name" defaultValue={link.flatMap(_.name).getOrElse("")} /></div>
        <div><input ref="url" defaultValue={link.map(_.url).getOrElse("")}/></div>
        <div><textarea ref="desc" defaultValue={link.flatMap(_.description).getOrElse("")} /></div>
        <div><input ref="showUrl" type="checkbox" defaultChecked={link.map(_.showUrl).getOrElse(true)} />Show url ?</div>
        <button onClick={self.addOrUpdate}>Ok</button>
        <button onClick={self.props.cancel}>Cancel</button>
      </div>
    </div>
  }

}
