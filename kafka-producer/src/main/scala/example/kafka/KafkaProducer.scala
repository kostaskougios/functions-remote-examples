package example.kafka

import example.model.{Person, PersonKey}
import functions.kafka.KafkaTransport
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.common.serialization.ByteArraySerializer

import scala.util.Using

@main
def kafkaProducer() =
  // The serialization is done for us by functions-remote, so we just need ByteArraySerializer
  val producer = new KafkaProducer(KafkaConf.props, new ByteArraySerializer, new ByteArraySerializer)
  Using.resource(new KafkaTransport("person", producer)): transport =>
    // KafkaFunctions impl using avro serialization
    val avroFunctions = KafkaFunctionsCallerFactory.newAvroKafkaFunctions(transport.transportFunction)
    // KafkaFunctions impl using json serialization
    val jsonFunctions = KafkaFunctionsCallerFactory.newJsonKafkaFunctions(transport.transportFunction)

    avroFunctions.addPerson(PersonKey("person1"))(2000, Person(5, "person-avro"))
    avroFunctions.removePerson(PersonKey("person2"))(Person(6, "person-to-remove-avro"))
    println("Published with avro serialization.")

    jsonFunctions.addPerson(PersonKey("person3"))(1000, Person(6, "person-json"))
    println("Published with json serialization.")
