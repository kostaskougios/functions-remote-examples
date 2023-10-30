package example

import commands.ls.LsFunctionsCallerFactory
import functions.environment.RuntimeConfig

/** Run me with -Dfunctions.debug to see debugging information.
  */
def builder =
  val runtimeConfig = RuntimeConfig.withDefaults()
  LsFunctionsCallerFactory.newClassloaderBuilder(runtimeConfig)

@main
def callLsFunctionViaAvroSerialization() =
  val lsFunctions = builder.newAvroLsFunctions
  // Now we will call the function which will avro-serialize the params via LsFunctionsMethods.Ls() case
  // class and then load the lsFunctions jars via an isolated classloader. The classloader isolates the
  // classpath of this caller with the classpath of the lsFunctions jars, this way we can have incompatible
  // libraries for each.
  // Next the exported.Exported impl in lsFunctions is invoked which routes to our actual LsFunctionsImpl class.
  val result      = lsFunctions.ls("/tmp")
  println(result)

@main
def callLsFunctionViaJsonSerialization() =
  val lsFunctions = builder.newJsonLsFunctions
  val result      = lsFunctions.ls("/tmp")
  println(s"Result of lsFunctions.ls = $result")
