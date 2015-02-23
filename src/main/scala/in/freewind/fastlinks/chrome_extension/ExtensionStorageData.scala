package in.freewind.fastlinks.chrome_extension

import in.freewind.fastlinks.Project
import in.freewind.fastlinks.libs.Chrome
import upickle._

import scala.concurrent.Future

case class ExtensionStorageData(projects: Seq[Project] = Nil, dataUrl: Option[String] = None)

object ExtensionStorageData {


  val Key = "in.freewind.fastlinks.chrome_extension"

  def load(): Future[Option[ExtensionStorageData]] = Chrome.storage.load(Key, read[ExtensionStorageData])

  def save(data: ExtensionStorageData): Future[ExtensionStorageData] = Chrome.storage.save[ExtensionStorageData](Key, data, write)

}
