package example.kafka

import example.model.{Person, PersonKey}
import functions.kafka.KafkaTransport

@main
def kafkaProducer() =
  val transport = KafkaTransport("person", KafkaConf.props)
  val f         = KafkaFunctionsCallerFactory.newAvroKafkaFunctions(transport.transportFunction)
  try
    f.addPerson(PersonKey("person1"))(2000, Person(5, "person-to-add"))
    f.removePerson(PersonKey("person2"))(Person(6, "person-to-remove"))
    println("Published.")
  finally transport.close()
