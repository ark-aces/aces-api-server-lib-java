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
    <version>5.4.0</version>
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


### ARK Auth

Ark Auth adds API key authorization with Ark Stake and daily fee
for API usage. 

This module should only be used with ACES nodes that
use `https` since sensitive API keys are sent in web requests.

First import the `ArkAuthConfig` module:

```
@Configuration
@Import({AcesServiceConfig.class, ArkAuthConfig.class})
public class ApplicationConfig {

}
``` 

You must add the following to your application config:

```yaml
arkAuth:
  # Ark Network to use for account lookups
  localArkNetwork:
    name: "localnet"
  
  # Ark Network to use for transaction broadcasts
  arkNetwork:
    name: "mainnet"
  
  # The address that fees are sent to
  serviceArkAddress: "ARNJJruY6RcuYCXcwWsu4bx9kyZtntqeAx"
  
  # Amount of ARK required in your stake account address
  minArkStake: "100.00"

  # Amount of ARK to charge payment account every 24 hours to keep
  # API key active
  arkFee: "1.00"
```

For each ark network name, you must include a network config
file under `ark-network-config/{networkName}.yml` (See example
configurations in [src/main/resources/ark_auth/](src/main/resources/ark_auth/))

Users can obtain an API Key by posting to the `/apiKeys` endpoint:

```
curl -X POST 'https://localhost/accounts' \
> -H 'Content-type: application/json' \
> -d '{"userArkAddress": "ARNJJruY6RcuYCXcwWsu4bx9kyZtntqeAx"}'
{
  "id" : "xhpwcyuUH8e5tYkLYDuQ",
  "status" : "inactive",
  "apiKey" : "GxlDQtN7PrJW315kxQitTYS5PTme5CfGsM7XZzOF",
  "userArkAddress" : "ARNJJruY6RcuYCXcwWsu4bx9kyZtntqeAx",
  "paymentArkAddress" : "AYwhuy5HNRdxZ6cCeDrVaTDhTfi5AxnHjk",
  "createdAt" : "2017-12-18T06:52:53.349Z"
}
```

The user must send an ark transaction from the `userArkAddress`
to the service controlled `paymentArkAddress` to verify ownership
of the address for staking.

Funds added to `paymentArkAddress` will be used to pay daily
activation fees if required by service configuration.

The API Key will be `active` when a verified `userArkAddress`
contains the required ARK stake amount and daily usage fees
are paid. If the stake drops below the configured value or
there is not enough funds in `paymentArkAddress` to cover the
daily fee, the API key becomes inactivated.

The user can now authenticate with the service using HTTP 
Basic Auth with username equal to the Account `id` and password equal to the
Account `apiKey` value:

```
curl -X POST 'https://localhost/subscriptions' \
-u xhpwcyuUH8e5tYkLYDuQ:GxlDQtN7PrJW315kxQitTYS5PTme5CfGsM7XZzOF
-H 'Content-type: application/json' \
-d '{
   "callbackUrl": "https://my-app.domain.com/events",
   "minConfirmations": "5"
}'
```

