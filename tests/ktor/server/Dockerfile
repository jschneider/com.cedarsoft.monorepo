# Use official maven build as base
FROM openjdk:8-jdk

ADD target/ktor-server-demo.jar /ktor-server/ktor-server-demo.jar
CMD java -XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap -jar /ktor-server/ktor-server-demo.jar

EXPOSE 8080
