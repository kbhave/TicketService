package org.kedar.ticketservice;

import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kedar.ticketservice.service.TicketService;
import org.kedar.ticketservice.vo.SeatHold;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.DEFINED_PORT)
public class TicketControllerTest {

	private JacksonTester<SeatHold> json;
	
	@Autowired
	private TestRestTemplate restTemplate;
	
	@MockBean
    private TicketService ticketService;
	
	@Before
    public void setup() {
		
	}

	@Test
	public void testQuery() {
		String content = this.restTemplate.getForObject(
				"http://localhost:8080/v1/tickets?fields=count&state=avail", String.class);
		//SeatHold seatHold = this.json.parse(content);
	}
	
	@Test
	public void testSetHold() {
		String content = this.restTemplate.getForObject(
				"http://localhost:8080/v1/tickets?num={num}&email={email}", String.class, "3", "a@b.com",
				String.class);
		
	}
	
	@Test
	public void test() {
		fail("Not yet implemented");
	}

}
