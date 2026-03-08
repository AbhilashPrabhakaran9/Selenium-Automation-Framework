package com.orangeHRM.test;

import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.orangehrm.base.BaseClass;
import com.orangehrm.pages.HomePage;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.utilities.DBConnection;
import com.orangehrm.utilities.DataProviders;
import com.orangehrm.utilities.ExtentManager;

public class DBVerificationTest extends BaseClass

{
	//Pasted these 6 below codes from LoginPageTest.java. Here we have created 2 reference variable (loginPage, homePage) of 
	//our LoginPage and HomePage classes.Because we are going to call login method and home page related methods.
	private LoginPage loginPage;
	private HomePage homePage;
	
	@BeforeMethod
	public void setupPages() //Inside setupPages() method we have created the objects of those Login and Home pages. This is for
	//initialization of those pages.
	{
		loginPage = new LoginPage(getDriver());
		homePage = new HomePage(getDriver());
	}
	
	//Writing the test cases now here.
	@Test(dataProvider="emplVerification", dataProviderClass = DataProviders.class)
	public void verifyEmployeeNameVerificationFromDB(String emplID, String empName)
	{
		//
		SoftAssert softAssert = getSoftAssert(); //Giving/putting getSoftAssert() method here instead of creating an object (new 
		//SoftAssert();) and later we will use this object (softAssert) and put in the place of Assert in this script.
		
		//Creating the object of Soft Assert class.
		//This is not thread safe because when we create test cases, everytime we have to create the object. So we can create 
		//the thread local object of soft assert in our base class and we can utilize that.
		
		//We are using ExtentManager class here for extent reports, ExtentManager.logStep() different methods/steps to log into 
		//our reports. So that's the reason i am using ExtentManager class dot log step with different steps.
		ExtentManager.logStep("Logging with Admin Credentials");
		loginPage.login(prop.getProperty("username"),prop.getProperty("password"));//loginPage = object , login() = method
		//Passing here username and password from config.properties [Note: We can also parameterize using Data provider
		
		ExtentManager.logStep("Click on PIM tab");
		homePage.clickOnPIMTab();
		
		ExtentManager.logStep("Search for Employee");
		homePage.employeeSearch(empName);
		
		ExtentManager.logStep("Get the employee name from DB");//Here we will get the data from database and later on we will
		//compare. 
		String employee_id=emplID;//Parameterized using data provider
		
		//Fetch the data into a map
		Map<String,String> employeeDetails = DBConnection.getEmployeeDetails(employee_id); //Calling getEmployeeDetails() with 
		//DBConnection class because getEmployeeDetails() method is static
		//Map = interface , employeeDetails = Map interface's reference variable
		
		//Get the data from employeeDetails reference variable
		String emplFirstName = employeeDetails.get("firstName");//firstName = key from DBConnection class
		//emplFirstName = String variable
		String emplMiddleName = employeeDetails.get("middleName");//middleName = key from DBConnection class
		String emplLastName = employeeDetails.get("lastName");//lastName = key from DBConnection class
		
		//Verifying first and last name
		String emplFirstAndMiddleName = (emplFirstName+" "+emplMiddleName).trim();//Concatinating here
		//Using trim here because if there is no middle name [null], then the space " " will remove using trim().
		//Here i delibrately failed the validation for last name by putting the word Test between first name and
		//last name. [For checking the Hard assertion scenario]. Like this type of situation, for executing the second 
		//validation of last name even if the first validation of first and middle name fails, i should use SOFT ASSERTION
		//in our script to handle this.
		
		//Validation for first and middle name
		ExtentManager.logStep("Verify the employee first and middle name");
		softAssert.assertTrue(homePage.verifyEmployeeFirstAndMiddleName(emplFirstAndMiddleName),
				"First and Middle name are not matching"); //emplFirstAndMiddleName = Getting from database
		//Here Assert class means HARD ASSERTION
		//I changed Assert [Hard Assertion] to softAssert [Soft Assertion]
		
		//Validation for last name
		ExtentManager.logStep("Verify the employee last name");
		softAssert.assertTrue(homePage.verifyEmployeeLastName(emplLastName));//emplLastName = Getting from database
		//Here Assert class means HARD ASSERTION
		//I changed Assert to softAssert
		
		ExtentManager.logStep("DB Validation completed");
		
		softAssert.assertAll(); //This we have to execute, so it will collect all the success, failures and give the reports
		
		}
	
	
	

}
