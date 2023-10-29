//> using scala 2.13
//> using dependency org.functions-remote::coursier:0.1-SNAPSHOT
import functions.coursier._

val LsVersion = "0.1-SNAPSHOT"

val resolver              = new CoursierResolver()
resolver.createDependenciesForArtifact(s"com.example:ls-receiver_3:$LsVersion")
resolver.createDependencyFileForExport(s"com.example:ls-exports_3:$LsVersion")
