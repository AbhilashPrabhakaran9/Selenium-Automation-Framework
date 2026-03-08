package com.orangeHRM.test;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.pages.HomePage;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.utilities.ExtentManager;
import com.orangehrm.utilities.DataProviders;

public class LoginPageTest extends BaseClass //extending for accessing driver

/*Point: We have written the getDriver() method to get the getter method, so copy this method from BaseClass
and we will put inside new LoginPage(); and new HomePage(); and we need to pass the driver to this LoginPage()
in LoginPage class. Similar steps for HomePage.
*/

	{
	//2 instances are getting due to below 2 lines
	private LoginPage loginPage; //For Initialisation
	private HomePage homePage;//For Initialisation
	
	@BeforeMethod //Task for me- Check difference between different annotations
	public void setupPages() 
	{
		loginPage=new LoginPage(getDriver()); //For using the reference variable, Initialising and Creating objects 
		//of LoginPage and we have to pass driver over here.
		homePage=new HomePage(getDriver());  //For using the reference variable, Initialising and Creating objects 
		//of HomePage
	}
	
	
	@Test(dataProvider="validLoginData", dataProviderClass = DataProviders.class)
	public void verifyValidLoginTest(String username, String password) {
		
		//ExtentManager.startTest("Valid Login Test"); -- This has been implemented in TestListener class	
		System.out.println("Running testMethod1 on thread: " + Thread.currentThread().getId());
		ExtentManager.logStep("Navigating to login page entering username and password");
		loginPage.login(username, password);
		ExtentManager.logStep("Verifying Admin tab is visible or not");
		Assert.assertTrue(homePage.isAdminTabVisible(), "Admin tab should be visible after successful login");
		ExtentManager.logStep("Validation successful");
		homePage.logout();
		ExtentManager.logStep("Loggedout successfully");
		staticWait(2); //I can use/call this method here because we extended the BaseClass.
	}
	
	@Test(dataProvider="inValidLoginData", dataProviderClass = DataProviders.class)
	public void invalidLoginTest(String username, String password) {
		
		//ExtentManager.startTest("In-Valid Login Test"); -- This has been implemented in TestListener class	
		System.out.println("Running testMethod2 on thread: " + Thread.currentThread().getId());
		ExtentManager.logStep("Navigating to login page entering username and password");
		loginPage.login(username, password);
		String expectedErrorMessage="Invalid credentials"; //i am failing this step by putting 1 to this, i can change back later
		Assert.assertTrue(loginPage.verifyErrorMessage(expectedErrorMessage),"Test failed: Invalid error message");
		ExtentManager.logStep("Validation successful");
		ExtentManager.logStep("Loggedout successfully");
		
		
		
		
	}
	
	

}
