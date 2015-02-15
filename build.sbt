enablePlugins(ScalaJSPlugin)

name := "wind-links"

organization := "in.freewind"

version := "1.0"

scalaVersion := "2.11.5"

resolvers += Resolver.sonatypeRepo("releases")

libraryDependencies ++= Seq(
  "com.xored.scalajs" %%% "scalajs-react" % "0.3.3",
  compilerPlugin("org.scalamacros" % "paradise" % "2.0.1" cross CrossVersion.full)
)

// creates resi-links-jsdeps.js
skip in packageJSDependencies := false

seq(lessSettings: _*)

(compile in Compile) <<= compile in Compile dependsOn (LessKeys.less in Compile)
