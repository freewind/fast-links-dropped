package in.freewind.windlinks

object SampleData {

  val projects = Seq(
    Project("scala", stars = Some(5), basicLinks = Seq(
      Link(name = Some("official"), url = "http://www.scala-lang.org/", description = Some("Official website")),
      Link(name = Some("typesafe"), url = "https://typesafe.com", description = Some("The company supports Scala"))),
      moreLinkGroups = Seq(LinkGroup("more", Seq(
        Link(name = Some("other1"), url = "http://www.other1.org/", description = Some("some other1")),
        Link(name = Some("other2"), url = "https://other2.com", description = Some("some other2"))))),
      description = Some("Links of Scala")),
    Project("scalajs", Seq(
      Link(Some("official"), url = "http://www.scala-js.org/"))),
    Project("react", Seq(
      Link(Some("official"), url = "http://facebook.github.io/react/"),
      Link(url = "http://facebook.github.io/react/")))
  )

}
