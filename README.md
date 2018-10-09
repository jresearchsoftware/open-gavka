# Gavka #

This project was created by two friends who were constantly under attack by the people, who tried to find out if their message was actually written to Apache Kafka.

Gavka is a web application for browsing Apache Kafka topics. The tool allows
  * search messages by key or time
  * export topic as a text file
  
Gavka consists of the two war files and can be deployed to any J2EE server. It requires a connection to the running Kafka cluster, which can be configured using environment variables. It is also possible to run Gavka with docker.

### How to Use ###

If you are successfully connected to Kafka Cluster then you can see your topics in the drop down.  Select the topic, select the serialisers and click Search button. You can also filter the messages by timestamp (enter in UTC).

### How to Build ###

Clone the repository then

`cd pom`

`mvn package`

Two files *api.war* and *gavka.war* will be under _api.app/target_ and _gwt.app/target_ directories. Those files can be deployed to tomcat or other server. Two environment variables must be specified

* bootstrap.servers
* schema.registry.url

### How to Run with Docker ###

It is assumed that you have an access to running Kafka cluster.

`docker run -d -p 4000:8080 -e bootstrap.servers=kafka_host:kafka_port -e schema.registry.url=http://schema_host:schema_port -p kafka_port:kafka_port -p schema_port:schema_port open-gavka`

For example,

`docker run -d -p 4000:8080 -e bootstrap.servers=172.16.1.1:9092 -e schema.registry.url=http://172.16.1.1:8081 -p 9092:9092 -p 8081:8081 open-gavka`

Then you can connect as http://localhost:4000/gavka

### Why is this project called Gavka? ###

When we were thinking about the name of the project this dog suggested: Gav! ![artie](images/artie.JPG)

