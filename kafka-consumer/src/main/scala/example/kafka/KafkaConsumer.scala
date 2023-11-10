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
  val consumer = new KafkaConsumer(KafkaConf.props, new ByteArrayDeserializer, new ByteArrayDeserializer)
  val m        = KafkaFunctionsReceiverFactory.invokerMap(new KafkaFunctionsImpl)
  Using.resource(KafkaPoller(consumer, m)): poller =>
    consumer.subscribe(Seq("person").asJava)
    poller.poll(Duration.ofSeconds(10))

class KafkaFunctionsImpl extends KafkaFunctions:
  override def addPerson(key: PersonKey)(ttl: Long, person: Person): Unit =
    println(s"Subscriber received addPerson: key=$key, params = $ttl, $person")

  override def removePerson(key: PersonKey)(person: Person): Unit =
    println(s"Subscriber received removePerson: key=$key, params = $person")
