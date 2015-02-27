package in.freewind.fastlinks

case class Meta(categories: Seq[Category])

case class Category(name: String, projects: Seq[Project], description: Option[String] = None)

case class Project(name: String,
                   linkGroups: Seq[LinkGroup] = Nil,
                   description: Option[String] = None,
                   stars: Option[Int] = None)

case class LinkGroup(name: String, links: Seq[Link] = Nil)

case class Link(name: Option[String] = None, url: String, description: Option[String] = None, showUrl: Boolean = true)
