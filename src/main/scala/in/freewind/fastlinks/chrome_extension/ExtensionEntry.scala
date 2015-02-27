package in.freewind.fastlinks.chrome_extension

import com.xored.scalajs.react.util.TypedEventListeners
import com.xored.scalajs.react.{TypedReactSpec, scalax}
import in.freewind.fastlinks.chrome_extension.main.{CategoryList, Setup}
import in.freewind.fastlinks.common.Search
import in.freewind.fastlinks.{Category, DataConverter}

import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

object ExtensionEntry extends TypedReactSpec with TypedEventListeners {

  case class State(category: Option[Category] = None,
                   dataUrl: Option[String] = None,
                   storageData: Option[ExtensionStorageData] = None)

  case class Props()

  override def getInitialState(self: This) = {
    for {
      dataOpt <- ExtensionStorageData.load()
      data <- dataOpt
    } self.setState(self.state.copy(category = data.category, dataUrl = data.dataUrl, storageData = Some(data)))

    State()
  }

  implicit class Closure(self: This) {

    import self._

    def onDataFetched(url: String, json: String): Unit = {
      val storageData = new ExtensionStorageData(category = Some(DataConverter.parse(json)), dataUrl = Some(url))
      ExtensionStorageData.save(storageData).foreach { data =>
        self.setState(state.copy(category = data.category, dataUrl = data.dataUrl))
      }
    }

  }

  @scalax
  override def render(self: This) = {
    import self._
    <div id="main-page">
      {Search(Search.Props(state.category.toList, Some(CategoryList(CategoryList.Props(state.category.toList)))))}
      {Setup(Setup.Props(self.onDataFetched, self.state.dataUrl))}
    </div>
  }

}
