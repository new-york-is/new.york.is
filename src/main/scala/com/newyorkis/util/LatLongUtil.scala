package com.newyorkis
package util

import com.newyorkis.util.Parser.DoubleParser
import scala.collection.mutable.ArrayBuffer

object LatLongUtil {
  implicit object LatLongParser extends Parser[LatLong] {
    override def apply(x: String): Option[LatLong] = {
      x.split(",") match {
        case Array(DoubleParser(lat), DoubleParser(lng)) => Some(LatLong(lat, lng))
        case _ => None
      }
    }
  }

  def distance(x: LatLong, y: LatLong): Double = {
    val latDelta = y.lat - x.lat
    val lngDelta = y.long - x.long

    math.sqrt(latDelta*latDelta + lngDelta*lngDelta)
  }

  def near(reference: LatLong): Ordering[LatLong] = new Ordering[LatLong] {
    override def compare(x: LatLong, y: LatLong): Int = {
      val distX = distance(reference, x)
      val distY = distance(reference, y)
      distX compareTo distY
    }
  }
}