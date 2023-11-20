# Sockets and fibers for api's that don't require http

This is best for 
- Very simple code, no IO frameworks, where calls seem synchronous but there is no performance penalty for waiting the results from the server due to the fibers of jdk 21+. Tests on a macbook pro M1 that runs both client and server had 188k requests/sec throughput for a simple add(a,b):Int function.
- Scala api's that talk to each other and don't require http to do so.
- Performance is important, i.e. 10x more throughput than http4s because there is no http-protocol overhead.

We will create a server that can serve calls to the `LsFunctions` trait with both avro and json serialization. The exported functions:

```scala
trait LsFunctions:
  def ls(path: String, lsOptions: LsOptions = LsOptions.Defaults): LsResult
  def fileSize(path: String): Long
```

Start by looking at 
- [bin/build-sockets-and-fibers](../bin/build-sockets-and-fibers) script and it's comments.
- [build.sbt](../build.sbt) , "fiber & sockets examples" section
- [The server](../ls-fiber-sockets-server/src/main/scala/example/SocketsServer.scala)
- [The client](../ls-fiber-sockets-client/src/main/scala/example/SocketClient.scala)

