package in.freewind.windlinks

case class Project(name: String, links: Seq[Link] = Nil, description: Option[String] = None)

case class Link(name: String, url: String, description: Option[String] = None)
