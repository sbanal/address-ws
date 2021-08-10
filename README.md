
# Address Web Service

This is a RESTful web service which exposes CRUD operations of Australian suburbs. 

# Pre-requisite

* Gradle (this was built using v6.5)
* JDK 1.8+

# Build, Test and Run

## Run build and tests
```
gradle clean build
```
Use *gradlew* for windows.

The built jar file can be found in the folder "[project-dir]/build/libs/aupost-coding-challenge-0.0.1-SNAPSHOT.jar*.

## Run Service
Run this service using the sample data.
```
java -jar aupost-coding-challenge-0.0.1-SNAPSHOT.jar
```

# Import Data

To run this service using your own data, specify *--spring.datasource.data=file:[file-path]* parameter.
Example:
```
java -jar build\libs\aupost-coding-challenge-0.0.1-SNAPSHOT.jar --spring.datasource.data=file:./src/test/resources/suburbs-test.sql
```

The data file should follow sql format as shown below:
```
INSERT INTO suburb (name, post_code) VALUES
    ('Truganina', 3029),
    ('Tarneit', 3029),
    ('Hoppers Crossing', 3029),
    ('Melbourne', 3000);
```

# Endpoint Definitions

| Operation | HTTP Method | Endpoint URL | Notes |
| --------- | ----------- | ------------ | ------------ |
| Create Suburb | POST | http://localhost:8080/address/suburbs/ | |
| Get Suburb of given id | GET | http://localhost:8080/address/suburbs/{id} | Id is the entity ID and not the post code |
| Get all suburbs | GET |  http://localhost:8080/address/suburbs{?name,page,size,sort} | Refer to spring data manual for valid parameters on page, size and sort |
| Delete suburb | GET |  http://localhost:8080/address/suburbs/{id} | Id is the entity ID and not the post code |
| Update suburb | PUT |  http://localhost:8080/address/suburbs/{id} | Id is the entity ID and not the post code |
| Search by postcode | GET | http://localhost:8080/address/suburbs/search/postCode{?code,page,size,sort} | Post Code is in the range [0, Int max]. Refer to spring data manual for valid parameters on page, size and sort |
| Search by suburb name | GET | http://localhost:8080/address/suburbs/search/name{?name,page,size,sort} | Suburb Name must be at least 3 characters. Refer to spring data manual for valid parameters on page, size and sort. Query is case in-sensitive and is a starts-with query. |

Suburb Entity JSON should be submitted in the HTTP body for create and update operations:
```
{name: "suburbname", postCode: 12345}
```

This endpoint uses *Spring Data Rest*. For a complete reference on page, size and sort parameter values please refer to the [manual](https://docs.spring.io/spring-data/rest/docs/current/reference/html/#repository-resources.collection-resource).

The REST endpoints can be viewed and tested using [Swagger UI](http://localhost:8080/swagger-ui/index.html).

# Accounts

To create, delete or update new Suburbs basic authentication is required. The current test account is:
```
username: apiuser
password: test
```
