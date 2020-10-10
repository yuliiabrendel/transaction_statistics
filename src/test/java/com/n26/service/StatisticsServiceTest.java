package com.n26.service;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.time.Instant;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.n26.model.Statistics;
import com.n26.model.Transaction;
import com.n26.service.StatisticsService;
import com.n26.service.TransactionService;

/***
 * @author Yuliia Brendel
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class StatisticsServiceTest {
	
	@Autowired
	private StatisticsService statisticsService;

	@Autowired
	private TransactionService transactionService;
	
	@Before
	public void init() {
		transactionService.deleteAll();
	}
	
	@Test
	public void computeStatisticsNoTransactionsTest() {
		Statistics statistics = statisticsService.computeStatistics();
		
		assertNotNull("statistics is null", statistics);
		
		assertNotNull("sum is null", statistics.getSum());
		assertEquals("sum does not equal \"0.00\"", statistics.getSum(), "0.00");
		
		assertNotNull("avg is null", statistics.getAvg());
		assertEquals("avg does not equal \"0.00\"", statistics.getAvg(), "0.00");
		
		assertNotNull("max is null", statistics.getMax());
		assertEquals("max does not equal \"0.00\"", statistics.getMax(), "0.00");
		
		assertNotNull("min is null", statistics.getMin());
		assertEquals("min does not equal \"0.00\"", statistics.getMin(), "0.00");
		
		assertNotNull("count is null", statistics.getCount());
		assertEquals("count does not equal 0", statistics.getCount(), 0);
	}
	
	@Test
	public void computeStatistics() {
		transactionService.addTransaction(new Transaction(new BigDecimal("5"), Instant.now()));
		transactionService.addTransaction(new Transaction(new BigDecimal("3"), Instant.now()));
		transactionService.addTransaction(new Transaction(new BigDecimal("3"), Instant.now()));
		
		Statistics statistics = statisticsService.computeStatistics();
		
		assertNotNull("statistics is null", statistics);
		
		assertNotNull("sum is null", statistics.getSum());
		assertEquals("sum does not equal \"11.00\"", statistics.getSum(), "11.00");
		
		assertNotNull("avg is null", statistics.getAvg());
		assertEquals("avg does not equal \"3.67\"", statistics.getAvg(), "3.67");
		
		assertNotNull("max is null", statistics.getMax());
		assertEquals("max does not equal \"5.00\"", statistics.getMax(), "5.00");
		
		assertNotNull("min is null", statistics.getMin());
		assertEquals("min does not equal \"3.00\"", statistics.getMin(), "3.00");
		
		assertNotNull("count is null", statistics.getCount());
		assertEquals("count does not equal 3", statistics.getCount(), 3);
	}
}
