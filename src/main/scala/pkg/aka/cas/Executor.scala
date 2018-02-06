package pkg.aka.cas

import com.datastax.driver.core._
import akka.actor.Actor

case class execute_query(query: String)
case class newQuery(query: String)

class Executor extends Actor {

  val cluster = Cluster.builder
    .withClusterName("myCluster")
    .addContactPoint("localhost")
    .build

  val session = cluster.connect("temp")

  /**
    *
    *This is the table
    * assuming table is created with some data
    *
    * session.execute (s"CREATE TABLE IF NOT EXISTS $table (year int, month int,day int,age int,frequency int, income int,value_segment
    *  int,brand text,category text,class text, style text,color_type text,choice_code int,color_family text,tier text,
    *  region text,location text,tx_time timeuuid,tx_id int,unit int,Basket_value int, PRIMARY KEY ((year, age, brand),
    *  month, day, region, location)) WITH caching = { 'keys' : 'ALL', 'rows_per_partition' : '10' };")
    *
    * session.execute (s"Insert into $table (year, month, day, age, frequency , income, value_segment , brand, category, class, style,
    *  color_type, choice_code, color_family, tier, region,location, tx_time, tx_id, unit, Basket_value) values(2000,5,21,
    *  19,18,15000,10,'xyzBrand','CatogoricalVariable','Aclass','style','redColor',1234,'warmColor','threeTier',
    *  'southwestRegion','India',now(),01,5,1221)")
    *
    * session.execute ("Insert into $table (year, month, day, age, frequency , income, value_segment , brand, category, class, style,
    *  color_type, choice_code, color_family, tier, region,location, tx_time, tx_id, unit, Basket_value) values(2000,4,21,
    *  20,18,15500,10,'pqrBrand','AA','Aclass','style','BlueColor',1234,'coldColor','threeTier','northeastRegion','India',
    *  now(),01,5,1221)
    **/
  def receive = {

    /**
      * upon receiving message `execute_query` with the query to be executed
      * this executes query with session.execute and returns the result
      * */
    case execute_query(query) =>
      var resultset = session.execute(query)
      println(resultset.all)
      println(s"executed queries $query")

      /**
        * This goes back to Sender looking for some queries to execute
        * */
      sender ! moreQuery
//      session.close()
  }
}
