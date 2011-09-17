package com.newyorkis

import com.newyorkis.rest.{RestApiFilter, RestApiRouter}
import com.newyorkis.service.{AuthenticationService, PingService}
import com.newyorkis.foursquare.{FoursquareApi, FoursquareAuthenticationApi}

import com.twitter.finagle.http.Http
import com.twitter.finagle.builder.ServerBuilder
import com.twitter.finagle.Service
import com.twitter.finagle.stats.OstrichStatsReceiver
import java.net.InetSocketAddress
import com.twitter.ostrich.admin.RuntimeEnvironment
import com.twitter.ostrich.admin.config.{AdminServiceConfig, StatsConfig, TimeSeriesCollectorConfig}

object Server {
  def main(args: Array[String]): Unit = {
    println("Starting new-york-near finagle server...")

    val config = (Option(System.getProperty("new-york-is.config")).map(Config.fromFile).getOrElse(DevConfig))

    val ostrichAdmin = new AdminServiceConfig {
      httpPort = 22557
      statsNodes = new StatsConfig {
        reporters = new TimeSeriesCollectorConfig
      }
    }
    val runtime = RuntimeEnvironment(this, Array())
    // TODO(paul) enable ostrich support
    //val ostrich = ostrichAdmin()(runtime)

    val restFilter = new RestApiFilter
    val foursquare = new FoursquareApi
    val foursquareAuth = new FoursquareAuthenticationApi(config.foursquare)
    val authService = new AuthenticationService(foursquareAuth, foursquare)
    val pingService = new PingService()

    val service = restFilter andThen RestApiRouter {
      case "auth" :: _ => authService
      case "ping" :: _ => pingService
    }

    val server =
      (ServerBuilder()
        .codec(Http())
        .bindTo(new InetSocketAddress(8080))
        .reportTo(new OstrichStatsReceiver)
        .name("new-york-is")
        .build(service))
  }
}
