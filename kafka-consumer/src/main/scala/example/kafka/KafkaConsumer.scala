package example.kafka

import example.model.{Person, PersonKey}
import functions.kafka.KafkaPoller
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.serialization.ByteArrayDeserializer

import java.time.Duration
import scala.jdk.CollectionConverters.*
import scala.util.Using

@main
def kafkaConsumer() =
  // The serialization is done for us by functions-remote, so we just need ByteArraySerializer
  val consumer   = new KafkaConsumer(KafkaConf.props, new ByteArrayDeserializer, new ByteArrayDeserializer)
  // This is a map of (function-coordinates , serialization format)  -> function-impl and is used
  // to call the correct function with the correct deserializer
  val invokerMap = KafkaFunctionsReceiverFactory.invokerMap(new KafkaFunctionsImpl)

  // ok now we're ready to poll for data.
  Using.resource(KafkaPoller(consumer, invokerMap)): poller =>
    consumer.subscribe(Seq("person").asJava)
    poller.poll(Duration.ofSeconds(10))

class KafkaFunctionsImpl extends KafkaFunctions:
  override def addPerson(key: PersonKey)(ttl: Long, person: Person): Unit =
    println(s"Subscriber received addPerson: key=$key, params = $ttl, $person")

  override def removePerson(key: PersonKey)(person: Person): Unit =
    println(s"Subscriber received removePerson: key=$key, params = $person")
