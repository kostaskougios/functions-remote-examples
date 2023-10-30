val scala3Version = "3.3.1"

ThisBuild / version := "0.1-SNAPSHOT"

ThisBuild / organization := "com.example"

name := "functions-remote-examples"

ThisBuild / scalaVersion := scala3Version

ThisBuild / scalacOptions ++= Seq("-unchecked", "-feature", "-deprecation", "-Xmax-inlines", "64")

// ----------------------- dependencies --------------------------------

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
// ----------------------- modules --------------------------------

// ----------------------- Example commands ---------------------------------------
lazy val `ls-exports` = project
  .settings(
    libraryDependencies ++= Seq(ScalaTest),
    buildInfoKeys    := Seq[BuildInfoKey](organization, name, version, scalaVersion, "exportedArtifact" -> "ls-receiver_3"),
    buildInfoPackage := "commands.ls"
  )
  .enablePlugins(BuildInfoPlugin)

lazy val `ls-receiver` = project
  .settings(
    Compile / unmanagedSourceDirectories += baseDirectory.value / "src" / "main" / "generated",
    cleanFiles += baseDirectory.value / "src" / "main" / "generated",
    libraryDependencies ++= Seq(Avro4s, FunctionsReceiver) ++ Circe
  )
  .dependsOn(`ls-exports`)

lazy val `ls-caller` = project
  .settings(
    Compile / unmanagedSourceDirectories += baseDirectory.value / "src" / "main" / "generated",
    cleanFiles += baseDirectory.value / "src" / "main" / "generated",
    libraryDependencies ++= Seq(Avro4s, FunctionsCaller) ++ Circe
  )
  .dependsOn(`ls-exports`)
