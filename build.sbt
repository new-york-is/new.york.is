name := "new.york.is"

version := "1.0"

scalaVersion := "2.8.1"

libraryDependencies ++= Seq(
    "org.specs2" %% "specs2" % "1.5",
    "org.specs2" %% "specs2-scalaz-core" % "5.1-SNAPSHOT" % "test"
)

resolvers ++= Seq("snapshots" at "http://scala-tools.org/repo-snapshots",
                  "releases" at "http://scala-tools.org/repo-releases")

