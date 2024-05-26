# Libremines

"named this way because i didn't realise there was another libremines!"

## Building

### Requirements:
- Java 21 or newer (openjdk-21-jdk)
- Maven 3.8.8 (note: some versions cause gluon to freak out)
- GraalVM 21

### Instructions:

Optional, create maven wrapper:

```shell
mvn wrapper:wrapper
```

This is helpful for version management, in case you have an incompatible version of maven installed, but most people will not need this.

Install dependencies:

```shell
./mvnw install
```

Build and run:

```shell
GRAALVM_HOME=/path/to/graalvm ./mvnw gluonfx:build
```

A native binary should be placed at `target/gluonfx/<your architecture>/libremines`