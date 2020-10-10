package com.n26.service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.n26.model.Statistics;
import com.n26.service.TransactionService;

/***
 * @author Yuliia Brendel
 */
@Service
public class StatisticsServiceImpl implements StatisticsService {

	@Autowired
	private TransactionService transactionService;
	
	@Override
	public Statistics computeStatistics() {
	
		final List<BigDecimal> amounts = transactionService.getTransactionAmounts();
		if (amounts.isEmpty())
			return new Statistics();
		
		long count = amounts.size();
		BigDecimal sum = amounts.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
		BigDecimal avg = sum.divide(new BigDecimal(count), 2, BigDecimal.ROUND_HALF_UP);
		BigDecimal max = amounts.stream().max(Comparator.naturalOrder()).get();
		BigDecimal min = amounts.stream().min(Comparator.naturalOrder()).get();
		
		return new Statistics(format(sum), avg.toString(), format(max), format(min), count);
	}
	
	private String format(BigDecimal bigDecimal) {
		return bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
	}
}
