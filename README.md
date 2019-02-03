# ACES Spring Boot Server Library

This is a library for implementing ACES Listener and ACES Service web servers 
using Java and Spring Boot.


## Usage

Add the following to your `pom.xml` dependencies:

```xml
<repository>
    <id>bintray-ark-aces-aces-maven</id>
    <url>https://dl.bintray.com/ark-aces/aces-maven</url>
</repository>
```
```
<dependency>
    <groupId>com.arkaces</groupId>
    <artifactId>aces_server_lib</artifactId>
    <version>5.11.0</version>
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

Add the following properties to your `application.yml` config:

```yaml
serverInfo:
  name: "MyListener"
  description: "My custom ACES Listener server."
  version: "1.0.0"
  websiteUrl: "https://domain.com/my-listener-website"
```

### ACES Service

Import Spring components into your Spring application for ACES Service web applications:

```java
@Configuration
@Import(AcesServiceConfig.class)
public class ApplicationConfig {

}
```
