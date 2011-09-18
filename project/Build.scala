import sbt._

object NewYorkIsBuild extends Build {
  val push = TaskKey[Unit]("push", "Pushes code to EC2")
  val pushStatic = TaskKey[Unit]("push-static", "Pushes static files to EC2")
  val remoteApiRestart = TaskKey[Unit]("remote-api-restart", "Restarts new.york.is API process through supervisor")
  lazy val project = Project("default", file("."))
}
