package com.kuliza.mmtraverl.rest.controller;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import io.restassured.RestAssured;

@RunWith(SpringJUnit4ClassRunner.class) 
public class DistanceCalculatorTest {
	
	@Before
    public void setBaseUri () {
            RestAssured.baseURI = "http://localhost:8080/managedmethods/api/"; 
    }
	
	@Test
    public void getDataTest() {
		RestAssured.get("caltraveTime/41.43206:-81.38992/40.6655101:-73.89188969999998").then().statusCode(200);
    }
	

}
