package com.newyorkis

import com.twitter.finagle.builder.Server
import com.twitter.util.Duration

object Foo {
  def main(args: Array[String]) = println("foursquare hackathon, yeah!")

  val bar = new Bar()
  println(bar.toString)
}

class Bar extends Server {
  def close(timeout: Duration) = { }
}


// vim: set ts=2 sw=2 et:
