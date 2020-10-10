package com.n26.controller;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.mockito.Mockito.when;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.n26.model.Statistics;
import com.n26.service.StatisticsService;

/***
 * @author Yuliia Brendel
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class StatisticsControllerTest {
	
	@Autowired
    private MockMvc mvc;
	
	@MockBean
	private StatisticsService statisticsService;
	
	@Test
	public void computeStatisticsTest_() throws Exception {
		
	}
	
	@Test
	public void computeStatisticsTest() throws Exception {
		when(statisticsService.computeStatistics()).thenReturn(new Statistics("10.00", "3.33", "6.00", "2.00", 3));
		
		mvc.perform(get("/statistics")
				.contentType("application/json")
				.content("{}"))
		        .andExpect(status().isOk())
		        .andExpect(content().contentTypeCompatibleWith("application/json"))
		        .andExpect(jsonPath("sum", equalTo("10.00")))
		        .andExpect(jsonPath("avg", equalTo("3.33")))
		        .andExpect(jsonPath("max", equalTo("6.00")))
		        .andExpect(jsonPath("min", equalTo("2.00")))
		        .andExpect(jsonPath("count", equalTo(3)));
	}
	
	@Test
	public void computeStatisticsNoTransactionsTest() throws Exception {
		when(statisticsService.computeStatistics()).thenReturn(new Statistics());
		
		mvc.perform(get("/statistics")
				.contentType("application/json")
				.content("{}"))
		        .andExpect(status().isOk())
		        .andExpect(content().contentTypeCompatibleWith("application/json"))
		        .andExpect(jsonPath("sum", equalTo("0.00")))
		        .andExpect(jsonPath("avg", equalTo("0.00")))
		        .andExpect(jsonPath("max", equalTo("0.00")))
		        .andExpect(jsonPath("min", equalTo("0.00")))
		        .andExpect(jsonPath("count", equalTo(0)));
	}
}
