package com.orangehrm.utilities;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class APIUtility {
	
	//Here we will create different methods like how we going to send the request like get, post and how we get the response.
	//So in this utility method, we will write all these API related methods.
	
	//Method to send the GET request
	public static Response sendGetRequest(String endPoint) //static = i can call directly , endPoint = URL
	
	{
		return RestAssured.get(endPoint); //RestAssured = CLASS
	}
	
	/*
	 * Response is a class in the Rest Assured API testing library that represents the HTTP response returned by the server 
	 * after making a request.

It contains everything the server sends back, such as:

Status code (200, 404, 500, etc.)

Response body (JSON, XML, etc.)

Headers

Cookies

Response time

-- REST API is REpresentational State Transfer Application Programming Interface
-- RestAssured = a main class in Rest Assured API testing library [a Java library used for testing REST APIs]
	 */
	
	//Method to send the POST Request
	public static Response sendPostRequest (String endPoint, String payLoad)
	//When we send data to the server, we have to send using the URL(endpoint) and the request body (payLoad)
	
	{
		
		return RestAssured.given().header("Content-Type", "application/json").body(payLoad).post();
		//header("Content-Type", "application/json") = Content type should be application in json format.
		//As you can see inside request screen in postman, we have different headers when we send the request. These headers
		// are in Key value pairs, we have to define like this. [header("Content-Type"- KEY, "application/json"- VALUE)]
		
		//Methods used here are given, header, body, post
	}
	
	//Method to validate the response status
	public static boolean validateStatusCode(Response response, int statusCode) //Response response = Response class and 
	//its reference variable [response]
	//statusCode = expected status code
	
	{
		return response.getStatusCode() == statusCode; //Comparison of the getting & expected response status codes and will give 
		//boolean value as return
	}
	
	//Method to extract value from JSON response
	public static String getJsonValue(Response response, String value) //value = json field value like username, email etc
	
	{
		return response.jsonPath().getString(value);
	}
	
	
}
