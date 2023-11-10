# http4s integration

To integrate with http4s, all our functions should all return `F[X]`. Then the code generation will generate
the json and avro serializers and also a transport for http4s. Also it will generate the routes for the server.

`HTTP-GET` and the rest of the `HTTP` methods are supported. Also url params are supported via methods with 2 sets
of parameters. The 1st set always is converted to url parameters and the second one is serialized as the body
of the request. See LsFunctions below for more details.

Start by looking at the exported functions: [LsFunctions](../cats-ls-exports/src/main/scala/commands/ls/LsFunctions.scala) which descibes the api and defines which methods will be `GET`, `POST`, `DELETE` etc.


Then follow the build script [build-cats-effects-http4s-receiver-and-caller](../bin/build-cats-effects-http4s-receiver-and-caller)
Then maybe look how the functions-remote-sbt-plugin is configured at [`build.sbt`](../build.sbt)

and also examine the code in these projects: 
- `cats-ls-exports` , i.e. [LsFunctions](../cats-ls-exports/src/main/scala/commands/ls/LsFunctions.scala)
- `cats-http4s-ls-receiver` i.e. [Http4sServer](../cats-http4s-ls-receiver/src/main/scala/server/Http4sServer.scala)
- `cats-http4s-ls-caller` i.e. [Http4sClient](../cats-http4s-ls-caller/src/main/scala/client/Http4sClient.scala)
