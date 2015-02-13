package in.freewind.windlinks

case class Project(title: String, links: Seq[Link])

case class Link(name: String, url: String, description: Option[String] = None)
