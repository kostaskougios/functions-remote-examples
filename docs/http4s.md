# http4s integration

To integrate with http4s, all our functions should all return `F[X]`. Then the code generation will generate
the json and avro serializers and also a transport for http4s. Also it will generate the routes for the server.


Start by looking at the exported functions: [LsFunctions](../cats-ls-exports/src/main/scala/commands/ls/LsFunctions.scala).


Then follow the build script [build-cats-effects-http4s-receiver-and-caller](../bin/build-cats-effects-http4s-receiver-and-caller)
and also examine the code in the projects 
- cats-ls-exports , i.e. [LsFunctions](../cats-ls-exports/src/main/scala/commands/ls/LsFunctions.scala)
- cats-http4s-ls-receiver i.e. [Http4sServer](../cats-http4s-ls-receiver/src/main/scala/server/Http4sServer.scala)
- cats-http4s-ls-caller i.e. [Http4sClient](../cats-http4s-ls-caller/src/main/scala/client/Http4sClient.scala)
