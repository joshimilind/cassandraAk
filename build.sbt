name := "akcas"

version := "0.1"

scalaVersion := "2.12.4"

libraryDependencies ++= Seq(
  "com.datastax.cassandra" % "cassandra-driver-core" % "3.3.2",
  "com.typesafe.akka" %% "akka-actor" % "2.4.16"
)
