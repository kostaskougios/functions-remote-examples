# functions-remote

See "Integrations" below for a list of integrations (http4s, sockets and fibers, kafka etc).

Note: Only for scala3. Currently, circe-json and avro4s serializations are supported.

Note: At the moment there are no artifacts published on maven repos. To run the examples locally do:

```shell
git clone git@github.com:kostaskougios/functions-remote-sbt-plugins.git
cd functions-remote-sbt-plugins
sbt publishLocal

cd ..
git clone git@github.com:kostaskougios/functions-remote.git
cd functions-remote
# ignore some compilation errors of tests modules, it is OK
sbt publishLocal
cd ..
```
and you are ready to go.



For scala, the most important ability of the language is the ability to call a function `f(x) = y` and get its results.
And while we can do that within a jvm instance, it would be useful to be able to call functions this easily across jvms that
are running in the same box or remote boxes.

Functions-remote is a code generator for calling functions "remotely", using different serialization methods (like json or avro), and different remote transports (like http).
Remotely means we may use http as a transport (i.e. via http4s), kafka where calling is publishing and subscribing is invoking the actual implementation of the function,
or just use an isolated classloader as transport so that we can
execute the function locally or maybe just spawn a new jvm to call the function. 
We'll see all these in more details below as well as why it can be useful to use different transports.

Effectively functions-remote allows the simplicity of `f(x) = y` no matter where `f` will really run. 

The generated code is very readable and as if written by a person.

There are many benefits using functions instead of the usual manual serialization of case classes. Apart not having to write
any serialization code, it is also a lot more readable and easier to mock in tests. Also easier to change where/how the call
will be done in the future if there is a need to do that.

## The exported functions

Let's say we have the following functionality that simulates the `ls` command line tool to list files in a directory:

```scala
trait LsFunctions:
  def ls(path: String, lsOptions: LsOptions = LsOptions.Defaults): LsResult
  def fileSize(path: String): Long
```

## The generated code

We can then use functions-remote to create code for us that:
- create case classes for the parameters of each method in the trait i.e. `case class Ls(path: String, lsOptions: LsOptions)`
- create serializers for json, avro etc so that we can serialize/deserialize `Ls` and `LsResult`
- create a `class LsFunctionsCaller extends LsFunctions` which can be used to call remotely our LsFunctions and use any serialization method available. The `Caller` extends our trait and internally serializes the args to `Array[Byte]` and sends them to the `transport`.
- create a `transport` for http4s, or locally via a classloader (we'll see that later on) etc. The transport is an `f( coordinates , Array[Byte] ): Array[Byte]`
- create an `LsFunctionsReceiver` that receives the `Array[Byte]` and converts it to a call to `LsFunctionsImpl`

After the code generation, we can use the generated code to call the function "remotely" (note for http4s and cats-effects, see the http4s integration below):
```scala
val lsFunctions: LsFunctions = builder.newAvroLsFunctions
val result: LsResult         = lsFunctions.ls("/tmp")
```

## Flow of data during a call

A call to a function is done via a generated `Caller` class, serialized, transferred to the `Receiver` via a `transport` and the `Receiver` takes 
care of actually calling the function implementation.

Our code invokes `LsFunctions.ls(... args ...)` ➡️ args are copied to the generated `Ls` case class ➡️ `Ls` is serialized to `Array[Byte]` ➡️ the transport is used to transfer the bytes to the `Receiver` ➡️ on the `Receiver` side we deserialize `Ls` and use it to invoke the actual `LsFunctionsImpl` ➡️ `LsResult` is serialized and send back to the caller

# Integrations

## plain sockets and fibers

This is by far the fastest (10x faster than http4s) option for api calls that don't require http. Also it doesn't
require any IO frameworks and doesn't block despite teh function call seemingly been synchronous. jdk21 or better only. 

[sockets and fibers](docs/sockets.md)

## http4s 

We can use the generator to integrate with http4s i.e. create routes for the receiver and create an http4s client for the caller.

[HTTP4S Integration](docs/http4s.md)

## kafka

We can convert function calls to kafka message publishing and subscriptions to function calling.

[kafka integration](docs/kafka.md)

## Isolated classloader

This executes functions locally using an IsolatedClassLoader. The caller doesn't depend on the receiver but internally
coursier is used to resolve the receiver dependency.

[local receivers](docs/local.md)
