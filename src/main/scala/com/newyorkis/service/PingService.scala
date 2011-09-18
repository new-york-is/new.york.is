package com.newyorkis.service

import com.newyorkis.rest.{RestApiService, RestApiRequest, RestApiResponse}
import com.twitter.util.Future
import net.liftweb.json.JsonAST.{JValue, JObject, JField, JString}

class PingService extends RestApiService {
  override def get(request: RestApiRequest) =
    Future.value(new RestApiResponse(JObject(List(JField("status", JString("ok"))))))
}