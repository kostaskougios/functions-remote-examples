package client

import cats.effect.*
import cats.syntax.all.*
import commands.ls.SimpleFunctionsCallerFactory
import fs2.io.net.Network
import org.http4s.*
import org.http4s.ember.client.EmberClientBuilder
import org.http4s.implicits.*

object StressTestHttp4sClient extends IOApp.Simple:
  val run = runClient[IO]

  def runClient[F[_]: Async: Network]: F[Unit] =
    for results <- EmberClientBuilder
        .default[F]
        .build
        .use: client =>
          val serverUri  = uri"http://localhost:8081"
          // We can call the functions with both json and avro serialization
          val avroCaller = SimpleFunctionsCallerFactory.newHttp4sAvroSimpleFunctions(client, serverUri)

          for r2 <- avroCaller.add(1, 2)()
          yield r2
    yield println(results)
