package com.n26.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/***
 * @author Yuliia Brendel
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Transaction {
	
	@JsonIgnore
	private volatile BigDecimal amountBigDecimal;
    private volatile String amount;
    private volatile Instant timestamp;
	
	public Transaction() {}
	
	public Transaction(BigDecimal amountBigDecimal, Instant timestamp) {
		this.amountBigDecimal = amountBigDecimal;
		this.timestamp = timestamp;
	}
	
	public Transaction(String amount, Instant timestamp) {
		this.amount = amount;
		this.timestamp = timestamp;
	}
	
	public BigDecimal getAmountBigDecimal() {
		return amountBigDecimal;
	}

	public void setAmountDecimal(BigDecimal amountBigDecimal) {
		this.amountBigDecimal = amountBigDecimal;
	}

	public String getAmount() {
		return amount;
	}

	public Instant getTimestamp() {
		return timestamp;
	}
	
	@Override
	public boolean equals(Object o) {
		 if (this == o) return true;
		 if (o == null || !(o instanceof Transaction)) return false;
		 Transaction that = (Transaction) o;
		 return Objects.equals(this.amountBigDecimal, that.amountBigDecimal) && 
				Objects.equals(this.timestamp, that.timestamp);
	} 
	
	@Override
	public int hashCode() {
		return Objects.hash(amountBigDecimal, timestamp);
	} 
 	
}
