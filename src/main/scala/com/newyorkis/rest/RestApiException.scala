package com.newyorkis.rest

import org.jboss.netty.handler.codec.http.{HttpResponse, HttpResponseStatus}
import org.jboss.netty.handler.codec.http.HttpResponseStatus.{BAD_REQUEST, FOUND, NOT_FOUND}

case class RestApiException(message: String, status: HttpResponseStatus = BAD_REQUEST) extends RuntimeException(message) {
  def postProcess(response: HttpResponse): Unit = ()
}

object RestApiNotFoundException extends RestApiException("", NOT_FOUND)