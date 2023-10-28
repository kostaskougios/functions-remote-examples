//> using scala 2.13
//> using dependency org.functions-remote::coursier:0.1-SNAPSHOT
import functions.coursier._

val LsVersion = "0.1-SNAPSHOT"

val functionsDependencies = Seq(
    s"com.example:ls-receiver_3:$LsVersion"
)
val exportsDependencies   = Seq(
    s"com.example:ls-exports_3:$LsVersion"
)
val resolver              = new CoursierResolver()
resolver.importDependencies(functionsDependencies)
resolver.importExports(exportsDependencies)

println("Ok, functions dependencies imported under .local/dependencies directory and exports under .local/exports")
