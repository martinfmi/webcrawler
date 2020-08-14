package org.javaknights.crawler.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlers {

	@ExceptionHandler
	public ResponseEntity<String> handleNoParameterException(NoValidParameterException exception) {
		return ResponseEntity.status(401).body("No parameter found: " + exception.getMessage());
	}
}
