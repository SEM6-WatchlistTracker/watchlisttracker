# Watchlist Tracker

Spring Boot microservices-based system consists of the following modules:

- **admin-service**: a module containing embedded Spring Boot Admin Server used for monitoring Spring Boot microservices running on Kubernetes
- **gateway-service**: a module that Spring Cloud Netflix Zuul for running Spring Boot application that acts as a proxy/gateway in our architecture.
- **organization-service**: a module containing a sample microservice.

The following picture illustrates the architecture described above including Kubernetes objects.

<img src="https://piotrminkowski.files.wordpress.com/2018/07/micro-kube-1.png" title="Architecture1">

## Deploy

<p align="center">
<a href="https://cloud.okteto.com/deploy">
  <img src="https://okteto.com/develop-okteto.svg" alt="Develop on Okteto">
</a>
</p>
