package examples

import examples.helidon.{HelidonFunctions, HelidonFunctionsCallerFactory}
import examples.helidon.model.LsOptions
import functions.helidon.transport.HelidonTransport
import io.helidon.webclient.api.WebClient

@main def helidonClient(): Unit =
  // Create a normal WebClient (v4 so that we take advantage of fibers on jdk21, helidon-webclient-http2 artifact)
  val client = WebClient
    .builder()
    .baseUri(s"http://localhost:8080")
    .build()

  // We need a helidon transport which will send/receive arrays of bytes.
  val transport = new HelidonTransport(client)

  // Use the generated HelidonFunctionsCallerFactory to get instances of our exported trait.
  println("We can call the functions using avro serialization")
  val avroFunctions: HelidonFunctions = HelidonFunctionsCallerFactory.newHelidonAvroHelidonFunctions(transport)
  // will do a POST request (configured as a comment at the HelidonFunctions trait) to the server. The convention is :
  // - the first param list , "/tmp" in this example, becomes part of the url
  // - the second param list, LsOptions in this example, is avro-serialized and send as the body of the request.
  println(avroFunctions.ls("/tmp")(LsOptions(includeDirs = true))) // will print: LsResult(List(LsFile(tmp/file1), LsFile(tmp/file2)))

  // will do a GET request with /tmp as part of the url.
  println(avroFunctions.fileSize("/tmp")()) // will print: 3000

  // will do a DELETE request
  println(avroFunctions.deleteAllWithFileSizeLessThan("/tmp", 500)()) // will print: List(LsFile(tmp/f1), LsFile(tmp/f2))

  println("We can call the functions using json serialization")
  val jsonFunctions: HelidonFunctions = HelidonFunctionsCallerFactory.newHelidonJsonHelidonFunctions(transport)
  println(jsonFunctions.ls("/tmp")())
