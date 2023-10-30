// All dependencies for scripts are located here.

//> using dependency com.lihaoyi::os-lib:0.9.1
//> using dependency org.functions-remote::proxy-generator:0.1-SNAPSHOT
//> using dependency commons-io:commons-io:2.15.0

// Common config and methods for caller & receiver scripts

import functions.model.GeneratorConfig
import java.io.File
import org.apache.commons.io.FileUtils

val ProjectRoot = os.pwd

val generatorConfig = GeneratorConfig.withDefaults()

val LsExportsDep = "com.example:ls-exports_3:0.1-SNAPSHOT"

def deleteScalaFiles(dir: String) = 
    println(s"Deleting scala files from $dir")
    FileUtils.deleteDirectory(new File(dir))
