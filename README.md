# Madrasati Application

![LOGO of the project](./picture/LOGO.png)

## Spring Boot Application with Docker

## Overview

This project is a Spring Boot application packaged with Docker and managed using Docker Compose. It includes configuration for a PostgreSQL database, Redis, and MongoDB and the frontend mobile app using kotlin andriod

![Screenshot of out project database](./picture/مدرستي%20-%20database%20scheme%201.jpg)

## Prerequisites

- [Docker](https://docs.docker.com/get-docker/) (version 20.10 or higher)
- [Docker Compose](https://docs.docker.com/compose/install/) (version 1.29 or higher)
- [Java 17](https://www.oracle.com/java/technologies/javase-jdk17-downloads.html) or higher (if running outside Docker)

## Getting Started

cd into the project where the compose and docker file in project folder
the write

### Clone the Repository

```bash
git clone https://github.com/your-username/your-repository.git
cd your-repository
```

### To run the project

```bash
docker compose up --build
```
