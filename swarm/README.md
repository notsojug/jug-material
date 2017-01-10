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

# JMeter

`$ jmeter -n -t hello.jmx -l hello-results.csv -j hello.log -e -o hello-site`

# Metrics

The resource:

* [http://localhost:8080/metrics](http://localhost:8080/metrics)
* [http://localhost:8080/metrics?name=length](http://localhost:8080/metrics?name=length)

Sample output:

```text
-- Histograms ------------------------------------------------------------------
length
             count = 14
               min = 12
               max = 20
              mean = 14,80
            stddev = 2,16
            median = 14,00
              75% <= 16,00
              95% <= 20,00
              98% <= 20,00
              99% <= 20,00
            99.9% <= 20,00

-- Timers ----------------------------------------------------------------------
time
             count = 14
         mean rate = 42,23 calls/minute
     1-minute rate = 81,26 calls/minute
     5-minute rate = 92,85 calls/minute
    15-minute rate = 94,94 calls/minute
               min = 0,14 milliseconds
               max = 4,26 milliseconds
              mean = 0,46 milliseconds
            stddev = 0,99 milliseconds
            median = 0,19 milliseconds
              75% <= 0,24 milliseconds
              95% <= 4,26 milliseconds
              98% <= 4,26 milliseconds
              99% <= 4,26 milliseconds
            99.9% <= 4,26 milliseconds
```