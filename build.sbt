val scala3Version = "3.7.4"

ThisBuild / scalacOptions ++= Seq(
  "-Xprint:postInlining",
  "-Xmax-inlines:100000"
)

lazy val root = project
  .in(file("."))
  .settings(
    name := "metaprogramming",
    version := "0.1.0-SNAPSHOT",
    scalaVersion := scala3Version,
    libraryDependencies ++= Seq(
      "org.postgresql" % "postgresql" % "42.7.1"
    )
  )
