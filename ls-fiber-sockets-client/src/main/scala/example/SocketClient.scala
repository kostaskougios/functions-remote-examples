package example

import commands.ls.{LsFunctions, LsFunctionsCallerFactory}
import functions.fibers.FiberExecutor
import functions.sockets.{SocketPool, SocketTransport}

import scala.util.Using

@main def socketClient(): Unit =
  val Port = 8080
  // We need to create the client socket pool and for that we also need a fiber executor.
  Using.resource(FiberExecutor()): executor =>
    Using.resource(SocketPool("localhost", Port, executor)): pool =>
      // Now we need to create the transport that carries arrays of bytes over the socket
      val transport = SocketTransport(pool)
      // ok now ready to create functions that will do calls remotely.

      // The one with avro serialization
      val lsA: LsFunctions = LsFunctionsCallerFactory.newAvroLsFunctions(transport.transportFunction)
      // We will call the function which will result of all arguments avro-serialized and send over a socket
      // to the server which will respond with the results
      println(lsA.ls("/tmp"))

      // The same but with json serialization
      val lsJ: LsFunctions = LsFunctionsCallerFactory.newJsonLsFunctions(transport.transportFunction)
      // We will call the function which will result of all arguments json-serialized and send over a socket
      // to the server which will respond with the results
      println(lsJ.ls("/tmp"))
