import functions.proxygenerator.*

val TargetRoot = s"$ProjectRoot/ls-caller/src/main/generated"
deleteScalaFiles(TargetRoot)

generateCaller(generatorConfig, avroSerialization = true, jsonSerialization = true, classloaderTransport = true)
    .generate(TargetRoot, LsExportsDep)
