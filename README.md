# PaymentApi

## Requirements

For building and running the application using docker-compose you will need docker

- [DOCKER](https://www.docker.com/products/docker-desktop/)

## Running the application with docker-compose

Once you have Docker installed and running:

- Clone paymentApi project to your machine
- Go to the main project folder and execute the following command

```shell
docker-compose up --build
```
Once all the containers are up, the application is ready receive request.

# RestClient

## Requirements
For building and running the application you need:

- [JDK 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
- [Gradle 8.0.1](https://gradle.org/releases/)

## Running the application
Clone the [restclient](https://github.com/njrizzotti/restclient.git) project from git  
Within the project already cloned, run the following command

```shell
.\gradlew bootRun --args=1000
```

The args argument represents the number of api calls that the client will perform to the PaymentsApi
