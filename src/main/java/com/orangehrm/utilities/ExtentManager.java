package com.orangehrm.utilities;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentManager //Contains settings, configuration related to Extent manager

{
	//Different variables are mentioning below.(extent,test,driverMap)
	
	private static ExtentReports extent; //variable of ExtentReports class , ExtentReports class for 
	//generating and managing the reports
	
	private static ThreadLocal<ExtentTest> test = new ThreadLocal<>(); //Thread local variable of ExtentTest class, 
	//Thread safe for parallel testing , Basically it defines the test which we are going to write on our
	//extent reports.We are creating an object of ThreadLocal.
	
	
	private static Map<Long,WebDriver> driverMap = new HashMap<>();
/*We will create one variable that stores in key value pair. So the first value like key will store thatis thread id.
This is for parallel testing as well. so we will make sure our code is thread safe. so we will store the thread id and
we will store the corresponding webdriver instance.We will create the object of hashmap class in collection framework 
in java to store the key value pair of that variable.

We will create the object of hashmap class (= new HashMap<>();) and will store in map interface, give name
as driverMap. The first value like key will be your thread.so we will take long type of data type and will 
use webdriver as value. So corresponding thread id will store webdriver instance.*/
	
	//Initialize Extent Report
	public synchronized static ExtentReports getReporter() //getReporter() method (which is a reference of ExtentReports 
	//class) of ExtentReports class
	
	{
		if(extent==null) {
			String reportPath=System.getProperty("user.dir")+"/src/test/resources/ExtentReport/ExtentReport.html";//Where 
			//we are going to generating extent report.
			
			//Task for me- Check the difference between forward and backward slashes in coding
			
			//Now we have to create object of ExtentSparkReporter, Earlier we used ExtentHTMLReporter.
			ExtentSparkReporter spark =new ExtentSparkReporter(reportPath); //This ExtentSparkReporter create 
			//or generate the contents in ExtentReports.
			
			//spark = ExtentSparkReporter's variable
			//Using spark variable,we are going to write configurations like the contents in this extent report.
			spark.config().setReportName("Automation Test Report");
			spark.config().setDocumentTitle("OrangeHRM Report");
			spark.config().setTheme(Theme.STANDARD);
			
			//Now creating the object of ExtentReports
			extent=new ExtentReports(); //05:09:04
			extent.attachReporter(spark);
			//Adding system information
			extent.setSystemInfo("Operating System", System.getProperty("os.name")); //Gives the operating system where 
			//we generate report  
			extent.setSystemInfo("Java Version", System.getProperty("java.version")); //Keeping java version inside 
			//our report
			extent.setSystemInfo("User Name", System.getProperty("user.name")); //Keeping user information
			//This is how you can initialize our ExtentReports getReporter method.   
			
		}
		//Now we can add return statement
		return extent;
		
		//This is how you can write getReporter() method to initialize extent report. So initially will generate the 
		//report and will set the report name document title and set theme. And later on we will add some system 
		//information using this extent variable.
	}
	
	//Start the test
	//Here ExtentTest = class , startTest() = method of ExtentTest class , 
	//extentTest = variable of ExtentTest class
	public synchronized static ExtentTest startTest(String testName) 
	{
		ExtentTest extentTest = getReporter().createTest(testName); //Start test
		test.set(extentTest);//Set the value to start the test
		return extentTest;
	}
	
	//End a test
	public synchronized static void endTest() {
		getReporter().flush();
	}
	
	//Get current Thread's test like how we are going to log the info, pass, fail all these values using test name.
	public synchronized static ExtentTest getTest() {
		return test.get();
	}
	
	//Method to get the name of the current test, we need this method while generating screenshots
	public static String getTestName() {
		ExtentTest currentTest = getTest();
		
		if(currentTest!=null) {
			return currentTest.getModel().getName();
		}
		
		else {
			return "No test is currently active for this thread";
		}
	}
	
	//Log a step with informational message
	public static void logStep(String logMessage) {
		//getTest().info("logMessage"); -- This is wrong
		 getTest().log(Status.INFO, logMessage); //I changed - This is correct
	}
	
	//Log a step validation with screenshot
	public static void logStepWithScreenshot(WebDriver driver, String logMessage, String screenShotMessage ) //3 parameters
	//Here we are passing driver as parameter because we are going to take screenshot over here.
	{
		getTest().pass(logMessage);
		
		//Screenshot method - We need to call the screenshot method over here, for that we need to pass 
		//driver and screenShotMessage
		attachScreenshot(driver, screenShotMessage);
	}
	
	//Log a step validation for API - For pass scenario
		public static void logStepValidationForAPI(String logMessage) 
		{
			getTest().pass(logMessage);
		}
	
	//Log a failure
	public static void logFailure(WebDriver driver, String logMessage, String screenShotMessage ) 
	{
		String colorMessage = String.format("<span style='color:red;'>%s/</span>", logMessage);
		getTest().fail(colorMessage);
		
		//Screenshot method
		attachScreenshot(driver, screenShotMessage);
	}
	
	//Log a failure for API
	//I can also use the same method name as above here because the parameters are different for both methods [METHOD OVERLOADING]
	//But i am using different method name here, which is fine.
		public static void logFailureAPI(String logMessage) 
		{
			String colorMessage = String.format("<span style='color:red;'>%s/</span>", logMessage);
			getTest().fail(colorMessage);
			
		}
	
	//log a skip
	public static void logskip(String logMessage)
	{
		String colorMessage = String.format("<span style='color:orange;'>%s/</span>", logMessage);
		getTest().skip(colorMessage);
	}
	
	//Take a screenshot with date and time in the file
	public synchronized static String takeScreenshot(WebDriver driver, String screenshotName) //screenshotName = SCREENSHOT FILE NAME
	{
		TakesScreenshot ts = (TakesScreenshot)driver; //TakesScreenshot=interface, ts = variable, Assigning to driver
		//(TakesScreenshot)driver = type casting
		File src = ts.getScreenshotAs(OutputType.FILE);//File src = File class with its variable src
		
		//Format date and time for file name - Converting the date to string
		String timeStamp = new SimpleDateFormat("yyyy-mm-dd_hh-mm-ss").format(new Date());
		
		//String timestamp = String class's variable timestamp
		//new SimpleDateFormat("yyyy-mm-dd_hh-mm-ss") = Object of SimpleDateFormat
		//format(new Date()) = format method passing with object of Date class [create the object of Date class]
		
		//Saving screenshot to a file
		//Here we will give the string destination path, so firstly when we take the screenshot that will be stored
		//in a memory and later on we have to store in a particular path.
		String destPath = System.getProperty("user.dir")+"/src/test/resources/screenshots/" + screenshotName+"_"+ timeStamp + ".png";
		
		//Now we have to copy to new destination
		File finalPath = new File(destPath);
		
		try {
			FileUtils.copyFile(src, finalPath); //It copies the file from src to finalPath.
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		//When we take screenshot that will convert to a string to append into extent report. 
		
		//Convert screenshot to Base64 for embedding in the report
		String base64Format = convertToBase64(src);  //base64Format = a variable , putting convertToBase64() method [utility method] 
		//here and passing src as parameter into convert to string takeScreenshot() method.
		return base64Format;
		}
	
	//Convert screenshot to Base64 format
	public static String convertToBase64(File screenShotFile) //screenShotFile = Path of file
	{
		String base64Format="";
		//Read the file content into a byte array, here we need to create one array that will read the file format or String file 
		//or file path into string of array.
		
		try {
			byte[] fileContent = FileUtils.readFileToByteArray(screenShotFile);
			base64Format = Base64.getEncoder().encodeToString(fileContent);
		} 
		
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		//We have to create one string variable here fileContent,
		//we are creating an array. Here parameter screenShotFile file path will be converted into this file content array.
		
		return base64Format; 
		
	}
	
	//As of now, we have created takeScreenshot() method, Now we have to attach that into report.
	
	//Attach screenshot to extent report using Base64
	public synchronized static void attachScreenshot(WebDriver driver, String message) 
	{
		try {
			String screenShotBase64 = takeScreenshot(driver,getTestName() ); //String screenShotBase64 = a String variable
			//so we will get screenshot in string format.
			
			//Now we have to attach that into report.
			getTest().info(message, com.aventstack.extentreports.MediaEntityBuilder.createScreenCaptureFromBase64String(screenShotBase64).build());
			//This is how we need to do to create the screenshot to attach the screenshot into extent report
		} 
		
		catch (Exception e) {
			getTest().fail("Failed to attach screenshot:"+ message);
			e.printStackTrace();
		}
		
	}
	
	//Register WebDriver for current Thread
	public static void registerDriver(WebDriver driver)
	{
		driverMap.put(Thread.currentThread().getId(), driver);//put() method for storing values
		//Thread.currentThread().getId() = getting current thread id
	}
	
	
}

//Note: I am declaring some methods as synchronized to make it more thread safe. So single thread can use like at a time 
//one thread can use method.

//Note for API Testing pint of view- Here we don't want to launch the browser, also don't want to take screenshot for API
//testing because this is back end testing. 

