/** This build has different sections for each integration. I.e. an http4s section and a kafka section. These sections are not related to each other, please
  * examine the section you're interested in.
  */
val scala3Version = "3.3.1"

ThisBuild / version      := "0.1-SNAPSHOT"
ThisBuild / organization := "com.example"
name                     := "functions-remote-examples"
ThisBuild / scalaVersion := scala3Version
ThisBuild / scalacOptions ++= Seq("-unchecked", "-feature", "-deprecation")

// -----------------------------------------------------------------------------------------------
// Dependencies
// -----------------------------------------------------------------------------------------------

val FunctionsVersion  = "0.1-SNAPSHOT"
val FunctionsCaller   = "org.functions-remote" %% "functions-caller"   % FunctionsVersion
val FunctionsReceiver = "org.functions-remote" %% "functions-receiver" % FunctionsVersion
val FunctionsAvro     = "org.functions-remote" %% "functions-avro"     % FunctionsVersion

val ScalaTest    = "org.scalatest"       %% "scalatest"   % "3.2.15" % Test
val Avro4s       = "com.sksamuel.avro4s" %% "avro4s-core" % "5.0.5"
val CirceVersion = "0.14.1"
val Circe        = Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser"
).map(_ % CirceVersion)

// -----------------------------------------------------------------------------------------------
// Example for IsolatedClassLoaderTransport and non-cats functions
// -----------------------------------------------------------------------------------------------

/** This contains the exported trait LsFunctions and related case classes. Normally this should not have any other dependencies, maybe just scalatest for the
  * test classpath but none for the compile classpath.
  */
lazy val `ls-exports` = project
  .settings(
    libraryDependencies ++= Seq(ScalaTest),
    // make sure exportedArtifact points to the full artifact name of the receiver.
    buildInfoKeys    := Seq[BuildInfoKey](organization, name, version, scalaVersion, "exportedArtifact" -> "ls-receiver_3"),
    buildInfoPackage := "commands.ls"
  )
  .enablePlugins(BuildInfoPlugin)

/** This contains the implementation of LsFunctions
  */
lazy val `ls-receiver` = project
  .settings(
    receiverExports           := Seq(s"com.example:ls-exports_3:${version.value}"),
    receiverJsonSerialization := true,
    receiverAvroSerialization := true,
    libraryDependencies ++= Seq(Avro4s, FunctionsAvro, FunctionsReceiver) ++ Circe
  )
  .dependsOn(`ls-exports`)
  .enablePlugins(FunctionsRemotePlugin)

/** This is a user of LsFunctions but it doesn't depend on ls-receiver.
  */
lazy val `ls-caller` = project
  .settings(
    callerExports                 := Seq(s"com.example:ls-exports_3:${version.value}"),
    callerAvroSerialization       := true,
    callerJsonSerialization       := true,
    callerClassloaderTransport    := true,
    callerClassloaderDependencies := Seq(s"com.example:ls-receiver_3:${version.value}"),
    libraryDependencies ++= Seq(Avro4s, FunctionsAvro, FunctionsCaller) ++ Circe
  )
  .dependsOn(`ls-exports`)
  .enablePlugins(FunctionsRemotePlugin)

// -----------------------------------------------------------------------------------------------
// Cats effects / http4s example
// -----------------------------------------------------------------------------------------------

val FunctionsHttp4sClient = "org.functions-remote" %% "http4s-client" % FunctionsVersion
val FunctionsHttp4sServer = "org.functions-remote" %% "http4s-server" % FunctionsVersion

val Http4sVersion = "0.23.23"
val Http4sServer  = Seq(
  "org.http4s" %% "http4s-ember-server" % Http4sVersion,
  "org.http4s" %% "http4s-dsl"          % Http4sVersion
)
val Http4sClient  = Seq(
  "org.http4s" %% "http4s-ember-client" % Http4sVersion
)
val Http4sCirce   = Seq("org.http4s" %% "http4s-circe" % Http4sVersion)
val CatsEffect    = "org.typelevel" %% "cats-effect" % "3.5.2"

/** The exports for cats-effects/http4s
  */
lazy val `cats-ls-exports` = project
  .settings(
    libraryDependencies ++= Seq(CatsEffect, ScalaTest),
    // make sure exportedArtifact points to the full artifact name of the receiver.
    buildInfoKeys    := Seq[BuildInfoKey](organization, name, version, scalaVersion, "exportedArtifact" -> "ls-receiver_3"),
    buildInfoPackage := "commands.ls"
  )
  .enablePlugins(BuildInfoPlugin)

/** This contains the cats-effects implementation of LsFunctions
  */
lazy val `cats-http4s-ls-receiver` = project
  .settings(
    receiverExports           := Seq(s"com.example:cats-ls-exports_3:${version.value}"),
    receiverJsonSerialization := true,
    receiverAvroSerialization := true,
    receiverHttp4sRoutes      := true,
    libraryDependencies ++= Seq(Avro4s, FunctionsAvro, FunctionsReceiver, FunctionsHttp4sServer) ++ Http4sServer ++ Circe
  )
  .dependsOn(`cats-ls-exports`)
  .enablePlugins(FunctionsRemotePlugin)

/** The client for our http4s server
  */
lazy val `cats-http4s-ls-caller` = project
  .settings(
    callerExports               := Seq(s"com.example:cats-ls-exports_3:${version.value}"),
    callerAvroSerialization     := true,
    callerJsonSerialization     := true,
    callerHttp4sClientTransport := true,
    libraryDependencies ++= Seq(Avro4s, FunctionsAvro, FunctionsHttp4sClient) ++ Http4sClient ++ Circe
  )
  .dependsOn(`cats-ls-exports`)
  .enablePlugins(FunctionsRemotePlugin)

