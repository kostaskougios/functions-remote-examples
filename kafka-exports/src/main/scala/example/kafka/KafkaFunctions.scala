package example.kafka

import example.model.{Person, PersonKey}

/** Mark it as exported: //> exported
  */
trait KafkaFunctions:
  def addPerson(key: PersonKey)(ttl: Long, person: Person): Unit
  def removePerson(key: PersonKey)(person: Person): Unit
