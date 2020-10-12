# Gavka #

This project was created by two friends who were constantly under attack by the people, who tried to find out if their message was actually written to Apache Kafka.

Gavka is a web application for browsing Apache Kafka topics. The tool allows

* search messages by key or time
* export topic as a text file

There is also a separate application for managing the connections to Kafka. Currently you can configure there connections to various Kafka environments.

If you just want to look how it works, here is the [latest version](http://c.chnch.us:90/).

Gavka consists of the three war files and can be deployed to any J2EE server. It requires a connection to the running Kafka cluster, which can be configured using environment variables. It is also possible to run Gavka with docker. It also requires Postgres database to store the information about connections and their properties.

After successful installation you will have two working applications

* http://localhost:90 - Gavka Kafka Tool
* http://localhost:90/admin - Gavka Administration for Kafka Tool

### How to Use ###

If you are successfully connected to Kafka Cluster then you can see your topics in the drop down.  Select the topic, select the serialisers and click Search button. You can also filter the messages by timestamp (enter in UTC).

### How to get sources ###

Main repository (hg): https://helixteamhub.cloud/JRS/projects/gavka/repositories/gavka

Mirror on GitHub (git): https://github.com/jresearchsoftware/open-gavka

### How to Build ###

Clone the parent POM repository (https://github.com/jresearchsoftware/open-pom) and install them 

`cd pom`

`mvn install`

Clone this repository then

`cd pom`

`mvn package`

Three files *api.war*, *gavka.war* and *gavkin.war* can be deployed to tomcat or other server. The database tables will be created during the first start, the database user must have all the necessary rights.

The following environment variables must be specified

* bootstrap.servers - kafka brokers, for example localhost:9092
* schema.registry.url - schema registry (optional), for example localhost:8081
* DB_HOST - host with postgresql server 
* DB_PORT - port for connection to postgresql server
* DB_USER - user for connection to postgres database
* DB_NAME - database name 
* DB_PASSWORD - password for connection to postgres database


### How to Run with Docker ###

**Please, pay attention to the API container name. Docker should be able to resolve gavka-api host for the correct work of the client**

If you have an access to running Kafka cluster add the connection parameters to the docker-compose.yml. Here we assume that Kafka and Postgresql are installed in 172.16.1.1 server.

```yaml
version: "2.0"

services:
    gavka-api: 
        image: gavka/gavka-api:latest
        environment:  
# uncomment if you do not have kafka and want to use mock          
#          - spring.profiles.active=nokafka
# if you have working kafka, put here connection parameters to kafka   
          - bootstrap.servers=172.16.1.1:9092
          - schema.registry.url=http://172.16.1.1:8081
          - DB_HOST=172.16.1.1
          - DB_PORT=5432
          - DB_NAME=gavkaDev
          - DB_USER=gavkaDev
          - DB_PASSWORD=gavkaDev 
    gavkin-ui:
        depends_on: 
            - gavka-api
        image: gavka/gavkin-ui:latest
    gavka-ui:
        depends_on: 
            - gavka-api
            - gavkin-ui
        image: gavka/gavka-ui:latest
        ports:
            - 90:80
```   
Then you can run it as 
`docker-compose up`

Now you can connect as http://localhost:90

If you do not have running Kafka and just want to run it with the mock services use the following docker-compose.yml :

```yaml
version: "2.0"

services:
    gavka-api: 
        image: gavka/gavka-api:latest
        environment:  
          - spring.profiles.active=nokafka
          - DB_HOST=172.16.1.1
          - DB_NAME=gavkaDev
          - DB_PORT=5432
          - DB_USER=gavkaDev
          - DB_PASSWORD=gavkaDev 
    gavkin-ui:
        depends_on: 
            - gavka-api
        image: gavka/gavkin-ui:latest
    gavka-ui:
        depends_on: 
            - gavka-api
            - gavkin-ui
        image: gavka/gavka-ui:latest
        ports:
            - 90:80

```   


### Why is this project called Gavka? ###

When we were thinking about the name of the project this dog suggested: Gav! ![artie](images/artie.JPG)
