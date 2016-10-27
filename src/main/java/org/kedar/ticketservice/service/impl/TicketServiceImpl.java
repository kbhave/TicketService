package org.kedar.ticketservice.service.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.IntStream;

import javax.annotation.PostConstruct;

import org.kedar.ticketservice.service.TicketService;
import org.kedar.ticketservice.utils.Constants;
import org.kedar.ticketservice.vo.SeatHold;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

/**
 * Service Implementation of TicketService interface.
 * @author kedar
 *
 */
@Configuration
public class TicketServiceImpl implements TicketService {
	private static final Logger logger = LoggerFactory.getLogger(TicketServiceImpl.class);
	Set<Integer> availSeats = new TreeSet<Integer>();	// Need the seats to be ordered
	Set<Integer> reservedSeats = new HashSet<Integer>();
	
	Map<Integer, SeatHold> SHMap = new HashMap<Integer, SeatHold>();

	/**
	 * numSeatsAvailable
	 * Purpose: find number of seats available (not held or reserved) at any given time
	 */
	@Override
	public synchronized int numSeatsAvailable() {
		this.refreshAvailableSeats();
		int available = availSeats.size();
		return available;
	}

	/**
	 * findAndHoldSeats
	 * Purpose: find numSeats number of seats and hold them for this customer
	 * 
	 * 1. Get next numSeats number of seats first
	 * 2. Create the new SeatHold object and add it to the SHMap
	 */
	@Override
	public synchronized SeatHold findAndHoldSeats(int numSeats, String customerEmail) {
		Set<Integer> seatNos = getNextNSeats(numSeats);

		SeatHold newSeatHold = new SeatHold(seatNos, customerEmail, numSeats);
		SHMap.put(newSeatHold.getId(), newSeatHold);
		
		return newSeatHold;
	}
	/**
	 * refreshAvailableSeats
	 * Purpose: Refresh the Available Seats because some holds might have expired
	 */
	private void refreshAvailableSeats() {
		Iterator<Integer> iterator = SHMap.keySet().iterator();
		while (iterator.hasNext()) {
			Integer SHId = iterator.next();
			SeatHold SH = SHMap.get(SHId);
			/* If SeatHold has expired, 
			 * -- add the seats back to availSeats
			 * -- Remove the SeatHold from SHMap
			 */
			if (SH.getExpired()) {
				logger.debug(String.format(
						"SeatHold with id %d has expired. Releasing %d seats", 
						SH.getId(), 
						SH.getNoSeats()));
				availSeats.addAll(SH.getSeatIDSet());
				iterator.remove();
				
			} else {
				// Make sure the seats for this Hold are not in availSeats
				availSeats.removeAll(SH.getSeatIDSet());
			}
		}
		
	}
	/**
	 * getNextNSeats
	 * Purpose: Get the specified number of seats from the Available seats
	 * 
	 * 1. Refresh the available seats (to make sure all the expired holds are gone)
	 * 2. Make sure all reserved seats are erased from available seats set
	 * 3. Return the required number of seats
	 * 4. Remove these from available seats set
	 * 
	 * @param numSeats
	 * @return
	 */
	private Set<Integer> getNextNSeats(int numSeats) {
		Set<Integer> retSet = new HashSet<Integer>();

		// Refresh available seats to get rid of Expired holds
		this.refreshAvailableSeats();
		// Remove all the reservedSeats from availSeats (if there are any)
		availSeats.removeAll(reservedSeats);

		/* Now,
		 * -- return numSeats from availSeats,
		 * -- remove them from availSeats
		 */
		for (Integer seat : availSeats) {
			retSet.add(seat);
			if (--numSeats == 0) break;
		}
		availSeats.removeAll(retSet);	// Update allSeats
		
		return retSet;
	}

	/**
	 * reserveSeats
	 * Purpose: Reserve seats from a given hold.
	 * 
	 * 1. Add to Reserved Seats set, 
	 * 2. Remove from Available Seats set and 
	 * 3. Remove the hold from the hold map.
	 * 
	 */
	@Override
	public synchronized String reserveSeats(int seatHoldId, String customerEmail) {
		SeatHold SH = SHMap.get(seatHoldId);
		if (SH != null) {
			logger.debug(String.format("Reserving %d seats from SeatHold #%d for %s", 
					SH.getNoSeats(), SH.getId(), SH.getEmail()));
			
			this.reservedSeats.addAll(SH.getSeatIDSet());
			this.availSeats.removeAll(SH.getSeatIDSet());
			SHMap.remove(SH.getId());
			
			return SH.getReservationId();
		}
		
		return null;
	}

	/**
	 * Just to set up some test data
	 */
	@PostConstruct
	private void initTheater() {
		System.out.println("Here");
		// Initialize all the seats in the theater
		IntStream.rangeClosed(1, Constants.TOTAL_SEATS).forEach(
				i ->  
				availSeats.add(i)
		);
	}

}
