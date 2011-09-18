package com.newyorkis
package model

import com.newyorkis.util.{CategoryUtil, MongoPoint}
import com.newyorkis.foursquare.CheckinDetail

import org.bson.types.ObjectId
import net.liftweb.mongodb.record.field.{ObjectIdField, MongoListField}
import net.liftweb.record.field.{StringField, UniqueIdField, IntField}
import net.liftweb.mongodb.record.{MongoRecord, MongoMetaRecord, MongoId}

class PushCheckin extends MongoRecord[PushCheckin] with MongoId[PushCheckin] {
  def meta = PushCheckin

  object userId extends StringField(this, 100)
  object venuename extends StringField(this, 100)
  object crossStreet extends StringField(this, 100)
  object latlng extends MongoPoint(this)
  object categories extends MongoListField[PushCheckin, String](this)
}

object PushCheckin extends PushCheckin with MongoMetaRecord[PushCheckin] {
  def fromCheckinDetail(user: User)(checkinDetail: CheckinDetail): Option[PushCheckin] = {
    for (venue <- checkinDetail.venue) yield {
      (PushCheckin
        .createRecord
        ._id(new ObjectId(checkinDetail.id))
        .userId(user.foursquareId.value)
        .venuename(venue.name)
        .crossStreet(venue.location.crossStreet)
        .latlng(LatLong(venue.location.lat, venue.location.lng))
        .categories(CategoryUtil.categoriesForCheckin(checkinDetail)))
    }
  }
}