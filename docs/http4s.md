# http4s integration

To integrate with http4s, all our functions should all return `F[X]`. 
See [LsFunctions](../cats-ls-exports/src/main/scala/commands/ls/LsFunctions.scala).


Start by reading the comments in the script [build-cats-effects-http4s-receiver-and-caller](../bin/build-cats-effects-http4s-receiver-and-caller)
and also examine the code in the projects 
- cats-ls-exports , i.e. [LsFunctions](../cats-ls-exports/src/main/scala/commands/ls/LsFunctions.scala)
- cats-http4s-ls-receiver i.e. [Http4sServer](../cats-http4s-ls-receiver/src/main/scala/server/Http4sServer.scala)
- cats-http4s-ls-caller i.e. [Http4sClient](../cats-http4s-ls-caller/src/main/scala/client/Http4sClient.scala)
