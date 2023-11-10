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
    for results <- EmberClientBuilder
        .default[F]
        .build
        .use: client =>
          val serverUri  = uri"http://localhost:8080"
          // We can call the functions with both json and avro serialization
          val jsonCaller = LsFunctionsCallerFactory.newHttp4sJsonLsFunctions(client, serverUri)
          val avroCaller = LsFunctionsCallerFactory.newHttp4sAvroLsFunctions(client, serverUri)

          for
            r1 <- jsonCaller.ls("/tmp/some-dir1")()
            r2 <- avroCaller.ls("/tmp/some-dir2")()
            r3 <- avroCaller.deleteAllWithFileSizeLessThan("/tmp/test", 8192)()
          yield (r1, r2, r3)
    yield println(results)
