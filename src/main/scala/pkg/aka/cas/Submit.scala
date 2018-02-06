package pkg.aka.cas

import akka.actor._

case class QueryToFire(query: String)
case object moreQuery

class Submit(sub_query: ActorRef) extends Actor {
  var executed_Query = Set.empty[String]

  def receive = {

    /**
      * upon receiving of message `QueryToFire` this checks if
      * the query is already executed if not it executes it and
      * adds it in `executed_Query`.
      * */
    case QueryToFire(query) =>
      if (!executed_Query(query))
        sub_query ! execute_query(query)
      executed_Query += query
      println(s"queries done + $executed_Query")

      /**
        * `sub_query` sends the message `execute_query` with the
        * query to be executed
        * */
      sub_query ! execute_query(query)

    case moreQuery =>
      var query = readLine("query ? > ")

      /**
        * Every new Query is first checked whether it has been
        * executed or not.
        * */
      if (!executed_Query(query))
        sub_query ! execute_query(query)
      executed_Query += query
      println(s"queries done + $executed_Query")

      /**
        * `sub_query` sends the message `execute_query` with the
        * query to be executed
        * */
      sub_query ! execute_query(query)

  }
}

object Submit extends App {
  println("...")
  val system = ActorSystem("akka_sys")

  val sub_query = system.actorOf(Props[Executor], name = "sub")
  val start = system.actorOf(Props(new Submit(sub_query)), name = "exe") // transmitter
  // start them going
  /**
    * here it will ask for query first time
    *
    */
  val query = readLine("query ? > ")

  /**
    * start will send message `QueryToFire` to Submit,
    * with query to be executed.
    * */
  start ! QueryToFire(query)
}
