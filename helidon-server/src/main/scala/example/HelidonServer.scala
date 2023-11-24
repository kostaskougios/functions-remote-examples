package example

import examples.helidon.model.*
import examples.helidon.{HelidonFunctions, HelidonFunctionsReceiverFactory}
import io.helidon.logging.common.LogConfig
import io.helidon.webserver.WebServer
import io.helidon.webserver.http.HttpRouting

@main def helidonServer(): Unit =
  // our exported functions impl, see the code at the end of this file
  val impl = new HelidonFunctionsImpl

  // configure the helidon server routes using the generated classes
  val routesBuilder = HttpRouting.builder()
  // add the routes for the avro serialization
  val avroRoutes    = HelidonFunctionsReceiverFactory.newAvroHelidonFunctionsHelidonRoutes(impl)
  avroRoutes.routes(routesBuilder)
  // add the same routes but with json serialization
  val jsonRoutes    = HelidonFunctionsReceiverFactory.newJsonHelidonFunctionsHelidonRoutes(impl)
  jsonRoutes.routes(routesBuilder)

  // now proceed with the standard helidon configuration
  LogConfig.configureRuntime()
  val server = WebServer.builder.port(8080).routing(routesBuilder).build.start
  println("Serving requests at http://localhost:8080")
  Thread.sleep(84600 * 1000)
  server.stop()

class HelidonFunctionsImpl extends HelidonFunctions:
  override def ls(path: String)(lsOptions: LsOptions)                          = LsResult(Seq(LsFile(path + "/file1"), LsFile(path + "/file2")))
  override def fileSize(path: String)()                                        = path.length * 1000
  override def deleteAllWithFileSizeLessThan(dir: String, minFileSize: Long)() =
    Seq(LsFile(s"$dir/f1"), LsFile(s"$dir/f2"))
