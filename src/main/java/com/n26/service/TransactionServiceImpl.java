package com.n26.service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.springframework.stereotype.Service;

import com.n26.model.Transaction;

/***
 * @author Yuliia Brendel
 */
@Service
public class TransactionServiceImpl implements TransactionService {
	
	private final long expiryTimeMillis = 60000;

	private final ConcurrentLinkedQueue<Transaction> transactions = new ConcurrentLinkedQueue<Transaction>();
	
	@Override
	public void addTransaction(Transaction transaction) {
		transactions.add(transaction);
	}
	
	@Override
	public List<BigDecimal> getTransactionAmounts() {
		List<BigDecimal> amounts = Collections.synchronizedList(new ArrayList<BigDecimal>());
		
		transactions.parallelStream().forEach(transaction -> {
			if (transactionExpired(transaction))
				transactions.remove(transaction);
			else 
				amounts.add(transaction.getAmountBigDecimal());
		});
		
		return amounts;
	}
	
	@Override
	public void deleteAll() {
		transactions.clear();
	}

	@Override
	public boolean transactionExpired(Transaction transaction) {
		return transaction.getTimestamp().isBefore(Instant.ofEpochMilli(System.currentTimeMillis() - expiryTimeMillis));
	}
	
	@Override
	public boolean transactionUnprocessable(Transaction transaction) {
		return transactionAmountNotParsable(transaction) || transactionTimestampInFuture(transaction);
	}

	@Override
	public ConcurrentLinkedQueue<Transaction> getTransactions() {
		return transactions;
	}
	
	private boolean transactionAmountNotParsable(Transaction transaction) {
		try {
			transaction.setAmountDecimal(new BigDecimal(transaction.getAmount()));
		} catch (Exception e) {
			return true;
		}
		return false;
	}
	
	private boolean transactionTimestampInFuture(Transaction transaction) {
		return transaction.getTimestamp().isAfter(Instant.now());
	}
}
