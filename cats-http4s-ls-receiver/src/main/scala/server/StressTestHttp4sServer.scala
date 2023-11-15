package server

import cats.effect.kernel.Async
import cats.effect.{Async, IO, IOApp}
import com.comcast.ip4s.*
import commands.ls.{SimpleFunctions, SimpleFunctionsReceiverFactory}
import fs2.io.net.Network
import org.http4s.HttpRoutes
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.implicits.*
import org.http4s.server.middleware.Logger

import java.util.concurrent.atomic.AtomicLong
import scala.concurrent.Future
import concurrent.ExecutionContext.Implicits.global

object StressTestHttp4sServer extends IOApp.Simple:
  val run = newServer[IO](port"8081").useForever

  def newServer[F[_]: Async: Network](port: Port) =
    val impl         = new SimpleFunctionsImpl
    Future {
      while true do
        Thread.sleep(1000)
        println(s"Total requests: ${impl.callsCount}")
    }
    val routesAvro   = SimpleFunctionsReceiverFactory.newAvroSimpleFunctionsRoutes(impl)
    val routes       = HttpRoutes.of(routesAvro.allRoutes)
    val finalHttpApp = Logger.httpApp(true, true)(routes.orNotFound)

    EmberServerBuilder.default
      .withHost(ipv4"0.0.0.0")
      .withPort(port)
      .withHttpApp(finalHttpApp)
      .build

class SimpleFunctionsImpl[F[_]: Async] extends SimpleFunctions[F]:
  private val A                              = Async[F]
  private val calls                          = new AtomicLong(0)
  def callsCount: Long                       = calls.get()
  override def add(a: Int, b: Int)(): F[Int] =
    calls.incrementAndGet()
    A.pure:
      a + b
