package com

import com.newyorkis.util.LatLongUtil
import com.newyorkis.util.RichFuture

import com.foursquare.rogue.Rogue
import com.twitter.util.Future

package object newyorkis extends Rogue {
  type LatLong = com.foursquare.rogue.LatLong
  val LatLong = com.foursquare.rogue.LatLong

  implicit val LatLongParser = LatLongUtil.LatLongParser
  implicit def RichFuture[A](future: Future[A]): RichFuture[A] = new RichFuture(future)
}