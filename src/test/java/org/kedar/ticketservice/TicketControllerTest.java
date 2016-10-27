package org.kedar.ticketservice;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kedar.ticketservice.service.TicketService;
import org.kedar.ticketservice.vo.SeatHold;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

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

	public void testQuery() {
		String content = this.restTemplate.getForObject(
				"http://localhost:8080/v1/tickets?fields=count&state=avail", String.class);
		//SeatHold seatHold = this.json.parse(content);
	}
	
	@Test
	public void testSetHold() throws IOException {
		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
		params.set("num", "3");
		params.set("email", "a@b.com");
		ResponseEntity<String> jsonResponse = this.restTemplate.postForEntity(
				"http://localhost:8080/v1/tickets", params , String.class);
		assertEquals(HttpStatus.OK, jsonResponse.getStatusCode());
	}
	
	public void test() {
		fail("Not yet implemented");
	}

}
