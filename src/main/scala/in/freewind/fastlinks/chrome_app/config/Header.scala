package in.freewind.fastlinks.chrome_app.config

import com.xored.scalajs.react.{TypedReactSpec, scalax}
import com.xored.scalajs.react.util.TypedEventListeners
import in.freewind.fastlinks.wrappers.chrome.{Entry, DirectoryEntry, FileEntry, chrome}
import org.scalajs.dom

import scala.scalajs.js

object Header extends TypedReactSpec with TypedEventListeners {

  case class State()

  case class Props()

  override def getInitialState(self: This) = State()

  implicit class Closure(self: This) {

    private def chooseDirectory(callback: js.Function2[DirectoryEntry, js.Array[FileEntry], Unit]): Unit = {
      chrome.fileSystem.chooseEntry(js.Dynamic.literal("type" -> "openDirectory"), (dir: Entry, files: js.Array[FileEntry]) => {
        callback(dir.asInstanceOf[DirectoryEntry], files)
        chrome.fileSystem.getDisplayPath(dir.asInstanceOf[DirectoryEntry], (path: String) => {
          println("### path: " + path)
          ()
        })
      })
    }

    val chooseDataDir = button.onClick(e => {
      chooseDirectory((dir: DirectoryEntry, files: js.Array[FileEntry]) => {
        println("###########: " + dir.fullPath)
        dom.console.dir(dir)
      })
    })
  }

  @scalax
  override def render(self: This) = {
    <div className="config-header">
      Header
      <span>
        <button onClick={self.chooseDataDir}>Choose Data Dir</button>
      </span>
    </div>
  }

}
