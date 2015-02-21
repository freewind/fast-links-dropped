package in.freewind.fastlinks

import upickle._

object DataConverter {

  def stringify(projects: Seq[Project]): String = {
    write(projects)
  }

  def parse(json: String): Seq[Project] = {
    read[Seq[Project]](json)
  }

}
