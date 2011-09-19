package com.newyorkis.service

import com.newyorkis.rest.{RestApiService, RestApiRequest, RestApiResponse}
import com.newyorkis.model.{Db, PushCheckin, User}

import java.net.URLDecoder
import com.twitter.util.Future
import net.liftweb.json.DefaultFormats
import net.liftweb.json.Extraction.decompose
import net.liftweb.json.JsonAST.{JObject, JField, JString, JArray}
import net.liftweb.json.{Printer, JsonAST, JsonParser}
import net.liftweb.http.provider.HTTPParam
import org.bson.types.ObjectId
import org.jboss.netty.util.CharsetUtil.UTF_8

case class PushCheckinPost(id: String, venue: VenueDetail)
case class UserDetail(id: String, firstName: String, photo: Option[String])
case class CheckinDetail(id: String, createdAt: Long, venue: Option[VenueDetail])
case class VenueDetail(id: String, name: Option[String], contact: Option[String], location: VenueLocation, categories: List[Category])
case class VenueLocation(address: Option[String], crossStreet: Option[String], city: String, state: String, postalCode: Option[String], country: Option[String], lat: Double, lng: Double)
case class Category(name: String, pluralName: String, icon: String, categories: Option[List[Category]], parents: Option[List[String]])

case class PushResult(id: String, firstName: String, userId: String, photo: String, venuename: String )

class PushCheckinService(db: Db) extends RestApiService {
  implicit val formats = DefaultFormats

  override def get(request: RestApiRequest) = {
    val secret = request.params.required[String]("secret")

    for {
      checkin <- db.fetchOne(PushCheckin.orderDesc(_._id))
    } yield {
      val fields = {
          List(
            JField("id", JString(checkin.id.toString())),
            JField("firstName", JString(checkin.firstName.toString())),
            JField("userId", JString(checkin.userId.toString())),
            JField("photo", JString(checkin.photo.toString())),
            JField("venuename", JString(checkin.venuename.toString())))
      }
      new RestApiResponse(JObject(List(JField("response", JArray(fields)))))
    }
  }

  override def post(request: RestApiRequest) = {
    val checkin = request.underlying.getContent.toString(UTF_8).split("&user=").apply(0).substring(8)
    val user = request.underlying.getContent.toString(UTF_8).split("&user=").apply(1)

    val checkinJson = JsonParser.parse(URLDecoder.decode(checkin, "UTF-8"))
    val userJson = JsonParser.parse(URLDecoder.decode(user, "UTF-8"))

    val checkinResponse = checkinJson.extract[PushCheckinPost]
    val userResponse = userJson.extract[UserDetail]

    val record = PushCheckin.fromPushCheckinPost(userResponse)(checkinResponse)
    db.save(record)
    Future.value(new RestApiResponse(JObject(List())))
  }
}