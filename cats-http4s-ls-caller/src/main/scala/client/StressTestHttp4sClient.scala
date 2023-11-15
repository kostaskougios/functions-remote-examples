package client

import cats.Parallel
import cats.effect.*
import cats.implicits.*
import commands.ls.StressTestFunctionsCallerFactory
import fs2.io.net.Network
import org.http4s.*
import org.http4s.ember.client.EmberClientBuilder
import org.http4s.implicits.*

object StressTestHttp4sClient extends IOApp.Simple:
  val run = runClient[IO]

  def runClient[F[_]: Async: Network: Parallel]: F[Unit] =
    for results <- EmberClientBuilder
        .default[F]
        .build
        .use: client =>
          val serverUri  = uri"http://localhost:8081"
          // We can call the functions with both json and avro serialization
          val avroCaller = StressTestFunctionsCallerFactory.newHttp4sAvroStressTestFunctions(client, serverUri)
          val calls      = (1 to 1000000)
            .grouped(10000)
            .map: g =>
              g.toList
                .map: i =>
                  avroCaller
                    .add(i, 2)()
                    .map: r =>
                      if r != i + 2 then throw new IllegalStateException(s"Received incorrect result for call $i, incorrect value = $r")
                      r
                .parTraverse(identity)

          for r <- calls.toList.sequence
          yield r.map(_.size).sum
    yield println(s"Total requests: $results")
