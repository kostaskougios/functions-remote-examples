package example
import commands.ls.LsFunctions
import commands.model.{LsFile, LsOptions, LsResult}
import commands.ls.LsFunctionsReceiverFactory
import functions.fibers.FiberExecutor
import functions.sockets.FiberSocketServer

import scala.util.Using

@main def socketServer(): Unit =
  val Port       = 8080
  // This invoke map supports calls with both avro & json serialization as per the sbt config
  val invokerMap = LsFunctionsReceiverFactory.invokerMap(new LsFunctionsImpl)

  Using.resource(FiberExecutor()): executor =>
    Using.resource(FiberSocketServer.startServer(Port, invokerMap, executor)): server =>
      println(s"Server listening on port $Port")
      while (true)
        Thread.sleep(1000)
        val total = server.stats.totalRequestCount
        println(
          s"Total requests: $total, activeSocketConnections: ${server.stats.activeConnectionsCount}"
        )

class LsFunctionsImpl extends LsFunctions:
  override def fileSize(path: String): Long                     = path.length * 1000
  override def ls(path: String, lsOptions: LsOptions): LsResult = LsResult(Seq(LsFile(path + "/file1"), LsFile(path + "/file2")))
