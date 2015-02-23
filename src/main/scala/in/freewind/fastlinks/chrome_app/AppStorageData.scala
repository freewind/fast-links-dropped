package in.freewind.fastlinks.chrome_app

import in.freewind.fastlinks.libs.Chrome

import scala.concurrent.Future

case class AppStorageData(localDataPath: Option[String], localDataId: Option[String] = None)

import upickle._

object AppStorageData {

  val Key = "in.freewind.fastlinks.chrome_app"

  def load(): Future[Option[AppStorageData]] = Chrome.storage.load(Key, read[AppStorageData])

  def save(data: AppStorageData): Future[AppStorageData] = Chrome.storage.save[AppStorageData](Key, data, write)

}
