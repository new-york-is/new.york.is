package com.newyorkis.foursquare

import com.newyorkis.util.JsonApiClient

import net.liftweb.json.DefaultFormats

case class SelfApiResponse(response: SelfApiResponseBody)
case class SelfApiResponseBody(user: UserApiResponse)
case class UserApiResponse(id: String, firstName: String, lastName: String, contact: UserContactResponse, checkins: UserCheckinsResponse)
case class UserContactResponse(phone: Option[String], email: String, twitter: Option[String], facebook: Option[String])
case class UserCheckinsResponse(count: Int)

class FoursquareVenuePushApi {
  def authenticate(accessToken: String) = new AuthenticatedFoursquareVenuePushApi(accessToken)
}

class AuthenticatedFoursquareVenuePushApi(AccessToken: String) extends JsonApiClient("api.foursquare.com", 443) {
  override def clientBuilder = super.clientBuilder.tls("api.foursquare.com")

  implicit val formats = DefaultFormats

  def self = {
    val endpoint = "/v2/users/self"
    val params =
      Map(
        "oauth_token" -> AccessToken)
    get(endpoint, params).map(_.extract[SelfApiResponse])
  }
}