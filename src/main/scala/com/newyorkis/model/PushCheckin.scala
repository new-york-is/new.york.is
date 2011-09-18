package com.newyorkis
package model

import com.newyorkis.service.PushCheckinPost
import com.newyorkis.util.{CategoryUtil, MongoPoint}
import com.newyorkis.foursquare.CheckinDetail

import org.bson.types.ObjectId
import net.liftweb.mongodb.record.field.{ObjectIdField, MongoListField}
import net.liftweb.record.field.{StringField, UniqueIdField, IntField}
import net.liftweb.mongodb.record.{MongoRecord, MongoMetaRecord, MongoId}

class PushCheckin extends MongoRecord[PushCheckin] with MongoId[PushCheckin] {
  def meta = PushCheckin

  object venuename extends StringField(this, 100)
}

object PushCheckin extends PushCheckin with MongoMetaRecord[PushCheckin] {
  def fromCheckinDetail(checkinDetail: CheckinDetail): Option[PushCheckin] = {
    for (venue <- checkinDetail.venue) yield {
      (PushCheckin
        .createRecord
        ._id(new ObjectId(checkinDetail.id))
        .venuename(venue.name))
    }
  }
}