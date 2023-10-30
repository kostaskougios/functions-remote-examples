# functions-remote

It would be nice if there was a simple way to call remote functions as if they were local. RPC but with a twist that we'll see below.

So lets say we have a function `def add(a: Int, b: Int): Int`. With functions-remote we will be able to call it on machine A normally (i.e. `add(5,6)`) 
and it's implementation be executed on machine B. The result then becomes available on A. All these with configurable serialization and transports.

Functions-remote is a code generator for calling functions "remotely", using different serialization methods (like json or avro), and different remote transports (like http).
Remotely means we may use http as a transport (i.e. via http4s) or just use an isolated classloader as transport so that we can
execute the function locally. We'll see all these in more details below as well as why it can be useful to use different transports.

Note: Only for scala3. Currently, circe-json and avro4s serializations are supported.

The generated code is very readable and as if written by a person.

## How it works

Let's say we have the following functionality that simulates the `ls` command line tool:

```scala
trait LsFunctions:
  def ls(path: String, lsOptions: LsOptions = LsOptions.Defaults): LsResult
  def fileSize(path: String): Long
```

We can then use functions-remote to create code for us that:
- create case classes for the parameters of each method in the trait i.e. `case class Ls(path: String, lsOptions: LsOptions)`
- create serializers for json, avro etc so that we can serialize/deserialize `Ls` and `LsResult`
- create a `class LsFunctionsCaller extends LsFunctions` which can be used to call remotely our LsFunctions and use any serialization method available. The `Caller` extends our trait and internally serializes the args to `Array[Byte]` and sends them to the `transport`.
- create a `transport` for http4s, or locally via a classloader (we'll see that later on) etc
- create an `LsFunctionsReceiver` that receives the `Array[Byte]` and converts it to a call to `LsFunctionsImpl`

After the code generation, we can use the generated code to call the function "remotely" (note for http4s and cats-effects, see the http4s integration):
```scala
val lsFunctions: LsFunctions = builder.newAvroLsFunctions
val result: LsResult         = lsFunctions.ls("/tmp")
```

## Architecture and Terminology

A call to a function is done via a generated `Caller` class, serialized, transferred to the `Receiver` via a `transport` and the `Receiver` takes 
care of actually calling the function implementation.

Our code invokes `LsFunctions.ls()` ➡️ `ls()` args are copied to the generated `Ls` case class ➡️ `Ls` is serialized to `Array[Byte]` ➡️ the transport is used to transfer the bytes to the `Receiver` ➡️ on the `Receiver` side we deserialize `Ls` and use it to invoke the actual `LsFunctionsImpl` ➡️ `LsResult` is serialized and send back to the caller

## Structure of the code for `Receiver` (or how to export a trait's functions)

We will need 2 modules:
- a module with the trait with the exported traits and related (serializable) case classes
- a module to implement the traits and be able to receive requests via it's transport

```
<project>
    |- ls-exports           : exported traits and related case classes, no extra code here, minimum or none external dependencies
    |- ls-receiver          : depends on ls-exports and implements the exported traits along with some extra code for the transport
```

## Structure of the code for `Caller` of functions

This should be just a standard module that depends on `ls-exports` only.

# Integrations

## http4s 

We can use the generator to integrate with http4s i.e. create routes for the receiver and create an http4s client for the caller.

[HTTP4S Integration](docs/http4s.md)


## Executing functions locally via an isolated classloader or spawning a jvm for each call

[local receivers](docs/local.md)
