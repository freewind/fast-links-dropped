package in.freewind.fastlinks.chrome_app

import com.xored.scalajs.react.util.TypedEventListeners
import com.xored.scalajs.react.{TypedReactSpec, scalax}
import in.freewind.fastlinks.libs.Chrome
import in.freewind.fastlinks.wrappers.chrome.DirectoryEntry
import in.freewind.fastlinks.{Meta, MetaFiles}
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

object AppEntry extends TypedReactSpec with TypedEventListeners {

  sealed trait Page

  case object SelectedMainPage extends Page

  case object SelectedConfigPage extends Page

  case class State(currentPage: Page, meta: Option[Meta] = None, storageData: Option[AppStorageData] = None)

  case class Props()

  override def getInitialState(self: This) = {
    import self._
    AppStorageData.load().foreach {
      case Some(data@AppStorageData(Some(localPath), Some(localId))) =>
        setState(state.copy(storageData = Some(data)))
        Chrome.fileSystem.isRestorable(localId).foreach {
          case true => Chrome.fileSystem.restore(localId).foreach { entry =>
            val dir = entry.asInstanceOf[DirectoryEntry]
            MetaFiles.readMeta(dir).foreach(m => setState(state.copy(meta = Some(m))))
          }
          case _ => println("#### data dir can't be restored")
        }
      case _ =>
    }
    State(SelectedMainPage)
  }

  implicit class Closure(self: This) extends AppBackend {

    import self._

    private def selectPage(page: Page) = setState(state.copy(currentPage = page))
    def goToConfigPage(): Unit = selectPage(SelectedConfigPage)
    def goToMainPage(): Unit = selectPage(SelectedMainPage)
    def saveStorageData(storageData: AppStorageData): Unit = {
      AppStorageData.save(storageData).foreach(data => setState(state.copy(storageData = Some(data))))
    }
    def updateMeta(meta: Meta) = {
      setState(state.copy(meta = Some(meta)))
    }
  }

  @scalax
  override def render(self: This) = {
    val backend = new Closure(self)
    self.state.currentPage match {
      case SelectedMainPage => MainPage(MainPage.Props(self.state.meta, backend))
      case SelectedConfigPage => ConfigPage(ConfigPage.Props(self.state.meta, backend))
    }
  }
}

// create a standalone trait outside the AppEntry, is for avoiding the circle reference compilation error
trait AppBackend {
  def goToConfigPage(): Unit
  def goToMainPage(): Unit
  def saveStorageData(storageData: AppStorageData): Unit
  def updateMeta(meta: Meta)
}
