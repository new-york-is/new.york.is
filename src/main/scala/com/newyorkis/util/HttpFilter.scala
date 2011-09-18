package com.newyorkis.util

import com.twitter.finagle.Filter
import org.jboss.netty.handler.codec.http.{HttpRequest, HttpResponse}

abstract class HttpFilter[+Req, -Rep] extends Filter[HttpRequest, HttpResponse, Req, Rep] { }