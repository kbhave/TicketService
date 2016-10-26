package org.kedar.ticketservice.vo;

import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.kedar.ticketservice.utils.Constants;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * SeatHold: This is essentially what the TicketService will return in JSON format.
 * 		Each SeatHold is per customer request, It has -
 * 			No of seats held for the customer
 * 			SeatIDs of the seats held
 * 			CustomerEmail
 * 			Amount of time the seats are held
 * 
 * Assumptions: We assume the theater has 100 seats with IDs from 1-100. 
 * Ideally you would have a Seat class to represent each seat.
 * 
 * @author kedar
 *
 */
@Configuration
public class SeatHold {
	private static AtomicInteger ID_GENERATOR = new AtomicInteger(1000);
	
	private Integer id;
	
	private Set<Integer> seatIDSet;
	
	private String email;
	
	private Integer noSeats;

	@JsonIgnore
	private Boolean expired = false;
	
	private long expiryTime;
	
	/**
	 * For JPA
	 */
	public SeatHold() {
	}

	/**
	 * @param id
	 * @param seatIDSet
	 * @param email
	 * @param noSeats
	 */
	public SeatHold(Set<Integer> seatIDSet, String email, Integer noSeats) {
		this.id = ID_GENERATOR.getAndIncrement();
		this.seatIDSet = seatIDSet;
		this.email = email;
		this.noSeats = noSeats;
		
		//Set the expiry time for this object
		this.expiryTime = System.currentTimeMillis() + (Constants.EXPIRY_SECONDS * 1000);
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getNoSeats() {
		return noSeats;
	}

	public void setNoSeats(Integer noSeats) {
		this.noSeats = noSeats;
	}

	public Set<Integer> getSeatIDSet() {
		return seatIDSet;
	}

	public void setSeatIDSet(Set<Integer> seatIDSet) {
		this.seatIDSet = seatIDSet;
	}

	public Boolean getExpired() {
		return (System.currentTimeMillis() >= this.expiryTime);
	}

	/**
	 * This gives us our Reservation Id
	 */
	@JsonIgnore
	public String getReservationId() {
		return String.format(
				"R%d-%d-%s", this.id, this.noSeats, this.email
			);
	}
	public String toString() {
		return String.format(
				"[ID=%d][%s][%s]", this.id, this.seatIDSet.toString(), this.email
			);
	}
}
