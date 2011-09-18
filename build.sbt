name := "newyorkis"

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
    "com.twitter" % "util-eval" % "1.8.17",
    "org.scala-tools.time" %% "time" % "0.4",
    "org.scalaj" %% "scalaj-collection" % "1.1",
    "org.scalaj" %% "scalaj-http" % "0.2.7",
    "net.liftweb" %% "lift-json" % "2.4-M2",
    "net.liftweb" %% "lift-mongodb-record" % "2.4-M2",
    "com.foursquare" %% "rogue" % "1.0.23" intransitive()
)

resolvers ++= Seq("snapshots" at "http://scala-tools.org/repo-snapshots",
                  "releases" at "http://scala-tools.org/repo-releases",
                  "twitter.com" at "http://maven.twttr.com/")

push <<= (assembly in Assembly, streams) map { (jar, s) =>
  val timestamp = {
    val f = new java.text.SimpleDateFormat("yyyy-MM-dd-HH-mm")
    f.setTimeZone(java.util.TimeZone.getTimeZone("UTC"))
    f.format(new java.util.Date)
  }
  val nameParts = jar.getName.split('.')
  val targetName = nameParts.patch(nameParts.size - 1, Seq(timestamp), 0).mkString(".")
  val targetPath = "/home/www/builds/" + targetName
  ("scp -p " + jar +" new.york.is:" + targetPath) ! s.log
  ("ssh new.york.is ln -fs " + targetName + " " + "/home/www/builds/root.jar") ! s.log
}

pushStatic <<= (streams) map { (s) =>
  ("scp -r static new.york.is:/home/www") ! s.log
}

remoteApiRestart <<= (streams) map { (s) =>
  ("ssh new.york.is sudo supervisorctl restart newyorkis") ! s.log
}

logLevel in push := Level.Debug
