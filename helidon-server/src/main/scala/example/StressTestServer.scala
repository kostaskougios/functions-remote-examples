package example

import examples.helidon.{StressTestFunctions, StressTestFunctionsReceiverFactory}
import io.helidon.logging.common.LogConfig
import io.helidon.webserver.WebServer
import io.helidon.webserver.http.HttpRouting

import java.util.concurrent.atomic.AtomicLong

@main def helidonStressTestServer(): Unit =
  val routesBuilder = HttpRouting.builder()
  val impl          = new StressTestFunctionsImpl

  val avroRoutes = StressTestFunctionsReceiverFactory.newAvroStressTestFunctionsHelidonRoutes(impl)
  avroRoutes.routes(routesBuilder)
  val jsonRoutes = StressTestFunctionsReceiverFactory.newJsonStressTestFunctionsHelidonRoutes(impl)
  jsonRoutes.routes(routesBuilder)

  LogConfig.configureRuntime()
  val server = WebServer.builder.port(8081).routing(routesBuilder).build.start
  println("Serving requests at http://localhost:8081")
  try
    while true do
      val startReqCount = impl.requestCount.get()
      Thread.sleep(1000)
      val reqCount      = impl.requestCount.get()
      println(s"Total requests: $reqCount, last second: ${reqCount - startReqCount} ")
  finally server.stop()

class StressTestFunctionsImpl extends StressTestFunctions:
  val requestCount                        = new AtomicLong(0)
  override def add(a: Int, b: Int)(): Int =
    requestCount.incrementAndGet()
    a + b
