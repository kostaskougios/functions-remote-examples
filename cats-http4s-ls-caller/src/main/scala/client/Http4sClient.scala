package client
import cats.effect.*
import cats.syntax.all.*
import commands.ls.LsFunctionsCallerFactory
import fs2.io.net.Network
import org.http4s.*
import org.http4s.ember.client.EmberClientBuilder
import org.http4s.implicits.*

object Http4sClient extends IOApp.Simple:
  val run = runClient[IO]

  def runClient[F[_]: Async: Network]: F[Unit] =
    for x <- EmberClientBuilder
        .default[F]
        .build
        .use: client =>
          val serverUri  = uri"http://localhost:8080"
          // We can test both json and avro serialization
          val jsonCaller = LsFunctionsCallerFactory.newHttp4sJsonLsFunctions(client, serverUri)
          val avroCaller = LsFunctionsCallerFactory.newHttp4sAvroLsFunctions(client, serverUri)

          for
            r1 <- jsonCaller.ls("/tmp/json")
            r2 <- avroCaller.ls("/tmp/avro")
          yield (r1, r2)
    yield ()
