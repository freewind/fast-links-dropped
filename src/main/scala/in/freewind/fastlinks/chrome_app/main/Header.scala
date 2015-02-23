package in.freewind.fastlinks.chrome_app.main

import com.xored.scalajs.react.{scalax, TypedReactSpec}
import com.xored.scalajs.react.util.TypedEventListeners
import in.freewind.fastlinks.{Category, Meta}
import in.freewind.fastlinks.chrome_app.AppBackend

object Header extends TypedReactSpec with TypedEventListeners {

  case class State()

  case class Props(meta: Option[Meta],
                   selectCategory: Category => Unit,
                   appBackend: AppBackend)

  override def getInitialState(self: This) = State()

  implicit class Closure(self: This) {

    val goToConfigPage = button.onClick(e => {
      self.props.appBackend.goToConfigPage()
    })

    def selectCategory(category: Category) = button.onClick(e => {
      self.props.selectCategory(category)
    })
  }

  @scalax
  override def render(self: This) = {
    <div className="config-header">
      {
        self.props.meta match {
          case Some(meta) => meta.categories.map(category =>
            <button onClick={self.selectCategory(category)}>{category.name}</button>
          )
          case _ => <span>no meta</span>
        }
      }
      <button onClick={self.goToConfigPage}>config</button>
    </div>
  }

}
