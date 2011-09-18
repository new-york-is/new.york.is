package com.newyorkis.util

import com.newyorkis.foursquare.CheckinDetail

object CategoryUtil {
  def categoriesForCheckin(checkin: CheckinDetail): List[String] = {
    (for {
      venue <- checkin.venue
      val categories = venue.categories
      val categoryNames = categories.map(_.name)
      val parentNames = categories.flatMap(_.parents).flatten
    } yield categoryNames ++ parentNames).flatten.toList.distinct
  }
}