package org.kedar.ticketservice;

import javax.annotation.PostConstruct;

import org.kedar.ticketservice.service.TicketService;
import org.kedar.ticketservice.vo.SeatHold;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Configuration
@EnableAutoConfiguration
@ComponentScan
@RequestMapping("/v1/tickets")
public class TicketController {
	@Autowired
	TicketService ticketService;
	private static final Logger logger = LoggerFactory.getLogger(TicketController.class);
	
	@PostConstruct
	public void init() {
		System.out.println("Initializing..");
	}
	
	/**
	 * The number of seats in the venue that are neither held nor reserved
	 *
	 * @return the number of tickets available in the venue
	 */
	@RequestMapping(method=RequestMethod.GET, 
			produces = { "application/json" })
    public String query(
    		@RequestParam(value="fields", required=false, defaultValue="count") String fields) {
		logger.info("###### Querying Available Tickets ######");
		if (fields.equals("count")) {
			int count = this.ticketService.numSeatsAvailable();
			return String.format("{ \"seats\" : { \"available\" : %d } }", count);
		} else {
			return String.format("{ \"seats\" : { \"error\" : \"Field %s Not Supported\" } }", fields);
		}
    }
	
	/**
	 * Find and hold the best available seats for a customer
	 *
	 * @param numSeats the number of seats to find and hold
	 * @param customerEmail unique identifier for the customer
	 * @return a SeatHold object identifying the specific seats and related information
	 */
	@RequestMapping(method=RequestMethod.POST,
			produces = { "application/json" })
	@ResponseBody
	public SeatHold setHold(
			@RequestParam(value="num", required=false, defaultValue="1") Integer numSeats,
			@RequestParam(value="email", required=false, defaultValue="") String customerEmail) {
		logger.info(String.format("###### Requesting Hold for %d Seats ######", numSeats));
		SeatHold seatHold = this.ticketService.findAndHoldSeats(numSeats.intValue(), customerEmail);
		return seatHold;
	}

	/**
	* Commit seats held for a specific customer
	*
	* @param seatHoldId the seat hold identifier
	* @param customerEmail the email address of the customer to which the
	seat hold is assigned
	* @return a reservation confirmation code
	*/
	@RequestMapping(method=RequestMethod.PUT, 
			value="/{holdId}",
			produces = { "application/json" })
	@ResponseBody
	public String reserve(
			@PathVariable("holdId") int seatHoldId,
			@RequestParam(value="email", required=false, defaultValue="") String customerEmail) {
		logger.info(String.format("###### Reserving TicketHold #%d ######", seatHoldId));
		String reservationId = this.ticketService.reserveSeats(seatHoldId, customerEmail);
		return String.format("{ \"reservation\" : \"%s\" }", reservationId);
	}

	public TicketService getTicketService() {
		return ticketService;
	}

	public void setTicketService(TicketService ticketService) {
		this.ticketService = ticketService;
	}

	public static void main(String[] args) throws Exception {
        SpringApplication.run(TicketController.class, args);
    }
}