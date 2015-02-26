package in.freewind.fastlinks.chrome_app.main

import com.xored.scalajs.react.util.{ClassName, TypedEventListeners}
import com.xored.scalajs.react.{TypedReactSpec, scalax}
import in.freewind.fastlinks.chrome_app.{AppBackend, AppStorageData}
import in.freewind.fastlinks.common.Editable
import in.freewind.fastlinks.libs.Chrome
import in.freewind.fastlinks.wrappers.chrome.DirectoryEntry
import in.freewind.fastlinks.{Category, Meta}

import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

object Header extends TypedReactSpec with TypedEventListeners {

  case class State()

  case class Props(meta: Option[Meta],
                   currentCategory: Option[Category],
                   allowEditing: Boolean,
                   selectCategory: Category => Unit,
                   appBackend: AppBackend)

  override def getInitialState(self: This) = {
    State()
  }

  implicit class Closure(self: This) {

    import self._

    val chooseDataDir = button.onClick(e => {
      Chrome.fileSystem.chooseDirectory().foreach { case (dir, path) =>
        val localDataId = Chrome.fileSystem.retainDir(dir.asInstanceOf[DirectoryEntry])
        val storageData = new AppStorageData(localDataPath = Some(path), localDataId = Some(localDataId))
        props.appBackend.saveStorageData(storageData)
      }
    })

    def selectCategory(category: Category) = button.onClick(e => {
      self.props.selectCategory(category)
    })

    val startEditing = button.onClick(e => {
      props.appBackend.startEditing()
    })

    val doneEditing = button.onClick(e => {
      props.appBackend.doneEditing()
    })

    def onUpdateCategoryName(name: String): Unit = {

    }
  }


  @scalax
  override def render(self: This) = {
    val allowEditing = self.props.allowEditing
    <div className="header">
      {
        self.props.meta match {
          case Some(meta) => meta.categories.map { category =>
            val className = ClassName("current-category" -> (Some(category) == self.props.currentCategory))
            <button onClick={self.selectCategory(category)} className={className}>{category.name}</button>
          }
          case _ => <span>no meta</span>
        }
      }
      <span className="config-panel">
        {
           if (allowEditing) {
             <span>
               <button onClick={self.chooseDataDir}>Choose Data Dir</button>
             </span>
             <button onClick={self.doneEditing} className="done">Done</button>
           } else {
             <button onClick={self.startEditing}>Edit</button>
           }
        }
      </span>
    </div>
  }

}