// -----------------------------------------------------------------------------------------------
// Kafka examples
// -----------------------------------------------------------------------------------------------

val FunctionsKafkaProducer = "org.functions-remote" %% "kafka-producer" % FunctionsVersion
val FunctionsKafkaConsumer = "org.functions-remote" %% "kafka-consumer" % FunctionsVersion
val KafkaClient            = "org.apache.kafka"      % "kafka-clients"  % "3.6.0"

lazy val `kafka-exports` = project
  .settings(
    libraryDependencies ++= Seq(ScalaTest),
    // make sure exportedArtifact points to the full artifact name of the receiver.
    buildInfoKeys    := Seq[BuildInfoKey](organization, name, version, scalaVersion, "exportedArtifact" -> "kafka-consumer_3"),
    buildInfoPackage := "example.kafka"
  )
  .enablePlugins(BuildInfoPlugin)

lazy val `kafka-producer` = project
  .settings(
    libraryDependencies ++= Seq(ScalaTest, KafkaClient, FunctionsKafkaProducer, Avro4s, FunctionsAvro) ++ Circe,
    callerExports           := Seq(s"com.example:kafka-exports_3:${version.value}"),
    callerAvroSerialization := true,
    callerJsonSerialization := true
  )
  .dependsOn(`kafka-exports`)
  .enablePlugins(FunctionsRemotePlugin)

lazy val `kafka-consumer` = project
  .settings(
    libraryDependencies ++= Seq(ScalaTest, KafkaClient, FunctionsKafkaConsumer, Avro4s, FunctionsAvro) ++ Circe,
    receiverExports           := Seq(s"com.example:kafka-exports_3:${version.value}"),
    receiverAvroSerialization := true,
    receiverJsonSerialization := true
  )
  .dependsOn(`kafka-exports`)
  .enablePlugins(FunctionsRemotePlugin)

// -----------------------------------------------------------------------------------------------
// fiber & sockets examples
// -----------------------------------------------------------------------------------------------

val FunctionsFiberSocketsServer = "org.functions-remote" %% "loom-sockets-server" % FunctionsVersion
val FunctionsFiberSocketsClient = "org.functions-remote" %% "loom-sockets-client" % FunctionsVersion

lazy val `ls-fiber-sockets-server` = project
  .settings(
    receiverExports           := Seq(s"com.example:ls-exports_3:${version.value}"),
    receiverJsonSerialization := true,
    receiverAvroSerialization := true,
    libraryDependencies ++= Seq(Avro4s, FunctionsAvro, FunctionsFiberSocketsServer) ++ Circe
  )
  .dependsOn(`ls-exports`)
  .enablePlugins(FunctionsRemotePlugin)

lazy val `ls-fiber-sockets-client` = project
  .settings(
    callerExports           := Seq(s"com.example:ls-exports_3:${version.value}"),
    callerJsonSerialization := true,
    callerAvroSerialization := true,
    libraryDependencies ++= Seq(Avro4s, FunctionsAvro, FunctionsFiberSocketsClient) ++ Circe
  )
  .dependsOn(`ls-exports`)
  .enablePlugins(FunctionsRemotePlugin)

// -----------------------------------------------------------------------------------------------
// helidon webserver/client examples
// -----------------------------------------------------------------------------------------------

val HelidonVersion         = "4.0.1"
val HelidonServer          = "io.helidon.webserver"  % "helidon-webserver"       % HelidonVersion
val HelidonClient          = "io.helidon.webclient"  % "helidon-webclient-http2" % HelidonVersion
val HelidonServerLogging   = "io.helidon.logging"    % "helidon-logging-jul"     % HelidonVersion
val FunctionsHelidonServer = "org.functions-remote" %% "helidon-server"          % FunctionsVersion
val FunctionsHelidonClient = "org.functions-remote" %% "helidon-client"          % FunctionsVersion

lazy val `helidon-exports` = project
  .settings(
    libraryDependencies ++= Seq(ScalaTest),
    // make sure exportedArtifact points to the full artifact name of the receiver.
    buildInfoKeys    := Seq[BuildInfoKey](organization, name, version, scalaVersion, "exportedArtifact" -> "helidon-server_3"),
    buildInfoPackage := "examples.helidon"
  )
  .enablePlugins(BuildInfoPlugin)

lazy val `helidon-server` = project
  .settings(
    receiverExports           := Seq(s"com.example:helidon-exports_3:${version.value}"),
    receiverJsonSerialization := true,
    receiverAvroSerialization := true,
    receiverHelidonRoutes     := true,
    libraryDependencies ++= Seq(
      Avro4s,
      FunctionsReceiver,
      FunctionsAvro,
      FunctionsHelidonServer,
      ScalaTest,
      HelidonServer,
      HelidonServerLogging % Test
    ) ++ Circe
  )
  .dependsOn(`helidon-exports`)
  .enablePlugins(FunctionsRemotePlugin)

lazy val `helidon-client` = project
  .settings(
    callerExports                := Seq(s"com.example:helidon-exports_3:${version.value}"),
    callerJsonSerialization      := true,
    callerAvroSerialization      := true,
    callerHelidonClientTransport := true,
    libraryDependencies ++= Seq(Avro4s, ScalaTest, HelidonClient, FunctionsAvro, FunctionsCaller, FunctionsHelidonClient) ++ Circe
  )
  .dependsOn(`helidon-exports`)
  .enablePlugins(FunctionsRemotePlugin)
