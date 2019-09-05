package com.kuliza.mmtraverl.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kuliza.mmtraverl.model.GeoLocation;
import com.kuliza.mmtraverl.model.TravelInfo;
import com.kuliza.mmtraverl.service.DistanceCalculateService;
import com.kuliza.mmtraverl.service.GoogleDistanceMatrixService;

@Service
public class DistanceCalculateserviceImpl implements DistanceCalculateService {
	
	@Autowired
	private GoogleDistanceMatrixService  googleDistanceMatrixService;

	@Override
	public TravelInfo calculateTraveTime(GeoLocation source, GeoLocation destination) {
		return googleDistanceMatrixService.callGooleDistanceAPI(source.getLatitude(), source.getLongitude(), destination.getLatitude(), destination.getLongitude());
	}

}
