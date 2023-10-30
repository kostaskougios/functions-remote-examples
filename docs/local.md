# Calling functions locally but without having them into your app classpath

Functions-remote can be useful when we want to call functions but not include all their code and dependencies into
our own classpath. This can be useful i.e. if we have 2 codebases with incompatible dependencies but still we want
to use functions of one in the other or if we use functions-remote http transport for production but for development
we want to just invoke the functions via an isolated classloader so that we don't run a bunch of http services locally.

To make it work there are a few steps:
- create our module with the exported functions. Configure it in sbt. In this example, it is the `ls-exports` module.
- publish it locally so that the jar is available
- the generator then can read the jar and generate code for the caller and receiver.
- create the receiver as a separate module where the exported functions are implemented and publish it locally
- the isolated classloader or jvm runner need to know where all the jars of the receiver are located. Again with a small scala-cli script we can create a list of these jars.
- create the caller (the code that calls the exported functions). The caller doesn't depend on the receiver and can have incompatible classpaths.


Let's see the structure of our LsFunctions example. We will impl both the caller and receiver. 
The build configuration is the most complicated part of functions-remote at the moment due to sbt using scala2.12 and 
functions-remote been a scala3 project, but it will be simplified in the future.

```
├── ls-caller                   : the caller, depends on ls-exports only but is able to call ls-receiver
├── ls-exports                  : contains the exported traits , LsFunctions
├── ls-receiver                 : contains LsFunctionsImpl and depends on ls-exports
└── scripts                     : scala-cli scripts to help us with the code generation configuration
```

The rest of the documentation is inside the code, starting with [`build.sbt`](build.sbt)
and then the bash script that builds and runs the caller.

[bash script that runs the build an executes ls-caller](../bin/build-and-run-call-to-lsfunctions-function-via-isolated-classloader)

# Using a separate jvm per call
Not yet implemented
