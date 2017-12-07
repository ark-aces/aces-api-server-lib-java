# ACES Spring Boot Server Library

This is a library for implementing ACES Listener and ACES Service web servers 
using Java and Spring Boot.


## Installation

```
mvn install
```

## Usage

Add the following to your `pom.xml` dependencies:

```xml
<dependency>
    <groupId>com.arkaces</groupId>
    <artifactId>aces_server_lib</artifactId>
    <version>5.0.0</version>
</dependency>
```

### ACES Listener

Import Spring components into your Spring application for ACES Listener web applications:

```java
@Configuration
@Import(AcesListenerConfig.class)
public class ApplicationConfig {

}
```

### ACES Service

Import Spring components into your Spring application for ACES Service web applications:

```java
@Configuration
@Import(AcesServiceConfig.class)
public class ApplicationConfig {

}
```





