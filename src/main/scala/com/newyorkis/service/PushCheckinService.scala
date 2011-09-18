package com.newyorkis.service

import com.newyorkis.foursquare.CheckinDetail
import com.newyorkis.rest.{RestApiService, RestApiRequest, RestApiResponse}
import com.newyorkis.model.{Db, PushCheckin, User}

import java.net.URLDecoder
import com.twitter.util.Future
import net.liftweb.json.DefaultFormats
import net.liftweb.json.JsonAST.{JObject, JField}
import net.liftweb.json.{Printer, JsonAST, JsonParser}
import net.liftweb.http.provider.HTTPParam
import org.bson.types.ObjectId
import org.jboss.netty.util.CharsetUtil.UTF_8

case class PushCheckinPost(id: String, checkin: CheckinDetail)

class PushCheckinService(db: Db) extends RestApiService {
  implicit val formats = DefaultFormats

  override def get(request: RestApiRequest) = {
    val secret = request.params.required[String]("secret")
    val id = request.params.required[ObjectId]("checkinId")

    Future.value(new RestApiResponse(JObject(List())))
  }

  override def post(request: RestApiRequest) = {
    val checkin = request.underlying.getContent.toString(UTF_8).split("&user=").apply(0).substring(8)
    val user = request.underlying.getContent.toString(UTF_8).split("&user=").apply(1)
    val checkinJson = JsonParser.parse(URLDecoder.decode(checkin, "UTF-8"))
    val userJson = JsonParser.parse(URLDecoder.decode(user, "UTF-8"))
    val response = checkinJson.extract[PushCheckinPost]

    println("\n>>RESPONSE>>\n" + response)
    Future.value(new RestApiResponse(JObject(List())))
  }
}