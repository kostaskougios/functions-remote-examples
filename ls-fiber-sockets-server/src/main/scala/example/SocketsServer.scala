package example
import commands.ls.LsFunctions
import commands.model.{LsFile, LsOptions, LsResult}
import commands.ls.LsFunctionsReceiverFactory
import functions.fibers.FiberExecutor
import functions.sockets.FiberSocketServer

import scala.util.Using

/** The fiber-sockets server is by far the most scalable and performant and simple implementation. Java-fibers of project loom enable us to have non-IO code
  * that runs fibers and where await() is the preferred and most performant way of getting results. Our functions can return just the outcome of their
  * calculations without the need of an IO monad. And yet the code is the most performing and scalable of all.
  *
  * Also the sockets server is 10x more performant than i.e. http4s because it doesn't have the overhead of the http protocol.
  */
@main def socketServer(): Unit =
  val Port       = 8080
  // This invoke map supports calls with both avro & json serialization as per the sbt config.
  val invokerMap = LsFunctionsReceiverFactory.invokerMap(new LsFunctionsImpl)

  Using.resource(FiberExecutor()): executor =>
    Using.resource(FiberSocketServer.startServer(Port, invokerMap, executor)): server =>
      println(s"Server listening on port $Port")
      // Server is listening, now we'll go into a forever loop printing some stats every second
      while (true)
        Thread.sleep(1000)
        val total = server.stats.totalRequestCount
        println(
          s"Total requests: $total, activeSocketConnections: ${server.stats.activeConnectionsCount}"
        )

class LsFunctionsImpl extends LsFunctions:
  override def fileSize(path: String): Long                     = path.length * 1000
  override def ls(path: String, lsOptions: LsOptions): LsResult = LsResult(Seq(LsFile(path + "/file1"), LsFile(path + "/file2")))
