# Synopsis

Implement a simple ticket service that facilitates the discovery, temporary hold, and final reservation of seats within a high-demand performance venue.

```sh
Implement a Ticket Service that provides the following functions:
1.  Find the number of seats available within the venue
    Note: available seats are seats that are neither held nor reserved.
2.  Find and hold the best available seats on behalf of a customer
    Note: each ticket hold should expire within a set number of seconds.
3.  Reserve and commit a specific group of held seats for a customer
```

# Code

The project uses Spring Boot to take advantage of embedded Tomcat, Starter POMs, and to create a single executable JAR. It uses Spring REST annotations to implement RestController and makes use of native Jackson for JSON responses, etc. It uses Maven as the build tool.

I had initially implemented this using the in-memory database H2 with JPA but then decided to keep it simple using simple Data Structures in memory, so in either case, no persistence was planned in the implementation.

SeatHold is a self-expiring object with a very rudimentary expiry logic (with a expired or not flag). I could have used caching options for SeatHold, which have built-in expiry logic - such as Ehcache - but kept my current implementation for simplicity.

Have used JUnit with Spring-test to unit test HTTP endpoints. I might not complete this part though due to time constraints.

## Error Handling
Spring Boot provides the /error mapping by default to handle errors gracefully. I have also included a small method 'handleException' to handle specific exceptions like IllegalArgumentException and NullPointerException in the RestController.

## Installation and instructions to run

Build using the following command:
mvn -U clean install

Run using the command:
mvn spring-boot:run
or 
java -jar target/ticketservice-1.0.0.jar

A log file will be created in /tmp.

## API Reference

#### Find the number of seats available within the venue (the idea is to extend this further)
```sh
GET http://localhost:8080/v1/tickets?fields=count&state=avail

HTTP/1.1 200 
Content-Type: application/json;charset=UTF-8
Content-Length: 35
Date: Wed, 26 Oct 2016 15:03:00 GMT

{ "seats" : { "available" : 100 } }
```

#### Find and hold the best available seats on behalf of a customer
```sh
POST http://localhost:8080/v1/tickets?num=3&email=a@b.com

Response:
HTTP/1.1 200 
Content-Type: application/json;charset=UTF-8
Transfer-Encoding: chunked
Date: Wed, 26 Oct 2016 15:03:14 GMT

{
  "id": 1000,
  "seatIDSet": [
    1,
    2,
    3
  ],
  "email": "x@y.com",
  "noSeats": 3
}
```

#### Reserve and commit a specific group of held seats for a customer
```sh
PUT http://localhost:8080/v1/tickets/1001?email=x@y.com

Response:
HTTP/1.1 200 
Content-Type: application/json;charset=UTF-8
Content-Length: 30
Date: Wed, 26 Oct 2016 15:03:34 GMT

{ "reservation" : "R1002-3-x@y.com" }
```

## Tests

#### Integration Testing using CURL
Have used Mocha in the past to do integration testing.
```sh
  - curl -i -X GET http://localhost:8080/v1/tickets?fields=count&state=avail

  - curl -i -X POST http://localhost:8080/v1/tickets?num=3&email=a@b.com

  - curl -i -X PUT http://localhost:8080/v1/tickets/{holdId}
	holdId from the POST command for holding seats
```

#### Unit Tests
```sh
Implemented a couple of sample unit tests using JUnit, Mockito with spring-test.
Could not do more due to time constraints.
```

## ASSUMPTIONS
* We assume the theater has 100 seats with IDs from 1-100. There is no concept of rows.

* Ideally you would have a Seat class to represent each seat. But to keep things simple, SeatHold has a seatIDs field which is simply a Set of Seat numbers.

## KNOWN LIMITATIONS:
* REST API definition can be improved - having something like -
	/seats/count?state=avail instead of /tickets/count to get number of seats available. 
	We can have separate controller handing /seats and /tickets URI space
	Error handling can be improved.
* The API is not using SSL, auth tokens and such..
* No filtering, sorting / paging has been implemented
* Seats are not on demand. So one can’t request a certain set of seats in the current implementation. But it can be done.
* SeatHolds are not released as soon as they expire. They are released when you either call numSeatsAvailable or findAndHoldSeats methods because that's when we manually check if they have expired. This was done to minimize complexity and also since it wasn’t required to be done constantly for the purpose of this assignment.
* reserveSeats : Does not reserve "Best Available" seats. There is no algorithm considered/developed in this implementation to determine the quality of seats.
