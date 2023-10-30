import functions.proxygenerator.*

// here we configure the target dir for generated classes and also what serializers
// we want to generate code for the receiver.

val TargetRoot = s"$ProjectRoot/cats-http4s-ls-receiver/src/main/generated"

generateReceiver(generatorConfig, avroSerialization = true, jsonSerialization = true, http4sRoutes = true)
    .generate(TargetRoot, CatsLsExportsDep)
