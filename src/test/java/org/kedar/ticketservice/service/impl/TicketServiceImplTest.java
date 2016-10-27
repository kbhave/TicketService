package org.kedar.ticketservice.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import org.kedar.ticketservice.service.TicketService;
import org.springframework.boot.test.mock.mockito.MockBean;

/**
 * INCOMPLETE - (due to time)
 * @author kedar
 */
public class TicketServiceImplTest {
	@MockBean
	private TicketService ticketService;
	
	@Before
	public void setUp() throws Exception {
		this.ticketService = new TicketServiceImpl();
	}

	@Test
	public void testNumSeatsAvailable() {
		assertEquals(0, this.ticketService.numSeatsAvailable());
	}

	public void testFindAndHoldSeats() {
		
	}

	public void testReserveSeats() {
	}

}
