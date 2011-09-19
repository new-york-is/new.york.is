package com.newyorkis
package model

import com.newyorkis.service.{PushCheckinPost}
import com.newyorkis.util.{CategoryUtil, MongoPoint}
import com.newyorkis.service.{VenueDetail, UserDetail}

import org.bson.types.ObjectId
import net.liftweb.mongodb.record.field.{ObjectIdField, MongoListField}
import net.liftweb.record.field.{StringField, UniqueIdField, IntField}
import net.liftweb.mongodb.record.{MongoRecord, MongoMetaRecord, MongoId}

class PushCheckin extends MongoRecord[PushCheckin] with MongoId[PushCheckin] {
  def meta = PushCheckin

  object userId extends StringField(this, 100)
  object firstName extends StringField(this, 100)
  object photo extends StringField(this, 100)
  object venuename extends StringField(this, 100)
}

object PushCheckin extends PushCheckin with MongoMetaRecord[PushCheckin] {
  def fromPushCheckinPost(user: UserDetail)(pushCheckinPost: PushCheckinPost): PushCheckin = {
    PushCheckin
      .createRecord
      ._id(new ObjectId(pushCheckinPost.id))
      .userId(user.id)
      .firstName(user.firstName)
      .photo(user.photo)
      .venuename(pushCheckinPost.venue.name)
  }
}