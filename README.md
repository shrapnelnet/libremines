# Libremines

"named this way because i didn't realise there was another libremines!"

![Libremines screenshot](https://github.com/user-attachments/assets/e0a4e551-afdf-4fc5-91c5-7ff45d36cb64)

## Building

### Requirements:
- Java 21
- Maven

### Instructions:

This will automatically fetch dependencies and run the project:

#### Linux:

```shell
./mvnw javafx:run
```

```powershell
./mvnw.cmd javafx:run
```

#### Native Image:

##### Requirements:
- GraalVM
- Maven 3.8.8

Create the maven wrapper at version 3.8.8. GluonFX does not work with newer versions as of 2024-07-27.

```shell
mvn wrapper:wrapper -Dmaven=3.8.8
```

Install dependencies, build and run:

```shell
./mvnw dependency:resolve gluonfx:build
```

A native binary should be placed at `target/gluonfx/<your architecture>/libremines`
