val scala3Version = "3.3.1"

ThisBuild / version := "0.1-SNAPSHOT"

ThisBuild / organization := "com.example"

name := "functions-remote-examples"

ThisBuild / scalaVersion := scala3Version

ThisBuild / scalacOptions ++= Seq("-unchecked", "-feature", "-deprecation", "-Xmax-inlines", "64")

// -----------------------------------------------------------------------------------------------
// Dependencies
// -----------------------------------------------------------------------------------------------

val FunctionsVersion  = "0.1-SNAPSHOT"
val FunctionsCaller   = "org.functions-remote" %% "functions-caller"   % FunctionsVersion
val FunctionsReceiver = "org.functions-remote" %% "functions-receiver" % FunctionsVersion

val ScalaTest = "org.scalatest" %% "scalatest" % "3.2.15" % Test

val Avro4s       = "com.sksamuel.avro4s" %% "avro4s-core" % "5.0.5"
val CirceVersion = "0.14.1"

val Circe = Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser"
).map(_ % CirceVersion)

val Http4sVersion = "0.23.23"

val Http4sServer = Seq(
  "org.http4s" %% "http4s-ember-server" % Http4sVersion,
  "org.http4s" %% "http4s-dsl"          % Http4sVersion
)

val Http4sClient = Seq(
  "org.http4s" %% "http4s-ember-client" % Http4sVersion
)

val Http4sCirce = Seq("org.http4s" %% "http4s-circe" % Http4sVersion)

val CatsEffect = "org.typelevel" %% "cats-effect" % "3.5.2"

val CatsEffectsTesting = "org.typelevel" %% "cats-effect-testing-scalatest" % "1.5.0" % Test
// -----------------------------------------------------------------------------------------------
// Example for IsolatedClassLoaderTransport
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
    Compile / unmanagedSourceDirectories += baseDirectory.value / "src" / "main" / "generated",
    cleanFiles += baseDirectory.value / "src" / "main" / "generated",
    libraryDependencies ++= Seq(Avro4s, FunctionsReceiver) ++ Circe
  )
  .dependsOn(`ls-exports`)

/** This is a user of LsFunctions but it doesn't depend on ls-receiver.
  */
lazy val `ls-caller` = project
  .settings(
    Compile / unmanagedSourceDirectories += baseDirectory.value / "src" / "main" / "generated",
    cleanFiles += baseDirectory.value / "src" / "main" / "generated",
    libraryDependencies ++= Seq(Avro4s, FunctionsCaller) ++ Circe
  )
  .dependsOn(`ls-exports`)

// -----------------------------------------------------------------------------------------------
// Cats effects examples
// -----------------------------------------------------------------------------------------------

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
    Compile / unmanagedSourceDirectories += baseDirectory.value / "src" / "main" / "generated",
    cleanFiles += baseDirectory.value / "src" / "main" / "generated",
    libraryDependencies ++= Seq(Avro4s, FunctionsReceiver) ++ Http4sServer ++ Circe
  )
  .dependsOn(`cats-ls-exports`)
