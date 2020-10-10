package com.n26.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.n26.model.Transaction;

/***
 * @author Yuliia Brendel
 */
public interface TransactionService {
	
	void addTransaction(Transaction transaction);
	
	List<BigDecimal> getTransactionAmounts();
	
	void deleteAll();
	
	boolean transactionExpired(Transaction transaction);
	
	boolean transactionUnprocessable(Transaction transaction);
	
	ConcurrentLinkedQueue<Transaction> getTransactions();
	
}
