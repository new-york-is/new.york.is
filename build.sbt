name := "new.york.is"

version := "1.0"

scalaVersion := "2.8.1"

scalacOptions ++= Seq(
  "-deprecation",
  "-unchecked")

libraryDependencies ++= Seq(
    "org.specs2" %% "specs2" % "1.5",
    "org.specs2" %% "specs2-scalaz-core" % "5.1-SNAPSHOT" % "test",
    "com.twitter" % "finagle-core" % "1.9.0",
    "com.twitter" % "finagle-http" % "1.9.0",
    "com.twitter" % "finagle-ostrich4" % "1.9.0",
    "org.scalaj" %% "scalaj-collection" % "1.1",
    "org.scalaj" %% "scalaj-http" % "0.2.7",
    "net.liftweb" %% "lift-json" % "2.4-M2",
    "org.scala-tools.time" %% "time" % "0.4"
)

resolvers ++= Seq("snapshots" at "http://scala-tools.org/repo-snapshots",
                  "releases" at "http://scala-tools.org/repo-releases",
                  "twitter.com" at "http://maven.twttr.com/")

