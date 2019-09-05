package com.kuliza.mmtraverl.model;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class GoogleDistanceMatrixTest {
	
	@Test
	public void innerClassTest() {
		GoogleDistanceMatrix googleDistanceMatrix=new GoogleDistanceMatrix();
		List<GoogleDistanceMatrix.Rows> rrows=new ArrayList<>();
		GoogleDistanceMatrix.Rows rows=googleDistanceMatrix.new Rows();
		GoogleDistanceMatrix.Rows.Elements elements=rows.new Elements();
		GoogleDistanceMatrix.Rows.Elements.InnerInfo distance=elements.new InnerInfo();
		distance.setText("text1");
		distance.setValue(143124);
		
		GoogleDistanceMatrix.Rows.Elements.InnerInfo duration=elements.new InnerInfo();
		duration.setText("duration");
		duration.setValue(4324232);
		
		elements.setDistance(distance);
		elements.setDuration(duration);
		List<GoogleDistanceMatrix.Rows.Elements> list=new ArrayList<>();
		list.add(elements);
		rows.setElements(list);
		rrows.add(rows);
		googleDistanceMatrix.setRows(rrows);
		System.out.println(googleDistanceMatrix);
	}
	
	@Test
	public void parseGooleDuration() {
		String duration="2 days 8 hours";
		int days = 0;
		int hours = 0;
		int totalhours=0;
		if(null!=duration && (duration.contains("days")|| duration.contains("hours"))) {
			String splitedData[]=duration.split(" "); 
			for (int i = 0; i < splitedData.length; i++) {
				if(splitedData[i].equals("days")) {
					days=Integer.parseInt(splitedData[i-1]);
				}
				if(splitedData[i].equals("hours")) {
					hours=Integer.parseInt(splitedData[i-1]);
				}
			}
		}
		System.out.println(days+">>>"+hours);
		
		totalhours=days*24+hours;
		System.out.println(totalhours);
		
	}
	
	@Test
	public void hoursToDays() {
		int hours=56;
		StringBuilder builder=new StringBuilder();
		builder.append(hours/24).append(" days ").append(hours%24).append(" hous");
		System.err.println(builder.toString());
	}

}
