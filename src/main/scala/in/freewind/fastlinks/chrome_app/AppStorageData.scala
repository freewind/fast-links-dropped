package in.freewind.fastlinks.chrome_app

import in.freewind.fastlinks.common.ChromeStorageSupport

case class AppStorageData(localDataPath: Option[String], localDataId: Option[String] = None)

import upickle._


object AppStorageData extends ChromeStorageSupport[AppStorageData] {

  override val Key = "in.freewind.fastlinks.chrome_app"
  override def fromJson(json: String): AppStorageData = read[AppStorageData](json)
  override def toJson(obj: AppStorageData): String = write(obj)

}
