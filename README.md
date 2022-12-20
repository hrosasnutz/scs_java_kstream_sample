# Spring Cloud Stream with Kafka Streams using Java
Demo project for Kafka Streams using SCS and Java 17

## Host

To run this project, you will need to add the following dns to hosts file

`127.0.0.1 kafka`


## Usage/Examples

Start docker stack with next command:
```bash
docker-compose up -d
```
Build application:
```bash
gradlew build -x test
java -jar build/libs/scs_java_kstream_sample-0.0.1-SNAPSHOT.jar --spring.profiles.active=local
```
Sample response:
```log
2022-12-20 09:46:15.066  INFO 18616 --- [-StreamThread-1] i.s.s.analytics.CustomerAnalytics        : Total 128 customers for LOYAL type.
2022-12-20 09:46:15.066  INFO 18616 --- [-StreamThread-1] i.s.s.analytics.CustomerAnalytics        : Total 129 customers for INTERMEDIATE type.
2022-12-20 09:46:26.307 DEBUG 18616 --- [pool-3-thread-1] i.s.s.producer.CustomerProducer          : send customer message: GenericMessage [payload=Customer(id=fab03e2b-4b23-4109-bea0-a3941450d483, name=Ambrose Hills, type=FORMER, active=false, nationality=lu, birthdate=1997-01-26, createdAt=2022-12-20T09:46:26.307002500), headers={kafka_messageKey=[B@5120f9df, id=9c480c14-62f1-77fb-e1cc-a78bd411cff4, timestamp=1671547586307}]
2022-12-20 09:46:26.307 DEBUG 18616 --- [pool-3-thread-1] i.s.s.producer.CustomerProducer          : send address message: GenericMessage [payload=Address(id=43676f40-ff43-4403-84bb-8ca1e74e569a, customerId=fab03e2b-4b23-4109-bea0-a3941450d483, fullAddress=21059 Robert Plaza, New Leesa, AZ 16748, street=8457 Samantha Cape, zipCode=11451, city=Rosarioport, state=Oregon, country=Country(code=NR, name=Qatar), createdAt=2022-12-20T09:46:26.307973400), headers={kafka_messageKey=[B@7487b145, id=29c16cc7-4f40-3833-a83c-a74026e2126f, timestamp=1671547586307}]
2022-12-20 09:46:26.417  INFO 18616 --- [-StreamThread-1] i.s.s.analytics.CustomerAnalytics        : Materialized View Customer And Address: MVCustomersAndAddresses(customer=Customer(id=fab03e2b-4b23-4109-bea0-a3941450d483, name=Ambrose Hills, type=FORMER, active=false, nationality=lu, birthdate=1997-01-26, createdAt=2022-12-20T09:46:26.307002500), address=Address(id=43676f40-ff43-4403-84bb-8ca1e74e569a, customerId=fab03e2b-4b23-4109-bea0-a3941450d483, fullAddress=21059 Robert Plaza, New Leesa, AZ 16748, street=8457 Samantha Cape, zipCode=11451, city=Rosarioport, state=Oregon, country=Country(code=NR, name=Qatar), createdAt=2022-12-20T09:46:26.307973400))
2022-12-20 09:46:29.010  INFO 18616 --- [-StreamThread-1] i.s.s.analytics.CustomerAnalytics        : Total 132 customers for FORMER type.
2022-12-20 09:46:29.290  INFO 18616 --- [-StreamThread-1] i.s.s.analytics.AddressAnalytics         : Country: Country(code=NR, name=Thailand)
```