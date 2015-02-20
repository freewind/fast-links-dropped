package in.freewind.fastlinks

case class Project(name: String,
                   basicLinks: Seq[Link] = Nil,
                   moreLinkGroups: Seq[LinkGroup] = Nil,
                   description: Option[String] = None,
                   stars: Option[Int] = None)

case class LinkGroup(name: String, links: Seq[Link] = Nil)

case class Link(name: Option[String] = None, url: String, description: Option[String] = None)
