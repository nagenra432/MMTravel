package com.kuliza.mmtraverl.model;

import java.io.Serializable;
import java.util.Map;

import lombok.Data;

@Data
public class TravelInfo implements Serializable {

	private static final long serialVersionUID = -6655336855909633061L;

	private Map<String, String> minTime;
	private Map<String, String> maxTime;
	private String statusCode="OK";

}
