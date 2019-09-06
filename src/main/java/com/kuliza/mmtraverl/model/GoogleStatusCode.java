package com.kuliza.mmtraverl.model;

public enum GoogleStatusCode {
	NOT_FOUND("NOT_FOUND"),ZERO_RESULTS("ZERO_RESULTS"),MAX_ROUTE_LENGTH_EXCEEDED("MAX_ROUTE_LENGTH_EXCEEDED"),OK("OK");
	
	private String levelStatusCode;

	private GoogleStatusCode(String levelStatusCode) {
		this.levelStatusCode = levelStatusCode;
	}

	public String getLevelStatusCode() {
		return levelStatusCode;
	}
	
	
	
}
