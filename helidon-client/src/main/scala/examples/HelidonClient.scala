package examples

import examples.helidon.HelidonFunctionsCallerFactory
import functions.helidon.transport.HelidonTransport
import io.helidon.webclient.api.WebClient

@main def helidonClient(): Unit =
  // Create a normal WebClient (v4 so that we take advantage of fibers on jdk21)
  val client = WebClient
    .builder()
    .baseUri(s"http://localhost:8080")
    .build()

  // We need a helidon transport which will send/receive arrays of bytes.
  val transport = new HelidonTransport(client)

  println("We can call the functions using avro serialization")
  val avroFunctions = HelidonFunctionsCallerFactory.newHelidonAvroHelidonFunctions(transport)
  println(avroFunctions.ls("/tmp")())
  println(avroFunctions.fileSize("/tmp")())
  println(avroFunctions.deleteAllWithFileSizeLessThan("/tmp", 500)())

  println("We can call the functions using json serialization")
  val jsonFunctions = HelidonFunctionsCallerFactory.newHelidonJsonHelidonFunctions(transport)
  println(jsonFunctions.ls("/tmp")())
