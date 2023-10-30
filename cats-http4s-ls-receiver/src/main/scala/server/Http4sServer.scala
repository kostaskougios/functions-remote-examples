package server

import cats.effect.{Async, IO, IOApp}
import com.comcast.ip4s.*
import commands.ls.{LsFunctions, LsFunctionsReceiverFactory}
import commands.model.{LsFile, LsOptions, LsResult}
import fs2.io.net.Network
import org.http4s.HttpRoutes
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.implicits.*
import org.http4s.server.middleware.Logger

/** Note: the generated routes are better suited for backend to backend calls, all of them are via PUT requests with the body been the serialized function args.
  */
object Http4sServer extends IOApp.Simple:
  val run = newServer[IO](port"8080").useForever

  def newServer[F[_]: Async: Network](port: Port) =
    // The implementation of the exported trait
    val impl           = new LsFunctionsImpl
    // We want our server to respond to both json and avro requests
    val testRoutesJson = LsFunctionsReceiverFactory.newJsonLsFunctionsRoutes(impl)
    val testRoutesAvro = LsFunctionsReceiverFactory.newAvroLsFunctionsRoutes(impl)
    // So we setup both routes. Check the source code of the routes, they are by default
    // configured to i.e. commands.ls.LsFunctions / ls / Json but this can be overridden.
    val routes         = HttpRoutes.of(testRoutesJson.allRoutes orElse testRoutesAvro.allRoutes)
    val finalHttpApp   = Logger.httpApp(true, true)(routes.orNotFound)

    EmberServerBuilder.default
      .withHost(ipv4"0.0.0.0")
      .withPort(port)
      .withHttpApp(finalHttpApp)
      .build

class LsFunctionsImpl[F[_]: Async] extends LsFunctions[F]:
  private val A                                       = Async[F]
  override def ls(path: String, lsOptions: LsOptions) = A.pure(LsResult(Seq(LsFile(path + "/file1"), LsFile(path + "/file2"))))
  override def fileSize(path: String)                 = A.pure(path.length * 1000)
