package com.kuliza.mmtraverl.service;

import com.kuliza.mmtraverl.model.GeoLocation;
import com.kuliza.mmtraverl.model.TravelInfo;

public interface DistanceCalculateService {
	
	TravelInfo calculateTraveTime(GeoLocation source,GeoLocation destination);

}
