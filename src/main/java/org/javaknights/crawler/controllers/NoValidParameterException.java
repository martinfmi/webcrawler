package org.javaknights.crawler.controllers;

public class NoValidParameterException extends Exception {

	public NoValidParameterException(String paramName) {
		super(paramName);
	}
	
	
}
