package com.n26.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.n26.model.Statistics;
import com.n26.service.StatisticsService;

/***
 * @author Yuliia Brendel
 */
@RestController
public class StatisticsController {
	
	@Autowired
	private StatisticsService statisticsService;
	
	@RequestMapping(value = "/statistics", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<Statistics> computeStatistics() {
		Statistics statistics = statisticsService.computeStatistics();
		return new ResponseEntity<Statistics>(statistics, HttpStatus.OK);
	}
}
