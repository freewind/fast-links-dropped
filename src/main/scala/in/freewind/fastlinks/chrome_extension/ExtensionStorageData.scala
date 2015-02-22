package in.freewind.fastlinks.chrome_extension

import in.freewind.fastlinks.Project
import in.freewind.fastlinks.common.ChromeStorageSupport
import upickle._

case class ExtensionStorageData(projects: Seq[Project] = Nil, dataUrl: Option[String] = None)

object ExtensionStorageData extends ChromeStorageSupport[ExtensionStorageData] {

  override val Key = "in.freewind.fastlinks.chrome_extension"
  override def fromJson(json: String): ExtensionStorageData = read[ExtensionStorageData](json)
  override def toJson(obj: ExtensionStorageData): String = write(obj)

}
