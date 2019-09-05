package com.kuliza.mmtraverl.service;

import com.kuliza.mmtraverl.model.TravelInfo;

public interface GoogleDistanceMatrixService {

	 TravelInfo callGooleDistanceAPI(double lat1,double long1,double lat2, double long2);
	
}
