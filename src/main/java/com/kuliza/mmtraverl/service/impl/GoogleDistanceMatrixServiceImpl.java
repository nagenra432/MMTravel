package com.kuliza.mmtraverl.service.impl;

import java.util.ArrayList;
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
import com.google.gson.GsonBuilder;
import com.kuliza.mmtraverl.model.GoogleDistanceMatrix;
import com.kuliza.mmtraverl.model.GoogleStatusCode;
import com.kuliza.mmtraverl.model.TravelInfo;
import com.kuliza.mmtraverl.model.TravelMode;
import com.kuliza.mmtraverl.service.GoogleDistanceMatrixService;

/*
 *  GoogleDistanceMatrixService calling Google DistanceMatrixService.
 *   Finding Distance and Travel Time Information  
 */

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
		
		/*
		 * TODO instead of calling one by one Google Rest call, 
		*  we have to enable in Multi Threaded environment
		*  using Callable interface for Asynchronous Response.   
		*/
		
		List<String> gogleLevelStatus=new ArrayList<>();
		RestTemplate restTemplate=new RestTemplate();
		String googleURL =null;
		boolean is_zero_result=true;
		for (TravelMode travelMode : modes) {
			
			/*
			 * Except Transit TravelMode Google Rest Api calling with 'mode' Parameter
			 * mode Exp : driving,walking,bicycling 
			 * If we are not pass mode Google API will take driving as default mode 
			 */
			try {
			if (!travelMode.equals(TravelMode.TRANSIT)) {
				 googleURL=googleUrlBuilder(lat1,long1,lat2, long2,mode,travelMode.getMode());
				 
				 log.info(googleURL);
				 ResponseEntity<String> result = restTemplate.getForEntity(googleURL, String.class);
				 log.info(result.getBody());
					
				 Gson gson = new GsonBuilder().serializeNulls().create();
				 GoogleDistanceMatrix googleDistanceMatrix = gson.fromJson(result.getBody(), GoogleDistanceMatrix.class);
				 String levelStatusCode=googleDistanceMatrix.getRows().get(0).getElements().get(0).getStatus();
				 gogleLevelStatus.add(levelStatusCode);
				 if(levelStatusCode.equals(GoogleStatusCode.OK.name())){
				 String data=googleDistanceMatrix.getRows().get(0).getElements().get(0).getDuration().getText();
				 if(null!=data) {
					 is_zero_result=false;
					 int hours=daysToHours(data);
					 modeOfDistanceMatrix.put(travelMode.getMode(), hours);
			 	 }
			 }
			}else {
				
				/*
				 * In Transit TravelMode Google Rest Api calling with 'transit_mode' Parameter
				 * transit_mode Exp : bus, train,tram ...etc   
				 */
				
				for (TravelMode transitModes : TravelMode.getAllTransitModes()) {
					googleURL=googleUrlBuilder(lat1,long1,lat2, long2,transitMode,transitModes.getMode());
					log.info(googleURL);
					ResponseEntity<String> result = restTemplate.getForEntity(googleURL, String.class);
					log.info(result.getBody());
					
					Gson gson = new GsonBuilder().serializeNulls().create();
					GoogleDistanceMatrix googleDistanceMatrix = gson.fromJson(result.getBody(), GoogleDistanceMatrix.class);
					 String levelStatusCode=googleDistanceMatrix.getRows().get(0).getElements().get(0).getStatus();
					 gogleLevelStatus.add(levelStatusCode);
					 if(levelStatusCode.equals(GoogleStatusCode.OK.name())){
					   String data=googleDistanceMatrix.getRows().get(0).getElements().get(0).getDuration().getText();
 					 if(null!=data) {
						 is_zero_result=false;
						 int hours=daysToHours(data);
						 modeOfDistanceMatrix.put(travelMode.getMode(), hours);
					 }
				   }
				}
			}
				
			}catch (Exception e) {
				log.error(e.getMessage());
			}
		}
		if(!is_zero_result && gogleLevelStatus.contains(GoogleStatusCode.OK.name())) {
			 travelInfo=extractTravelInfo(modeOfDistanceMatrix);
			 travelInfo.setStatusCode(GoogleStatusCode.OK.name());
		}else {
			travelInfo=new TravelInfo();
			travelInfo.setStatusCode(gogleLevelStatus.get(0));
		}
		return travelInfo;
	}
	
	/*
	 * Building Google url
	 */
	private String googleUrlBuilder(double lat1,double long1, double lat2,  double long2,String mode,String modeValue) {
		StringBuilder urlbuilder=new StringBuilder();
		urlbuilder.append(gooleMatrixURI);
		urlbuilder.append(origins).append(lat1).append(",").append(long1);
		urlbuilder.append("&").append(destinations).append(lat2).append(",").append(long2);
		urlbuilder.append("&").append(key).append(appKey);
		urlbuilder.append("&").append(mode).append(modeValue);
		
		return urlbuilder.toString();
	}
	
	/*
	 * Taking Map key is mode or transit_mode and value is Mints Time
	 * Sorting based on Time
	 * Checking mins and hours and days building Response 
	 * Response key is Mode and value is Min and Max travel time
	 */
	
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
	
	/*
	 * Converting google duration to mins
	 */
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
