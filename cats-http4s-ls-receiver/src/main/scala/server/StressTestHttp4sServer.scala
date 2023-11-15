package server

import cats.effect.kernel.Async
import cats.effect.{Async, IO, IOApp}
import com.comcast.ip4s.*
import commands.ls.{StressTestFunctions, StressTestFunctionsReceiverFactory}
import fs2.io.net.Network
import org.http4s.HttpRoutes
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.implicits.*
import org.http4s.server.middleware.Logger

import java.util.concurrent.atomic.AtomicLong
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object StressTestHttp4sServer extends IOApp.Simple:
  val run = newServer[IO](port"8081").useForever

  def newServer[F[_]: Async: Network](port: Port) =
    val impl         = new StressTestFunctionsImpl
    Future {
      while true do
        val startReqCount = impl.callsCount
        Thread.sleep(1000)
        val reqCount      = impl.callsCount
        println(s"Total requests: $reqCount, per second : ${reqCount - startReqCount}")
    }
    val routesAvro   = StressTestFunctionsReceiverFactory.newAvroStressTestFunctionsRoutes(impl)
    val routes       = HttpRoutes.of(routesAvro.allRoutes)
    val finalHttpApp = Logger.httpApp(true, true)(routes.orNotFound)

    EmberServerBuilder.default
      .withHost(ipv4"0.0.0.0")
      .withPort(port)
      .withHttpApp(finalHttpApp)
      .build

class StressTestFunctionsImpl[F[_]: Async] extends StressTestFunctions[F]:
  private val A                              = Async[F]
  private val calls                          = new AtomicLong(0)
  def callsCount: Long                       = calls.get()
  override def add(a: Int, b: Int)(): F[Int] =
    calls.incrementAndGet()
    A.pure:
      a + b
