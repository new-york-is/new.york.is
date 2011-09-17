package com.newyorkis

import com.twitter.util.Eval
import java.io.File

object Config {
  def fromFile(fileName: String): Config = {
    val eval = new Eval
    eval[Config](new File(fileName))
  }
}

case class Config(foursquare: FoursquarePushConfig)

case class FoursquarePushConfig(clientId: String, clientSecret: String, callbackUrl: String, downloadUrl: String, pushUrl: String)

object DevConfig extends Config(
  FoursquarePushConfig(clientId = "L2Y45KQZ5ZC1N2QCZI11ZOOZPPXO4SSKK2GLKU2WKX2FXDUI",
  clientSecret = "A12OXNUK0RO3HYKDTH51LOI002DDYEGBFNV3I40BEI5IWI4G",
  callbackUrl = "http://localhost:8080/auth/callback",
  downloadUrl = "https://choosenear.me",
  pushUrl = "https://choosenear.me"))