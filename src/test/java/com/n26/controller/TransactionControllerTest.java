package com.n26.controller;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.n26.service.TransactionService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;

import static org.mockito.Mockito.verifyZeroInteractions;

/***
 * @author Yuliia Brendel
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TransactionControllerTest {
	
	@Autowired
    private MockMvc mvc;
	
	@Mock
	private TransactionService service;
	
	@Test
	public void addTransactionValidRequestTest() throws Exception {
		mvc.perform(post("/transactions")
				.contentType("application/json")
				.content("{\"amount\": \"12.3343\",\"timestamp\": \"" + Instant.now().toString() + "\"}"))
		        .andExpect(status().isCreated())
		        .andExpect(content().string(""));
	}
	
	@Test
	public void addExpiredTransactionTest() throws Exception {
		mvc.perform(post("/transactions")
				.contentType("application/json")
				.content("{\"amount\": \"12.3343\",\"timestamp\": \"" + Instant.now().minusSeconds(100).toString() + "\"}"))
		        .andExpect(status().isNoContent())
		        .andExpect(content().string(""));
		
		verifyZeroInteractions(service);
	}
	
	@Test
	public void addTransactionInvalidJsonTest() throws Exception {
		mvc.perform(post("/transactions")
				.contentType("application/json")
				.content("{\"amount\": ,\"timestamp\": \"" + Instant.now().toString() + "\"}"))
		        .andExpect(status().isBadRequest())
		        .andExpect(content().string(""));
		
		verifyZeroInteractions(service);
	}
	
	@Test
	public void addTransactionAmountNotParsableTest() throws Exception {
		mvc.perform(post("/transactions")
				.contentType("application/json")
				.content("{\"amount\": \"five\",\"timestamp\": \"" + Instant.now().toString() + "\"}"))
		        .andExpect(status().isUnprocessableEntity())
		        .andExpect(content().string(""));
		
		verifyZeroInteractions(service);
	}
	
	@Test
	public void addTransactionInFutureTest() throws Exception {
		mvc.perform(post("/transactions")
				.contentType("application/json")
				.content("{\"amount\": \"12.3343\",\"timestamp\": \"" + Instant.now().plusSeconds(1).toString() + "\"}"))
		        .andExpect(status().isUnprocessableEntity())
		        .andExpect(content().string(""));
		
		verifyZeroInteractions(service);
	}
	
	@Test
	public void deleteAllTransactionsTest() throws Exception {
		mvc.perform(delete("/transactions")
				.contentType("application/json")
				.content("{}"))
		        .andExpect(status().isNoContent())
		        .andExpect(content().string(""));
	}
}
