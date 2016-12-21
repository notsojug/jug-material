# README

### Requirements

* Java 8 JVM
* Maven 3.3.9
* Docker 1.12

### Resources locations

* [http://localhost:8080/hello](http://localhost:8080/hello)
* [http://localhost:8080/names-generator](http://localhost:8080/names-generator)

## Demo 1

To build the Uberjar:

`$ mvn clean package`

To run the Uberjar:

`$ java -jar target/swarm-demo-swarm.jar`

## Demo 2

To build the Container:

`$ mvn clean docker:build`

To run the Container:

`$ docker run -p 8080:8080 swarm-demo`
