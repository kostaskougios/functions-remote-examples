package example.kafka

import example.model.{Person, PersonKey}

/** Mark it as exported:
  *
  * //> exported
  *
  * functions-remote kafka integration works by having methods (with Unit as return type). Calling these methods publishes data and on the subscriber side the
  * actual implementation of this trait is called when the subscriber receives the data.
  */
trait KafkaFunctions:
  /** kafka code generation uses 2 sets of params with the 1st one containing the kafka key and the 2nd set any data we want to publish to kafka. The kafka key
    * can be a primitive (string, int etc) but for this example we use a case class.
    */
  def addPerson(key: PersonKey)(ttl: Long, person: Person): Unit
  def removePerson(key: PersonKey)(person: Person): Unit
