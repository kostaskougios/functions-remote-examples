# kafka integration

functions-remote generates classes that can be used to publish and subscribe to kafka topics via just doing a method
call. For now the methods should return `Unit`. Also methods should have 2 param sets, the first should contain the
publish kafka key and the second any data required. Example:

```scala
  def addPerson(key: PersonKey)(ttl: Long, person: Person): Unit
```

On the publisher side a call to `addPerson()` is translated to publishing `ttl` and `person` to kafka with `key` as the
key for the published data.

On the consumer side, the subscriber receives the data and calls the impl of `addPerson()` with the deserialized data.

In this example both avro and json serialization is supported for publishing and subscribing.

The rest of the documentation can be found in the implementation itself. Here are a list of things to have a look at:

- [The shell script that builds and runs producer and consumer](../bin/build-kafka-producer-consumer)
- [sbt build.sbt, kafka section](../build.sbt) where the functions-remote-sbt-plugin is configured to generate classes
- [producer](../kafka-producer/src/main/scala/example/kafka/KafkaProducer.scala)
- [consumer](../kafka-consumer/src/main/scala/example/kafka/KafkaConsumer.scala)