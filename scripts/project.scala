// All dependencies for scripts are located here.
//> using dependency com.lihaoyi::os-lib:0.9.1
//> using dependency org.functions-remote::proxy-generator:0.1-SNAPSHOT
import functions.model.GeneratorConfig

// Common config and methods for caller & receiver scripts

val ProjectRoot = os.pwd

val generatorConfig = GeneratorConfig.withDefaults()

val LsExportsDep = "com.example:ls-exports_3:0.1-SNAPSHOT"
val CatsLsExportsDep = "com.example:cats-ls-exports_3:0.1-SNAPSHOT"
