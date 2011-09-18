package com.newyorkis.service

import com.newyorkis.rest.{RestApiService, RestApiRequest, RestApiResponse}

import com.twitter.util.Future
import net.liftweb.json.JsonAST.{JObject, JField}
import net.liftweb.json.{Printer, JsonAST, JsonParser}
import org.jboss.netty.util.CharsetUtil.UTF_8

class PushService extends RestApiService {
  override def post(request: RestApiRequest) = {
    val json = JsonParser.parse(request.underlying.getContent.toString(UTF_8))

    Future.value(new RestApiResponse(JObject(List())))
  }
}