import functions.proxygenerator.*

val TargetRoot = s"$ProjectRoot/ls-receiver/src/main/generated"
deleteScalaFiles(TargetRoot)

generateReceiver(generatorConfig, avroSerialization = true, jsonSerialization = true)
    .generate(TargetRoot, LsExportsDep)
