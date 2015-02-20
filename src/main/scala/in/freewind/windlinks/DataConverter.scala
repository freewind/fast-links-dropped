package in.freewind.windlinks

object DataConverter {

  def stringify(projects: Seq[Project]): String = {
    upickle.write(projects)
  }

  def parse(json: String): Seq[Project] = {
    upickle.read[Seq[Project]](json)
  }

}
