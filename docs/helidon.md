# Helidon web server and client on jdk21's fibers 

This is best for
- Simplicity in the code base, no IO frameworks, where calls seem synchronous but there is no performance penalty for waiting the results from the server due to the fibers of jdk 21+.
- Creating rest api's
- Performance is important but still http is required.

We will create a web server that can serve calls to the `HelidonFunctions` trait with both avro and json serialization. The exported functions:

[HelidonFunctions](../helidon-exports/src/main/scala/examples/helidon/HelidonFunctions.scala)

Start by looking at
- [bin/build-helidon](../bin/build-helidon) script and it's comments.
- [build.sbt](../build.sbt) , "helidon" section
- [The server](../helidon-server/src/main/scala/example/HelidonServer.scala)
- [The client](../helidon-client/src/main/scala/examples/HelidonClient.scala)

