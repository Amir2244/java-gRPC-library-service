# Library Management Service

A modern library management system built with gRPC that allows managing books through a client-server architecture.

## Features

- Add new books to the library
- ISBN-based duplicate detection
- Real-time streaming responses
- Structured logging

## Technical Stack

- Java
- gRPC
- Protocol Buffers
- Stream Observer Pattern
- Java virtual threads

## Getting Started

### Prerequisites

- Java 21 or higher
- Maven
- gRPC tools

### Installation

1. Clone the repository
2. Build the project:
```bash
mvn clean install
```
3. Start the server:
```bash
java -jar target/library-grpc-1.0-SNAPSHOT.jar
```
