# Libremines

"named this way because i didn't realise there was another libremines!"

![Libremines screenshot](https://github.com/shrapnelnet/libremines/assets/133451255/f19e0006-a587-4f39-8626-67606db7cd58)


## Building

### Requirements:
- Java 21 or newer (openjdk-21-jdk)
- Maven 3.8.8 (note: some versions cause gluon to freak out)
- GraalVM 21

### Instructions:

Create the maven wrapper. gluonfx does not like versions of maven that are not 3.8.8:

```shell
mvn wrapper:wrapper -Dmaven=3.8.8
```

This is helpful for version management, in case you have an incompatible version of maven installed.
Install dependencies:

```shell
./mvnw install
```

Build and run:

```shell
GRAALVM_HOME=/path/to/graalvm ./mvnw gluonfx:build
```

A native binary should be placed at `target/gluonfx/<your architecture>/libremines`
