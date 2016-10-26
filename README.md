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

# Code Example

## REST API:
1. Find the number of seats available within the venue
GET /v1/tickets/count

2. Find and hold the best available seats on behalf of a customer
GET /v1/tickets?count=3&cust=<email>&offset=0

Response:
{	“groupID”: <groupid>,
	“custEmail”: <custEmail>,
	“noTickets”: <no of tickets>,
	“seatNos”: [ <seatID>, <seatID>, ..],
	“holdTime”: <no of seconds>
}

3. Reserve and commit a specific group of held seats for a customer
POST /v1/tickets?

## Installation

Provide code examples and explanations of how to get the project.

## API Reference

Depending on the size of the project, if it is small and simple enough the reference docs can be added to the README. For medium size to larger projects it is important to at least provide a link to where the API reference docs live.

## Tests

Describe and show how to run the tests with code examples.

## Contributors

Let people know how they can dive into the project, include important links to things like issue trackers, irc, twitter accounts if applicable.

## License

A short snippet describing the license (MIT, Apache, etc.)


ASSUMPTIONS
* We assume the theater has 100 seats with IDs from 1-100. 

* Ideally you would have a Seat class to represent each seat. But to keep things simple, SeatHold has a seatIDs field which is simply a comma-delimited list of Seat numbers.

REST API:
1. Find the number of seats available within the venue
GET /v1/tickets?fields=count&state=avail

2. Find and hold the best available seats on behalf of a customer
GET /v1/tickets?count=3&cust=<email>&offset=0

Response:
{	“groupID”: <groupid>,
	“custEmail”: <custEmail>,
	“noTickets”: <no of tickets>,
	“seatNos”: [ <seatID>, <seatID>, ..],
	“holdTime”: <no of seconds>
}

3. Reserve and commit a specific group of held seats for a customer
POST /v1/tickets?


Customer: Tickets required – 4
System: 4 Tickets Held – 

KNOWN LIMITATIONS:
•	REST API can be improved - having something like -
		/seats/count?state=avail instead of /tickets/count to get number of seats available
		We can have separate controller handing /seats and /tickets URI space
•	Seats are not on demand. So one can’t request a certain set of seats in the current implementation. But it can be done.
•	SeatHolds are not released constantly. They are released when you either call numSeatsAvailable or findAndHoldSeats methods. This was done to minimize complexity and also since it wasn’t required to be done constantly for the purpose of this assignment.
