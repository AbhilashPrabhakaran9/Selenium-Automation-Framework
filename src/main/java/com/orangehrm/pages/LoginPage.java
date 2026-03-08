package com.orangehrm.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.orangehrm.actiondriver.ActionDriver;
import com.orangehrm.base.BaseClass;

public class LoginPage {
	
	private ActionDriver actionDriver; //actionDriver variable
	
	//Define locators using By class
	
	private By usernameField = By.name("username"); 
	private By passwordField = By.xpath("//*[@id=\"app\"]/div[1]/div/div[1]/div/div[2]/div[2]/form/div[2]/div/div[2]/input");
	////*[@id="app"]/div[1]/div/div[1]/div/div[2]/div[2]/form/div[2]/div/div[2]/input
	//private By passwordField = By.cssSelector("input[type='password']");
	private By loginButton = By.xpath("//button[text()=' Login ']");
	private By errorMessage = By.xpath("//p[text()='Invalid credentials']");
	
	//How to initialise the action driver
	//Initialize the ActionDriver object by passing WebDriver instance
	
	//public LoginPage(WebDriver driver) 
	
	//{
		//this.actionDriver=new ActionDriver(driver);//Here for initializing the ActionDriver's object or reference, 
		//variable, we will create the object of ActionDriver class and pass driver inside that, and we will store in 
		//actiondriver variable. This is how you can initialize the action driver object by constructor of this
		//login page. So in the test class when we call this when we create the object of this login page so automatically
		//the constructor will be called and it will initialize the action driver.
		
		/*
		 * ActionDriver actionDriver = new ActionDriver(driver);

		 * 	ActionDriver → This is a class name.

			actionDriver → This is an object (reference variable) of that class.

			new ActionDriver(driver) → This creates a new object of the ActionDriver class and 
			passes driver to its constructor.
		 */
	//}
	
	public LoginPage(WebDriver driver) {
		this.actionDriver=BaseClass.getActionDriver();
	}
	
	
	
	//Method to perform login
	public void login(String username, String password) {
		actionDriver.enterText(usernameField, username);
		actionDriver.enterText(passwordField, password);
		actionDriver.click(loginButton);
	}
	
	//Method to check if error message is displayed
	public boolean isErrorMessageDisplayed() {
		return actionDriver.isDisplayed(errorMessage);
	}
	
	//Method to get the text from error message
	public String getErrorMessageText() {
		return actionDriver.getText(errorMessage);
	}
	
	//Verify if error is correct or not --- Changed the return type
	public boolean verifyErrorMessage(String expectedError) {
		return actionDriver.compareText(errorMessage, expectedError);
	}
			
	

}
