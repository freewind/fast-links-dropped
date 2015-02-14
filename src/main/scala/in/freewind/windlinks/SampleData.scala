package in.freewind.windlinks

object SampleData {

  val projects = Seq(
    Project("scala", basicLinks = Seq(
      Link(name = "official", url = "http://www.scala-lang.org/", description = Some("Official website")),
      Link(name = "typesafe", url = "https://typesafe.com", description = Some("The company supports Scala"))),
      moreLinkGroups = Seq(LinkGroup("more", Seq(
        Link(name = "other1", url = "http://www.other1.org/", description = Some("some other1")),
        Link(name = "other2", url = "https://other2.com", description = Some("some other2"))))),
      description = Some("Links of Scala")),
    Project("scalajs", Seq(
      Link("official", url = "http://www.scala-js.org/"))),
    Project("react", Seq(
      Link("official", url = "http://facebook.github.io/react/")))
  )

}
