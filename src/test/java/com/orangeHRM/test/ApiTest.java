package com.orangeHRM.test;

import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.orangehrm.utilities.APIUtility;
import com.orangehrm.utilities.ExtentManager;
import com.orangehrm.utilities.RetryAnalyzer;

import io.restassured.response.Response;

public class ApiTest //I don't want to extend the baseclass because i am not initializing webdriver [This ApiTest run in backend]
{
	
	//@Test(retryAnalyzer = RetryAnalyzer.class) //retryAnalyzer = an attribute --- RETRY LOGIC 1st OPTION
	
	@Test
	//This test case will be applicable for retry logic
	
	public void verifyGetUserAPI() 
	{
		SoftAssert softAssert = new SoftAssert();
		
		//Step1: Define API Endpoint
		String endPoint = "https://jsonplaceholder.typicode.com/users/1"; //Since we do not have api for orangehrm, that's why we
		//are using this end point
		ExtentManager.logStep("API Endpoint: " + endPoint);
		
		//Step2: Send GET Request
		ExtentManager.logStep("Sending GET Request to the API");
		Response response = APIUtility.sendGetRequest(endPoint); //Storing in response variable
		
		//Step3: validate status code
		ExtentManager.logStep("Validating API Response status code");
		boolean isStatusCodeValid = APIUtility.validateStatusCode(response, 200); //I entered wrong response code if the 
		//code=201, the right one is 200
		
		softAssert.assertTrue(isStatusCodeValid, "Status code is not as expected"); //If it is not true, then i can put 'Status 
		//code is not as expected"
		
		//We have to call the method from Extent Manager whether it is pass or fail
		if(isStatusCodeValid) {
			ExtentManager.logStepValidationForAPI("Status Code Validation passed");
		}
		else {
			ExtentManager.logFailureAPI("Status code validation failed");
		}
		
		//Step4: validate user name
		ExtentManager.logStep("Validating response body for username"); 
		
		//Getting username
		String userName = APIUtility.getJsonValue(response, "username");
		boolean isUserNameValid = "Bret".equals(userName);
		softAssert.assertTrue(isUserNameValid,"Username is not valid"); //If it is not true, then i can put "Username is not 
		//valid"
		if(isUserNameValid) {
			ExtentManager.logStepValidationForAPI("Username validation passed");
		}
		else {
			ExtentManager.logFailureAPI("Username validation failed");
		}
		
		//Step5: Validate email
		ExtentManager.logStep("Validating response body for email");
		
		
		String userEmail = APIUtility.getJsonValue(response, "email");
		
		boolean isEmailValid = "Sincere@april.biz".equals(userEmail);
		
		softAssert.assertTrue(isEmailValid, "Email is not valid");
		
		if(isEmailValid) {
			ExtentManager.logStepValidationForAPI("Email validation passed");
		}
		else {
			ExtentManager.logFailureAPI("Email validation failed");
		}
		
		softAssert.assertAll();
		
		
	}

}

//Note: I can try for orther fields which present in the postman's request screen like id, name, username, email etc.

//Note:- Why we have created the object of Soft assertion over here because last time we have created while testing the
//DB verification, we have created the getter method getSoftAssert() inside BaseClass.java. Since the UI testing and API
//testing/test cases are different. That is the reason we have not extends the base class. We have written this ApiTest class
//separately. so thats the reason we have created separate object over here.
