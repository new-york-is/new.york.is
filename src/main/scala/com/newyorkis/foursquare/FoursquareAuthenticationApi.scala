package com.newyorkis.foursquare

import com.newyorkis.FoursquarePushConfig
import com.newyorkis.util.JsonApiClient
import com.newyorkis.util.RichFuture

import com.twitter.util.Future
import net.liftweb.json.JsonAST.{JObject, JField, JString}

class FoursquareAuthenticationApi(val config: FoursquarePushConfig) extends JsonApiClient("foursquare.com", 443) {
  override def clientBuilder = super.clientBuilder.tls("foursquare.com")

  implicit def RichFuture[A](future: Future[A]): RichFuture[A] = new RichFuture(future)

  def auth(code: String): Future[String] = {
    val endpoint = "/oauth2/access_token"
    val params =
      Map(
        "client_id" -> config.clientId,
        "client_secret" -> config.clientSecret,
        "grant_type" -> "authorization_code",
        "redirect_uri" -> config.callbackUrl,
        "code" -> code)
    get(endpoint, params) collect {
      case JObject(List(JField("access_token", JString(accessToken)))) =>
        accessToken
    }
  }
}