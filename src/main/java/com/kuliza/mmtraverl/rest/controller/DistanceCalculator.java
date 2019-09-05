package com.kuliza.mmtraverl.rest.controller;


import javax.validation.Valid;
import javax.validation.constraints.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kuliza.mmtraverl.model.GeoLocation;
import com.kuliza.mmtraverl.model.TravelInfo;
import com.kuliza.mmtraverl.service.DistanceCalculateService;
import com.kuliza.mmtraverl.util.GeoLocationAdapter;

@RestController
@RequestMapping(value = "/api")
//@Validated
public class DistanceCalculator {
	
	private static final String LAT_LONG_REGEXP = "^([-+]?\\d{1,2}([.]\\d+)?),\\s*([-+]?\\d{1,3}([.]\\d+)?)$";
	
	
	@Autowired
	private DistanceCalculateService distanceCalculateService; 

	/*
	 * This service will give Max and Min Travel time based on travel Mode
	 * API Service call format below
	 * exp: http://localhost:8080/managedmethods/api/caltraveTime/41.43206:-81.38992/40.6655101:-73.89188969999998
	 * Response : 
	 * {
		  "minTime": {
		    "driving": "7 hous 29 mins "
		  },
		  "maxTime": {
		    "walking": "6 days 6 hous "
		  }
		}
	 */
	@GetMapping(value = "/caltraveTime/{latitude1}:{longitude1}/{latitude2}:{longitude2}")
	public ResponseEntity<TravelInfo>  calTraveTime(@Valid @Pattern(regexp = LAT_LONG_REGEXP,message="Latitude1 not allow Characters") @PathVariable("latitude1") String latitude1,
			@Valid @Pattern(regexp = LAT_LONG_REGEXP,message="Latitude2 not allow Characters") @PathVariable("latitude2") String latitude2,
			@Valid @Pattern(regexp = LAT_LONG_REGEXP,message="Longitude1 not allow Characters") @PathVariable("longitude1") String longitude1,
			@Valid @Pattern(regexp = LAT_LONG_REGEXP,message="Longitude2 not allow Characters") @PathVariable("longitude2") String longitude2) 
	   {
		
		TravelInfo travelInfo=null;
		 try {
			 GeoLocation source=GeoLocationAdapter.convertLatLag(latitude1, longitude1);
			 GeoLocation destination=GeoLocationAdapter.convertLatLag(latitude2, longitude2);		
			 travelInfo=distanceCalculateService.calculateTraveTime(source, destination);
	        }catch (Exception e) {
	        	 return new ResponseEntity<>(null, null, HttpStatus.INTERNAL_SERVER_ERROR);
			}
	        return new ResponseEntity<>(travelInfo, HttpStatus.OK);
	}

}
