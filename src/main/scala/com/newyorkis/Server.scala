package com.newyorkis

import com.newyorkis.model.MongoDb
import com.newyorkis.rest.{RestApiFilter, RestApiRouter}
import com.newyorkis.service.{AuthenticationService, PingService, PushCheckinService}
import com.newyorkis.foursquare.{FoursquareVenuePushApi, FoursquareAuthenticationApi}

import com.mongodb.Mongo
import com.twitter.finagle.builder.ServerBuilder
import com.twitter.finagle.http.Http
import com.twitter.finagle.stats.OstrichStatsReceiver
import com.twitter.ostrich.admin.RuntimeEnvironment
import com.twitter.ostrich.admin.config.{AdminServiceConfig, StatsConfig, TimeSeriesCollectorConfig}
import com.twitter.util.FuturePool
import java.net.InetSocketAddress
import java.util.concurrent.Executors
import net.liftweb.mongodb.{DefaultMongoIdentifier, MongoDB, MongoAddress, MongoHostBase}

object Server {
  def main(args: Array[String]): Unit = {
    println("Starting new-york-near finagle server...")

    val config = (Option(System.getProperty("newyorkis.config")).map(Config.fromFile).getOrElse(DevConfig))

    val _mongo = new Mongo
    val address = MongoAddress(new MongoHostBase { def mongo = _mongo }, config.mongo.name)
    MongoDB.defineDb(DefaultMongoIdentifier, address)
    val mongoPool = Executors.newFixedThreadPool(4)
    val db = new MongoDb(FuturePool(mongoPool))

    val ostrichAdmin = new AdminServiceConfig {
      httpPort = 22557
      statsNodes = new StatsConfig {
        reporters = new TimeSeriesCollectorConfig
      }
    }
    val runtime = RuntimeEnvironment(this, Array())
    val ostrich = ostrichAdmin()(runtime)

    val restFilter = new RestApiFilter
    val foursquare = new FoursquareVenuePushApi
    val foursquareAuth = new FoursquareAuthenticationApi(config.foursquare)
    val authService = new AuthenticationService(foursquareAuth, foursquare, db)
    val pingService = new PingService()
    val pushCheckinService = new PushCheckinService(db)

    val service = restFilter andThen RestApiRouter {
      case "auth" :: _ => authService
      case "ping" :: Nil => pingService
      case "push" :: Nil => pushCheckinService
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
