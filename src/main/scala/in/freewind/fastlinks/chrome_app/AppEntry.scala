package in.freewind.fastlinks.chrome_app

import com.xored.scalajs.react.util.TypedEventListeners
import com.xored.scalajs.react.{TypedReactSpec, scalax}
import in.freewind.fastlinks.libs.Chrome
import in.freewind.fastlinks.{Meta, MetaFiles}
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

object AppEntry extends TypedReactSpec with TypedEventListeners {

  case class State(meta: Option[Meta] = None, storageData: Option[AppStorageData] = None, allowEditing: Boolean = false)

  case class Props()

  override def getInitialState(self: This) = {
    import self._
    AppStorageData.load().foreach {
      case Some(data@AppStorageData(Some(localPath), Some(localId))) =>
        setState(state.copy(storageData = Some(data)))
        Chrome.fileSystem.isRestorable(localId).foreach {
          case true => initMeta(self, localId)
          case _ => println("#### data dir can't be restored")
        }
      case _ =>
    }
    State()
  }

  private def initMeta(self: This, localId: String): Unit = {
    import self._
    Chrome.fileSystem.restoreDir(localId).foreach { dir =>
      MetaFiles.readMeta(dir).foreach(m => setState(state.copy(meta = Some(m))))
    }
  }

  implicit class Closure(self: This) extends AppBackend {

    import self._

    def saveStorageData(storageData: AppStorageData): Unit = {
      AppStorageData.save(storageData).foreach { data =>
        setState(state.copy(storageData = Some(data)))
        data.localDataId.foreach(id => initMeta(self, id))
      }
    }

    def updateMeta(meta: Meta): Unit = {
      setState(state.copy(meta = Some(meta)))
    }
    def startEditing(): Unit = {
      setState(state.copy(allowEditing = true))
    }

    def doneEditing(): Unit = {
      saveFiles()
      setState(state.copy(allowEditing = false))
    }

    private def saveFiles(): Unit = {
      for {
        data <- self.state.storageData
        dirId <- data.localDataId
        dir <- Chrome.fileSystem.restoreDir(dirId)
        meta <- self.state.meta
      } MetaFiles.saveMeta(dir, meta)
    }
  }

  @scalax
  override def render(self: This) = {
    val backend = new Closure(self)
    MainPage(MainPage.Props(self.state.meta, self.state.allowEditing, backend))
  }
}

// create a standalone trait outside the AppEntry, is for avoiding the circle reference compilation error
trait AppBackend {
  def saveStorageData(storageData: AppStorageData): Unit
  def updateMeta(meta: Meta): Unit
  def startEditing(): Unit
  def doneEditing(): Unit
}
