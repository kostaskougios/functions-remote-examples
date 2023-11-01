# functions-remote

Note: Only for scala3. Currently, circe-json and avro4s serializations are supported.


For scala, the most important ability of the language is the ability to call a function `f(x) = y` and get its results.
And while we can do that within a jvm instance, it would be useful to be able to call functions this easily across jvms that
are running in the same box or remote boxes.

Functions-remote is a code generator for calling functions "remotely", using different serialization methods (like json or avro), and different remote transports (like http).
Remotely means we may use http as a transport (i.e. via http4s) or just use an isolated classloader as transport so that we can
execute the function locally or maybe just spawn a new jvm to call the function. 
We'll see all these in more details below as well as why it can be useful to use different transports.

Effectively functions-remote allows the simplicity of `f(x) = y` no matter where `f` will really run. 

The generated code is very readable and as if written by a person.

## The exported function

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

After the code generation, we can use the generated code to call the function "remotely" (note for http4s and cats-effects, see the http4s integration):
```scala
val lsFunctions: LsFunctions = builder.newAvroLsFunctions
val result: LsResult         = lsFunctions.ls("/tmp")
```

## Flow of data during a call

A call to a function is done via a generated `Caller` class, serialized, transferred to the `Receiver` via a `transport` and the `Receiver` takes 
care of actually calling the function implementation.

Our code invokes `LsFunctions.ls(... args ...)` ➡️ args are copied to the generated `Ls` case class ➡️ `Ls` is serialized to `Array[Byte]` ➡️ the transport is used to transfer the bytes to the `Receiver` ➡️ on the `Receiver` side we deserialize `Ls` and use it to invoke the actual `LsFunctionsImpl` ➡️ `LsResult` is serialized and send back to the caller

## Project structure for exporting functions

We will need 2 modules:
- a module with the exported traits and related case classes
- a module to implement the traits and be able to receive requests via its transport(s)

```
<project>
    |- ls-exports           : exported traits and related case classes, no extra code here, minimum or none external dependencies
    |- ls-receiver          : depends on ls-exports and implements the exported traits along with some extra code for the transport
```

## Project structure for calling the exported functions

This should be just a standard module that depends on `ls-exports` only. The build scripts should also generate code to do the calls.

# Integrations

## http4s 

We can use the generator to integrate with http4s i.e. create routes for the receiver and create an http4s client for the caller.

[HTTP4S Integration](docs/http4s.md)


## Executing functions locally via an isolated classloader or spawning a jvm for each call

[local receivers](docs/local.md)
