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

persistLauncher in Compile := true

lazy val chrome = taskKey[Unit]("Generate the chrome app")

chrome <<= (fastOptJS in Compile) map { _ =>
  val to = new File("./target/chrome")
  IO.delete(to)
  IO.createDirectory(to)
  IO.copyFile(new File("./target/scala-2.11/resource_managed/main/resources/css/main.css"), new File(to, "css/main.css"))
  Seq("js", "css", "images").foreach(dirName => IO.copyDirectory(new File("./target/scala-2.11/classes/" + dirName), new File(to, dirName)))
  Seq("index.html", "manifest.json").foreach(fileName => IO.copyFile(new File("./target/scala-2.11/classes/" + fileName), new File(to, fileName)))
  Seq("fast-links-jsdeps.js", "fast-links-fastopt.js", "fast-links-launcher.js").foreach(fileName =>
    IO.copyFile(new File("./target/scala-2.11/" + fileName), new File(to, "js/" + fileName)))
}

