import functions.proxygenerator.*

// here we configure the target dir for generated classes and also what serializers and transports
// we want to generate code for the caller.

val TargetRoot = s"$ProjectRoot/ls-caller/src/main/generated"

generateCaller(generatorConfig, avroSerialization = true, jsonSerialization = true, classloaderTransport = true)
    .generate(TargetRoot, LsExportsDep)
