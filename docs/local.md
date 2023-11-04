# Calling functions locally but without having them into your app classpath

Functions-remote can be useful when we want to call functions but not include all their code and dependencies into
our own classpath. This can be useful i.e. if we have 2 codebases with incompatible dependencies but still we want
to use functions of one in the other or if we use functions-remote http transport for production but for development
we want to just invoke the functions via an isolated classloader so that we don't run a bunch of http services locally.

To make it work there are a few steps:
- create our module with the exported functions. Configure it in sbt. In this example, it is the `ls-exports` module.
- publish it locally so that the jar is available.
- the generator then can read the jar and generate code for the caller and receiver.
- create the receiver (impl of the functions) as a separate module where the exported functions are implemented and publish it locally so that the isolated classloader can find it.
- create the caller (the code that calls the exported functions). The caller doesn't depend on the receiver and can have incompatible classpaths. The generated
    caller classes use the isolated class loader as a transport and the classloader loads all ls-receiver jars and invokes the actual implementation.


Let's see the structure of our LsFunctions example. We will impl both the caller and receiver. 

```
├── ls-caller                   : the caller, depends on ls-exports only but is able to call ls-receiver via the IsolatedClassLoaderTransport
├── ls-exports                  : contains the exported trait, LsFunctions
└── ls-receiver                 : contains LsFunctionsImpl and depends on ls-exports
```

The rest of the documentation is inside the code, starting with [`build.sbt`](../build.sbt) where you can see how 
the functions-remote-sbt-plugin is configured and used and then [the bash script that runs the build an executes ls-caller](../bin/build-and-run-call-to-lsfunctions-function-via-isolated-classloader).


# Using a separate jvm per call
Not yet implemented
