package example.kafka

import java.util.Properties

object KafkaConf:
  val props = new Properties
  props.put("bootstrap.servers", "localhost:9092")
  props.put("linger.ms", 1)
  props.setProperty("group.id", "test")
  props.setProperty("client.id", "test-consumer")
  props.setProperty("enable.auto.commit", "true")
  props.setProperty("auto.commit.interval.ms", "1000")
  props.put("auto.offset.reset", "earliest")
