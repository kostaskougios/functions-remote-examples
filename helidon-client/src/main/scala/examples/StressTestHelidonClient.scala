package examples

import examples.helidon.StressTestFunctionsCallerFactory
import functions.helidon.transport.HelidonTransport
import io.helidon.webclient.api.WebClient

import java.io.UncheckedIOException

@main def stressTestHelidonClient(): Unit =
  val client = WebClient
    .builder()
    .baseUri(s"http://localhost:8081")
    .build()

  val transport = new HelidonTransport(client)

  val avroFunctions = StressTestFunctionsCallerFactory.newHelidonAvroStressTestFunctions(transport)
  for i <- 1 to 1_000_000 do
    try
      val r = avroFunctions.add(i, 5)()
      if r != i + 5 then throw new IllegalStateException(s"Invalid response for add($i,5), response = $r")
    catch case e: UncheckedIOException => e.printStackTrace()
