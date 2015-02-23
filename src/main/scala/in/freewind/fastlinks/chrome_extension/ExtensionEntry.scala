package in.freewind.fastlinks.chrome_extension

import com.xored.scalajs.react.{scalax, TypedReactSpec}
import com.xored.scalajs.react.util.TypedEventListeners
import in.freewind.fastlinks.{Project, DataConverter}
import in.freewind.fastlinks.chrome_extension.main.Setup
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

object ExtensionEntry extends TypedReactSpec with TypedEventListeners {

  case class State(projects: Seq[Project] = Nil,
                   dataUrl: Option[String] = None,
                   storageData: Option[ExtensionStorageData] = None)

  case class Props()

  override def getInitialState(self: This) = {
    for {
      dataOpt <- ExtensionStorageData.load()
      data <- dataOpt
    } self.setState(self.state.copy(projects = data.projects, dataUrl = data.dataUrl, storageData = Some(data)))

    State()
  }

  implicit class Closure(self: This) {

    import self._

    def onDataFetched(url: String, json: String): Unit = {
      val storageData = new ExtensionStorageData(projects = DataConverter.parse(json), dataUrl = Some(url))
      ExtensionStorageData.save(storageData).foreach { data =>
        self.setState(state.copy(projects = data.projects, dataUrl = data.dataUrl))
      }
    }

  }

  @scalax
  override def render(self: This) = {
    <div id="main-page">
      {FastLinks(FastLinks.Props(projects = self.state.projects))}
      {Setup(Setup.Props(self.onDataFetched, self.state.dataUrl))}
    </div>
  }

}
