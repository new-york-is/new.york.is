package com.newyorkis.service

import com.newyorkis.model.{Db, User}
import com.newyorkis.rest.{RestApiRequest, RestApiResponse, RestApiService, RestApiException, RestApiNotFoundException}
import com.newyorkis.foursquare.{FoursquareVenuePushApi, FoursquareAuthenticationApi}

import com.twitter.finagle.Service
import com.twitter.util.Future
import net.liftweb.json.DefaultFormats
import net.liftweb.json.Extraction.decompose
import net.liftweb.json.JsonAST.{JValue, JObject, JField, JString}
import org.jboss.netty.buffer.ChannelBuffers.copiedBuffer
import org.jboss.netty.handler.codec.http.{HttpResponse, HttpRequest, DefaultHttpResponse, QueryStringEncoder}
import org.jboss.netty.handler.codec.http.HttpResponseStatus.{BAD_REQUEST, FOUND}
import org.jboss.netty.handler.codec.http.HttpVersion.HTTP_1_1
import org.jboss.netty.util.CharsetUtil.UTF_8

class AuthenticationService(authApi: FoursquareAuthenticationApi, fsqApi: FoursquareVenuePushApi, db: Db) extends RestApiService {
  def redirectTo(uri: String): Future[RestApiResponse] = {
    Future.exception(new RestApiException("", FOUND) {
      override def postProcess(response: HttpResponse): Unit = {
        response.addHeader("Location", uri)
      }
    })
  }

  override def apply(request: RestApiRequest): Future[RestApiResponse] = {
    request.path match {
      case "auth" :: Nil =>
        val uri = {
          val encoder = new QueryStringEncoder("https://foursquare.com/oauth2/authenticate")
          encoder.addParam("client_id", authApi.config.clientId)
          encoder.addParam("response_type", "code")
          encoder.addParam("redirect_uri", authApi.config.callbackUrl)
          encoder.toString
        }
        redirectTo(uri)
      case "auth" :: "callback" :: Nil =>
        val code = request.params.required[String]("code")
        for {
          accessToken <- authApi.auth(code)
          userInfo <- fsqApi.authenticate(accessToken).self
          user <- (db.fetchOne(User.where(_.foursquareId eqs userInfo.response.user.id))
                     .rescue {
                       case ex: java.util.NoSuchElementException =>
                         db.save(
                           User
                             .createRecord
                             .foursquareId(userInfo.response.user.id)
                             .foursquareToken(accessToken)
                             .firstName(userInfo.response.user.firstName)
                             .lastName(userInfo.response.user.lastName)
                             .email(userInfo.response.user.contact.email)
                             .phone(userInfo.response.user.contact.phone))
                         })
          redirect <- redirectTo("http://107.20.173.39/?secret=" + user.secret.value)
        } yield {
          redirect
        }
      case _ =>
        Future.exception(RestApiNotFoundException)
    }
  }
}