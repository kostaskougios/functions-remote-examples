package example.kafka

import example.model.{Person, PersonKey}
import functions.kafka.KafkaTransport

@main
def kafkaProducer() =
  val transport     = KafkaTransport("person", KafkaConf.props)
  val avroFunctions = KafkaFunctionsCallerFactory.newAvroKafkaFunctions(transport.transportFunction)
  val jsonFunctions = KafkaFunctionsCallerFactory.newJsonKafkaFunctions(transport.transportFunction)
  try
    avroFunctions.addPerson(PersonKey("person1"))(2000, Person(5, "person-avro"))
    avroFunctions.removePerson(PersonKey("person2"))(Person(6, "person-to-remove-avro"))
    println("Published with avro serialization.")

    jsonFunctions.addPerson(PersonKey("person3"))(1000, Person(6, "person-json"))
    println("Published with json serialization.")
  finally transport.close()
