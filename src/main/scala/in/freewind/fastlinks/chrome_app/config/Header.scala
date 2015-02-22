package in.freewind.fastlinks.chrome_app.config

import java.io.IOException

import com.xored.scalajs.react.util.TypedEventListeners
import com.xored.scalajs.react.{TypedReactSpec, scalax}
import in.freewind.fastlinks.{Project, Category, Meta}
import in.freewind.fastlinks.chrome_app.AppStorageData
import in.freewind.fastlinks.wrappers.chrome._
import org.scalajs.dom
import org.scalajs.dom._

import scala.concurrent.{Future, Promise}
import scala.scalajs.js
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

import upickle._

object Header extends TypedReactSpec with TypedEventListeners {

  case class State(meta: Option[Meta] = None, storageData: Option[AppStorageData] = None)

  case class Props()

  private def isRestorable(id: String): Future[Boolean] = {
    val p = Promise[Boolean]()
    chrome.fileSystem.isRestorable(id, (isRestorable: Boolean) => {
      p.success(isRestorable)
      ()
    })
    p.future
  }

  private def restore(id: String): Future[Entry] = {
    val p = Promise[Entry]()
    chrome.fileSystem.restoreEntry(id, (entry: Entry) => {
      p.success(entry)
      ()
    })
    p.future
  }

  private def readFile(dir: DirectoryEntry, fileName: String): Future[String] = {
    val p = Promise[String]()
    dir.getFile("_meta_.json", js.Dynamic.literal(), (entry: FileEntry) => {
      dom.console.dir(entry)
      println(s"${entry.isFile } ${entry.isDirectory } ")
      if (entry != null) {
        entry.file((file: FileEntry) => {
          println("######## entry.createReader: ")
          val reader = new FileReader()
          println("######### reader created:" + reader)
          reader.onload = (event: UIEvent) => {
            println("############ read file: " + event)
            ()
          }
          reader.onloadend = (event: ProgressEvent) => {
            println("############ read file: " + reader.result)
            p.success(reader.result.toString)
            ()
          }
          reader.onerror = (event: Event) => {
            println("######### read error")
            p.failure(new IOException("read error"))
            ()
          }
          println("###### going to read")
          reader.readAsText(entry.asInstanceOf[Blob])
          ()
        })
      }
      ()
    })
    p.future
  }

  private def readMeta(dir: DirectoryEntry): Future[Meta] = {
    val p = Promise[Meta]()
    readFile(dir, "_meta_.json").map(read[_Meta_]).foreach { _meta_ =>
      val categoryFutures = _meta_.categories.map(category =>
        readFile(dir, category.filename).map(read[Seq[Project]]).map(ps => new Category(category.name, ps, category.description))
      )
      Future.sequence(categoryFutures).foreach { categories =>
        p.success(new Meta(categories))
      }
    }
    p.future
  }

  override def getInitialState(self: This) = {
    import self._
    AppStorageData.load().foreach {
      case Some(data@AppStorageData(Some(localPath), Some(localId))) =>
        setState(state.copy(storageData = Some(data)))
        isRestorable(localId).foreach {
          case true => restore(localId).foreach { entry =>
            val dir = entry.asInstanceOf[DirectoryEntry]
            readMeta(dir).foreach(m => setState(state.copy(meta = Some(m))))
          }
          case _ => println("#### no restored")
        }
      case _ =>
    }

    State()
  }

  implicit class Closure(self: This) {

    private def chooseDirectory(): Future[(Entry, String)] = {
      val p = Promise[(Entry, String)]()
      chrome.fileSystem.chooseEntry(js.Dynamic.literal("type" -> "openDirectory"), (dir: Entry) => {
        chrome.fileSystem.getDisplayPath(dir.asInstanceOf[DirectoryEntry], (path: String) => {
          p.success((dir, path))
          ()
        })
      })
      p.future
    }

    val chooseDataDir = button.onClick(e => {
      chooseDirectory().foreach { case (dir, path) =>
        val localDataId = chrome.fileSystem.retainEntry(dir)
        val storageData = new AppStorageData(localDataPath = Some(path), localDataId = Some(localDataId))
        AppStorageData.save(storageData).foreach(data => self.setState(self.state.copy(storageData = Some(data))))
      }
    })
  }

  @scalax
  override def render(self: This) = {
    <div className="config-header">
      { self.state.meta.map(_.categories.map(_.name)).getOrElse("no meta") }
      <span>
        <button onClick={self.chooseDataDir}>Choose Data Dir</button>
      </span>
    </div>
  }

}

case class _Meta_(categories: Seq[_Category_])

case class _Category_(name: String, filename: String, description: Option[String])
