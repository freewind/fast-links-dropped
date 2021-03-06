import sbt._

enablePlugins(ScalaJSPlugin)

name := "fast-links"

organization := "in.freewind"

version := "1.0"

scalaVersion := "2.11.5"

resolvers += Resolver.sonatypeRepo("releases")

libraryDependencies ++= Seq(
  "com.xored.scalajs" %%% "scalajs-react" % "0.3.3",
  "com.lihaoyi" %%% "upickle" % "0.2.6",
  compilerPlugin("org.scalamacros" % "paradise" % "2.0.1" cross CrossVersion.full)
)

// creates resi-links-jsdeps.js
skip in packageJSDependencies := false

seq(lessSettings: _*)

(compile in Compile) <<= compile in Compile dependsOn (LessKeys.less in Compile)

lazy val chrome = taskKey[Unit]("Generate the chrome app")

chrome <<= (fastOptJS in Compile) map { _ =>
  Seq("app", "extension").foreach { dir =>
    val to = new sbt.File("./target/chrome", dir)
    IO.createDirectory(to)
    to.listFiles.foreach(IO.delete)
    IO.copyDirectory(new File("./target/scala-2.11/classes", dir), to)
    Seq("fast-links-jsdeps.js", "fast-links-fastopt.js", "fast-links-fastopt.js.map").foreach(fileName =>
      IO.copyFile(new File("./target/scala-2.11/" + fileName), new File(to, s"js/$fileName")))
    val cssPath = "css/main.css"
    IO.copyFile(new File(s"./target/scala-2.11/resource_managed/main/resources/$dir", cssPath), new File(to, cssPath))
  }
}
