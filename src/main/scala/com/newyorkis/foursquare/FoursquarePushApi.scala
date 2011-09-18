package com.newyorkis.foursquare

case class CheckinDetail(id: String, createdAt: Long, venue: Option[VenueDetail])
case class VenueDetail(id: String, name: Option[String], contact: Option[String], location: VenueLocation, categories: List[Category])
case class VenueLocation(address: Option[String], crossStreet: Option[String], city: String, state: String, postalCode: Option[String], country: Option[String], lat: Double, lng: Double)
case class Category(name: String, pluralName: String, icon: String, categories: Option[List[Category]], parents: Option[List[String]])