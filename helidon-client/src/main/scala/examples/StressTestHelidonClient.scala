package examples

import examples.helidon.StressTestFunctionsCallerFactory
import functions.helidon.transport.HelidonTransport
import io.helidon.webclient.api.WebClient

@main def stressTestHelidonClient(): Unit =
  val client = WebClient
    .builder()
    .baseUri(s"http://localhost:8081")
    .build()

  val transport = new HelidonTransport(client)

  val avroFunctions = StressTestFunctionsCallerFactory.newHelidonAvroStressTestFunctions(transport)
  for i <- 1 to 1_000_000 do
    val r = avroFunctions.add(i, 5)()
    if r != i + 5 then throw new IllegalStateException(s"Invalid response for add($i,5), response = $r")
