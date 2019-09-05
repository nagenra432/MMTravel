package com.kuliza.mmtraverl.model;

import java.util.ArrayList;
import java.util.List;

public enum TravelMode {

	DRIVING("driving"),WALKING("walking"),BICYCLING("bicycling"),TRANSIT("transit"),
	BUS("bus"),SUBWAY("subway"),TRAIN("train"),TRAM("tram"),RAIL("rail");
	
	
	public static List<TravelMode> getAllTravelModes() {
		List<TravelMode> travelModes=new ArrayList<>();
		travelModes.add(DRIVING);
		travelModes.add(WALKING);
		travelModes.add(BICYCLING);
		travelModes.add(TRANSIT);
		return travelModes;
	}
	
	public static List<TravelMode> getAllTransitModes() {
		List<TravelMode> travelModes=new ArrayList<>();
		travelModes.add(BUS);
		travelModes.add(SUBWAY);
		travelModes.add(TRAIN);
		travelModes.add(TRAM);
		travelModes.add(RAIL);
		return travelModes;
	}
	
	private String mode;

	private TravelMode(String mode) {
		this.mode = mode;
	}

	public String getMode() {
		return mode;
	}
	
}
