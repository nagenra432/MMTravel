package com.kuliza.mmtraverl.service.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.kuliza.mmtraverl.model.GoogleDistanceMatrix;
import com.kuliza.mmtraverl.model.TravelInfo;
import com.kuliza.mmtraverl.model.TravelMode;
import com.kuliza.mmtraverl.service.GoogleDistanceMatrixService;

@Service
public class GoogleDistanceMatrixServiceImpl implements GoogleDistanceMatrixService {
	
	private  static final Logger log = LoggerFactory.getLogger(GoogleDistanceMatrixServiceImpl.class);
	
	@Value("${google.app.key}")
	private String appKey;
	
	@Value("${google.distance.matrix.uri}")
	private String gooleMatrixURI;
	
	@Value("${google.distance.matrix.origins}")
	private String origins;
	
	@Value("${google.distance.matrix.destinations}")
	private String destinations;
	
	@Value("${google.distance.matrix.mode}")
	private String mode;
	
	@Value("${google.distance.matrix.transit.mode}")
	private String transitMode;
	
	@Value("${google.distance.matrix.key}")
	private String key;
	

	@Override
	public TravelInfo callGooleDistanceAPI(double lat1,double long1,double lat2, double long2) {
		
		List<TravelMode> modes=TravelMode.getAllTravelModes();
		Map<String, Integer> modeOfDistanceMatrix=new HashMap<>();
		TravelInfo travelInfo=null;
		
		//List<String> googleurls=new ArrayList<>();
		RestTemplate restTemplate=new RestTemplate();
		String googleURL =null;
		for (TravelMode travelMode : modes) {
			if (!travelMode.equals(TravelMode.TRANSIT)) {
				 googleURL=googleUrlBuilder(lat1,long1,lat2, long2,mode,travelMode.getMode());
				 
				 log.info(googleURL);
				 ResponseEntity<String> result = restTemplate.getForEntity(googleURL, String.class);
				 log.info(result.getBody());
					
				 Gson gson = new Gson();
				 GoogleDistanceMatrix googleDistanceMatrix = gson.fromJson(result.getBody(), GoogleDistanceMatrix.class);
				 int hours=daysToHours(googleDistanceMatrix.getRows().get(0).getElements().get(0).getDuration().getText());
				 modeOfDistanceMatrix.put(travelMode.getMode(), hours);
				
			}else {
				
				for (TravelMode transitModes : TravelMode.getAllTransitModes()) {
					googleURL=googleUrlBuilder(lat1,long1,lat2, long2,transitMode,transitModes.getMode());
					log.info(googleURL);
					ResponseEntity<String> result = restTemplate.getForEntity(googleURL, String.class);
					log.info(result.getBody());
					
					Gson gson = new Gson();
					GoogleDistanceMatrix googleDistanceMatrix = gson.fromJson(result.getBody(), GoogleDistanceMatrix.class);
					int hours=daysToHours(googleDistanceMatrix.getRows().get(0).getElements().get(0).getDuration().getText());
					modeOfDistanceMatrix.put(travelMode.getMode(), hours);
				}
				
			}
		}
		
		travelInfo=extractTravelInfo(modeOfDistanceMatrix);
		return travelInfo;
	}
	
	private String googleUrlBuilder(double lat1,double long1, double lat2,  double long2,String mode,String modeValue) {
		StringBuilder urlbuilder=new StringBuilder();
		urlbuilder.append(gooleMatrixURI);
		urlbuilder.append(origins).append(lat1).append(",").append(long1);
		urlbuilder.append("&").append(destinations).append(lat2).append(",").append(long2);
		urlbuilder.append("&").append(key).append(appKey);
		urlbuilder.append("&").append(mode).append(modeValue);
		
		return urlbuilder.toString();
	}
	
	private TravelInfo extractTravelInfo(Map<String, Integer> modeOfDistanceMatrix) {
		TravelInfo travelInfo=new TravelInfo();
		 List<Map.Entry<String, Integer> > list = new LinkedList<Map.Entry<String, Integer> >(modeOfDistanceMatrix.entrySet()); 
	  
	      Collections.sort(list, new Comparator<Map.Entry<String, Integer> >() { 
	            public int compare(Map.Entry<String, Integer> map1,  
	                               Map.Entry<String, Integer> map2) 
	            { 
	                return (map1.getValue()).compareTo(map2.getValue()); 
	            } 
	        }); 
	      
	       
	      Map<String, String> trlInfo=new HashMap<>();
	      StringBuilder builder=new StringBuilder();
	      int time=modeOfDistanceMatrix.get(list.get(0).getKey());
	      if(time/24/60 > 0)
	    	  builder.append(time/24/60).append(" days ");
	      if(time/60%24 > 0)
	    	  builder.append(time/60%24).append(" hous ");
		  if(time%60>0)
			  builder.append(time%60).append(" mins ");
		  
		  trlInfo.put(list.get(0).getKey(), builder.toString());
		  travelInfo.setMinTime(trlInfo);
		  
		  StringBuilder builder1=new StringBuilder();
		  Map<String, String> trlInfo1=new HashMap<>();
		  
		  int maxTime=modeOfDistanceMatrix.get(list.get(list.size()-1).getKey());
	      if(maxTime/24/60 > 0)
	    	  builder1.append(maxTime/24/60).append(" days ");
	      if(maxTime/60%24 > 0)
	    	  builder1.append(maxTime/60%24).append(" hous ");
		  if(maxTime%60>0)
			  builder1.append(maxTime%60).append(" mins ");
		  
		  trlInfo1.put(list.get(list.size()-1).getKey(), builder1.toString());
		  travelInfo.setMaxTime(trlInfo1);
	     
		return travelInfo;
	}
	
	public int daysToHours(String duration) {
		int days = 0;
		int hours = 0;
		int mints=0;
		
		if(null!=duration && (duration.contains("days")|| duration.contains("hours")||duration.contains("mins"))) {
			String splitedData[]=duration.split(" "); 
			for (int i = 0; i < splitedData.length; i++) {
				if(splitedData[i].equals("days")) {
					days=Integer.parseInt(splitedData[i-1]);
				}
				if(splitedData[i].equals("hours")) {
					hours=Integer.parseInt(splitedData[i-1]);
				}
				if(splitedData[i].equals("mins")) {
					mints=Integer.parseInt(splitedData[i-1]);
				}
			}
		}
		return (days*24+hours)*60+mints;
		
	}

}