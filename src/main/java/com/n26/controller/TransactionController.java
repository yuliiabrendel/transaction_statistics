package com.n26.controller;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.n26.model.Transaction;
import com.n26.service.TransactionService;

/***
 * @author Yuliia Brendel
 */
@RestController
@ControllerAdvice
public class TransactionController {
	
	@Autowired
	private TransactionService transactionService;
	
	@RequestMapping(value = "/transactions", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<?> addTransaction(@RequestBody Transaction transaction) {
		
		if (transactionService.transactionExpired(transaction))
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		if (transactionService.transactionUnprocessable(transaction))
			return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
		
		transactionService.addTransaction(transaction);
		
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
	
	@RequestMapping(value="/transactions", method=RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<?> deleteAllTransactions() {
		transactionService.deleteAll();
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@ExceptionHandler(HttpMessageNotReadableException.class)
    public final ResponseEntity<?> httpMessageNotReadableException(HttpMessageNotReadableException e, WebRequest request) {
        if (e.getCause() instanceof InvalidFormatException)
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
