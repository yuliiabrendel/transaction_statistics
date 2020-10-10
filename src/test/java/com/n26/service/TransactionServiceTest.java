package com.n26.service;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.n26.model.Transaction;
import com.n26.service.TransactionService;

/***
 * @author Yuliia Brendel
 */
@SpringBootTest
public class TransactionServiceTest {

	private TransactionService service;
	
	@Before
	public void init() {
		service = new TransactionServiceImpl();
	}
	
	@Test
	public void addTransactionAmountBigDecimalTest() {
		Transaction transaction = new Transaction(new BigDecimal(12.3343), Instant.now());
		service.addTransaction(transaction);
		
		assertNotNull("transactions is null", service.getTransactions());
		assertFalse("transactions is empty", service.getTransactions().isEmpty());
		assertEquals("transactions size does not equal 1", service.getTransactions().size(), 1);
		
		Transaction addedTransaction = service.getTransactions().peek();
		
		assertNotNull("Added transaction is null", addedTransaction);
		assertNotNull("BigDecimal amount is null", addedTransaction.getAmountBigDecimal());
		assertNull("String amount is not null", addedTransaction.getAmount());
		assertNotNull("timestamp is null", addedTransaction.getTimestamp());
		assertEquals("BigDecimal amounts are not equal", addedTransaction.getAmountBigDecimal(), transaction.getAmountBigDecimal());
		assertEquals("Timestamps are not equal", addedTransaction.getTimestamp(), transaction.getTimestamp());
	}
	
	@Test
	public void addTransactionAmountStringTest() {
		Transaction transaction = new Transaction("12.3343", Instant.now());
		service.addTransaction(transaction);
		
		assertNotNull("transactions is null", service.getTransactions());
		assertFalse("transactions is empty", service.getTransactions().isEmpty());
		assertEquals("transactions size does not equal 1", service.getTransactions().size(), 1);
		
		Transaction addedTransaction = service.getTransactions().peek();
		
		assertNotNull("Added transaction is null", addedTransaction);
		assertNotNull("String amount is null", addedTransaction.getAmount());
		assertNull("BigDecimal amount is not null", addedTransaction.getAmountBigDecimal());
		assertNotNull("timestamp is null", addedTransaction.getTimestamp());
		assertEquals("String amounts are not equal", addedTransaction.getAmount(), transaction.getAmount());
		assertEquals("Timestamps are not equal", addedTransaction.getTimestamp(), transaction.getTimestamp());
	}
	
	@Test
	public void addEmptyTransactionTest() {
		Transaction transaction = new Transaction();
		service.addTransaction(transaction);
		
		assertNotNull("transactions is null", service.getTransactions());
		assertFalse("transactions is empty", service.getTransactions().isEmpty());
		assertEquals("transactions size does not equal 1", service.getTransactions().size(), 1);
		
		Transaction addedTransaction = service.getTransactions().peek();
		
		assertNotNull("Added transaction is null", addedTransaction);
		assertNull("String amount is not null", addedTransaction.getAmount());
		assertNull("BigDecimal amount is not null", addedTransaction.getAmountBigDecimal());
		assertNull("timestamp is not null", addedTransaction.getTimestamp());
	}
	
	@Test
	public void getTransactionAmountsTest() {
		Transaction transaction = new Transaction(new BigDecimal(12.3343), Instant.now());
		service.addTransaction(transaction);
		
		List<BigDecimal> transactionAmounts = service.getTransactionAmounts();
		
		assertNotNull("transactionAmounts is null", transactionAmounts);
		assertFalse("transactionAmounts is empty", transactionAmounts.isEmpty());
		assertEquals("transactionAmounts size does not equal 1", transactionAmounts.size(), 1);
		
		BigDecimal transactionAmount = transactionAmounts.get(0);
		assertNotNull("transactionAmount is null", transactionAmount);
		assertEquals("TransactionAmounts are not equal", transactionAmount, transaction.getAmountBigDecimal());
		assertFalse("transaction has experied", service.transactionExpired(service.getTransactions().peek()));
	}
	
	@Test
	public void getTransactionAmountsExpiredTransactionTest() {
		Transaction transaction = new Transaction(new BigDecimal(12.3343), Instant.now().minusSeconds(61));
		assertTrue("transaction has not expired", service.transactionExpired(transaction));
		
		service.addTransaction(transaction);
		
		List<BigDecimal> transactionAmounts = service.getTransactionAmounts();
		
		assertNotNull("transactionAmounts is null", transactionAmounts);
		assertTrue("transactionAmounts is not empty", transactionAmounts.isEmpty());
		assertNotNull("transactions is null", service.getTransactions());
		assertTrue("transactions is not empty", service.getTransactions().isEmpty());
	}
	
	@Test
	public void deleteAllTest() {
		service.addTransaction(new Transaction(new BigDecimal(12.3343), Instant.now()));
		service.addTransaction(new Transaction(new BigDecimal(15.03), Instant.now()));
		service.addTransaction(new Transaction(new BigDecimal(262.01), Instant.now()));
		
		assertFalse("transactions is empty", service.getTransactions().isEmpty());
		
		service.deleteAll();
		
		assertNotNull("transactions is null", service.getTransactions());
		assertTrue("transactions is not empty", service.getTransactions().isEmpty());
	}
	
	@Test
	public void transactionExpiredFalseTest() {
		Transaction transaction = new Transaction(new BigDecimal(12.3343), Instant.now().minusSeconds(59));
		assertFalse("transaction has expired", service.transactionExpired(transaction));
	}
	
	@Test
	public void transactionExpiredTrueTest() {
		Transaction transaction = new Transaction(new BigDecimal(12.3343), Instant.now().minusSeconds(61));
		assertTrue("transaction has not expired", service.transactionExpired(transaction));
	}
	
	@Test
	public void transactionUnprocessableAmountNotParsableTrueTest() {
		assertTrue("Invalid amount is parsable", 
				service.transactionUnprocessable(new Transaction("amount", Instant.now())));
	}
	
	@Test
	public void transactionUnprocessableAmountNotParsableFalseTest() {
		assertFalse("Valid amount is not parsable", 
				service.transactionUnprocessable(new Transaction("12.05", Instant.now())));
	}
	
	@Test
	public void transactionUnprocessableTimestampInFutureTrueTest() {
		assertTrue("timestamp is not in future", 
				service.transactionUnprocessable(new Transaction("amount", Instant.now().plusSeconds(1))));
	}
	
	@Test
	public void transactionUnprocessableTimestampInFutureFalseTest() {
		assertFalse("timestamp is in future", 
				service.transactionUnprocessable(new Transaction("12.05", Instant.now().minusSeconds(1))));
	}
}
