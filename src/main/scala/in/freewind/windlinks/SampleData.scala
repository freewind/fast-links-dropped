package in.freewind.windlinks

object SampleData {

  val projects = Seq(
    Project("scala", Seq(
      Link(name = "official", url = "http://www.scala-lang.org/", description = Some("Official website")),
      Link(name = "typesafe", url = "https://typesafe.com", description = Some("The company supports Scala")))),
    Project("scalajs", Seq(
      Link("official", url = "http://www.scala-js.org/"))),
    Project("react", Seq(
      Link("official", url = "http://facebook.github.io/react/")))
  )

}
