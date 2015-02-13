package in.freewind.windlinks.main

import com.xored.scalajs.react.util.TypedEventListeners
import com.xored.scalajs.react.{scalax, TypedReactSpec}
import in.freewind.windlinks.{Link, Project}

object Main extends TypedReactSpec with TypedEventListeners {

  case class State()

  case class Props()

  override def getInitialState(self: This) = State()

  @scalax
  override def render(self: This) = {
    <div>
      {Seq(Search(Search.Props()), Links(Links.Props(projects)))}
    </div>
  }

  private val projects = Seq(
    Project("scala", Seq(
      Link(name = "official", url = "http://www.scala-lang.org/", description = Some("Official website")),
      Link(name = "typesafe", url = "https://typesafe.com", description = Some("The company supports Scala")))),
    Project("scalajs", Seq(
      Link("official", url = "http://www.scala-js.org/"))),
    Project("react", Seq(
      Link("official", url = "http://facebook.github.io/react/")))
  )
}
