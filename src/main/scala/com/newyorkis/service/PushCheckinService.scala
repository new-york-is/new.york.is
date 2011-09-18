package com.newyorkis.service

import com.newyorkis.foursquare.CheckinDetail
import com.newyorkis.rest.{RestApiService, RestApiRequest, RestApiResponse}
import com.newyorkis.model.{Db, PushCheckin, User}

import com.twitter.util.Future
import net.liftweb.json.DefaultFormats
import net.liftweb.json.JsonAST.{JObject, JField}
import net.liftweb.json.{Printer, JsonAST, JsonParser}
import org.jboss.netty.util.CharsetUtil.UTF_8

case class PushCheckinPost(checkin: CheckinDetail, user: PushCheckinPostUserDetail)
case class PushCheckinPostUserDetail(id: String)

class PushCheckinService(db: Db) extends RestApiService {
  implicit val formats = DefaultFormats

  override def post(request: RestApiRequest) = {
    val json = JsonParser.parse(request.underlying.getContent.toString(UTF_8))
    val response = json.extract[PushCheckinPost]
    val userId = response.user.id

    for {
      user <- db.fetchOne(User.where(_.foursquareId eqs userId))
      record <- PushCheckin.fromCheckinDetail(user)(response.checkin)
      _ <- db.save(record)
    } {}

    Future.value(new RestApiResponse(JObject(List())))
  }
}