package com.kuliza.mmtraverl.util;

import com.kuliza.mmtraverl.model.GeoLocation;

public class GeoLocationAdapter {

	public static GeoLocation convertLatLag(String latitude, String longitude) {

		GeoLocation geoLocation = null;
		if (null != latitude && null != longitude) {
			geoLocation = new GeoLocation();
			geoLocation.setLatitude(Double.parseDouble(latitude));
			geoLocation.setLongitude(Double.parseDouble(longitude));
		}
		return geoLocation;
	}

}
