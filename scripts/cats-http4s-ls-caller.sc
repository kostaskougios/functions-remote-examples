import functions.proxygenerator.*

val TargetRoot = s"$ProjectRoot/cats-http4s-ls-caller/src/main/generated"

// For the caller we also need to generate the client transport for http4s.
generateCaller(generatorConfig,avroSerialization = true, jsonSerialization = true, http4sClientTransport = true)
  .generate(TargetRoot, CatsLsExportsDep)
