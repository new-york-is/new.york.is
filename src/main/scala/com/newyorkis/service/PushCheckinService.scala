package com.newyorkis.service

import com.newyorkis.foursquare.CheckinDetail
import com.newyorkis.rest.{RestApiService, RestApiRequest, RestApiResponse}
import com.newyorkis.model.{Db, PushCheckin, User}

import com.twitter.util.Future
import net.liftweb.json.DefaultFormats
import net.liftweb.json.JsonAST.{JObject, JField}
import net.liftweb.json.{Printer, JsonAST, JsonParser}
import org.bson.types.ObjectId
import org.jboss.netty.util.CharsetUtil.UTF_8

case class PushCheckinPost(id: String)

class PushCheckinService(db: Db) extends RestApiService {
  implicit val formats = DefaultFormats

  override def get(request: RestApiRequest) = {
    val secret = request.params.required[String]("secret")
    val id = request.params.required[ObjectId]("checkinId")

    Future.value(new RestApiResponse(JObject(List())))
  }

  override def post(request: RestApiRequest) = {
    println(">>REQUEST>> " + request.underlying.getContent.toString(UTF_8))
    val param = request.params.required[String]("checkin")
    val json = JsonParser.parse(param)

    println(">>JSON>> " + request)
    val response = json.extract[PushCheckinPost]
    println(">>RESPONSE>> " + response)

    Future.value(new RestApiResponse(JObject(List())))
  }
}