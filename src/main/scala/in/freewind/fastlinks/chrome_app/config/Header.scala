package in.freewind.fastlinks.chrome_app.config

import com.xored.scalajs.react.util.TypedEventListeners
import com.xored.scalajs.react.{TypedReactSpec, scalax}
import in.freewind.fastlinks.chrome_app.AppStorageData
import in.freewind.fastlinks.libs.Chrome
import in.freewind.fastlinks.{Category, Meta}
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue


object Header extends TypedReactSpec with TypedEventListeners {

  case class State()

  case class Props(meta: Option[Meta],
                   selectCategory: Category => Unit,
                   saveStorageData: AppStorageData => Unit,
                   goToMainPage: () => Unit)

  override def getInitialState(self: This) = {
    State()
  }

  implicit class Closure(self: This) {

    val chooseDataDir = button.onClick(e => {
      Chrome.fileSystem.chooseDirectory().foreach { case (dir, path) =>
        val localDataId = Chrome.fileSystem.retain(dir)
        val storageData = new AppStorageData(localDataPath = Some(path), localDataId = Some(localDataId))
        self.props.saveStorageData(storageData)
      }
    })

    def selectCategory(category: Category) = button.onClick(e => {
      self.props.selectCategory(category)
    })

    val goToMainPage = element.onClick(e => {
      self.props.goToMainPage()
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
      <span>
        <button onClick={self.chooseDataDir}>Choose Data Dir</button>
      </span>
      <button onClick={self.goToMainPage}>Go to main page</button>
    </div>
  }

}
