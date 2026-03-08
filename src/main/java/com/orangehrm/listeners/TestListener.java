package com.orangehrm.listeners;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.testng.IAnnotationTransformer;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.annotations.ITestAnnotation;

import com.orangehrm.base.BaseClass;
import com.orangehrm.utilities.ExtentManager;
import com.orangehrm.utilities.RetryAnalyzer;

public class TestListener implements ITestListener, IAnnotationTransformer //ITestListener = an interface

//Generally whenever we implements, whatever unimplemented methods are there, automatically it should come over here.

//Retry Logic Option 2nd ---> IAnnotationTransformer = We have to implement this one because this will modify our test 
//at @Test annotation at run time. So it will apply the same logic what we have done individually at the test level (@Test).
//So whatever we have given over here [ApiTest.java - @Test()] that will be applicable when we implement this IAnnotation
//Transformer interface

{
    @Override
	public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
    	annotation.setRetryAnalyzer(RetryAnalyzer.class);
    	
	}

	//Triggered when a test starts
	@Override
	public void onTestStart(ITestResult result) 
	{
		String testName = result.getMethod().getMethodName();
		
		//Start logging in Extent Reports
		ExtentManager.startTest(testName);
		ExtentManager.logStep("Test Started: " +testName);
		
	}

	//Triggered when a test succeeds
	@Override
	public void onTestSuccess(ITestResult result) {
		String testName = result.getMethod().getMethodName();
		
		if(!result.getTestClass().getName().toLowerCase().contains("api")) //getName() for getting test class name
		
		{
			
			ExtentManager.logStepWithScreenshot(BaseClass.getDriver(), "Test passed successfully!", "Test End: " + testName + 
					"- Test passed");
			
		}
		
		else {
			
			ExtentManager.logStepValidationForAPI("Test End: " + testName + "- Test passed");
		}
		
		
		
	}

	//Triggered when a test fails
	@Override
	public void onTestFailure(ITestResult result)// result =  ITestResult's reference variable
	
	{
		//Youtuber says that he uses this method everytime like name of the test. I can also create one private method and you can call 
		//that method. That is also fine.
		String testName = result.getMethod().getMethodName();
		String failureMessage = result.getThrowable().getMessage(); //This will give failure message
		ExtentManager.logStep(failureMessage);
		
		if(!result.getTestClass().getName().toLowerCase().contains("api")) {
			ExtentManager.logFailure(BaseClass.getDriver(), "Test failed!", "Test End: " + testName + "- Test failed");	
			//BaseClass.getDriver() = This is to attach screenshot because we are calling getDriver.
		}
		
		else {
			ExtentManager.logFailureAPI("Test End: " + testName + "- Test failed");	
			
		}
		
		
		
	}

	//Triggered when a Test skips
	@Override
	public void onTestSkipped(ITestResult result) 
	{
		String testName = result.getMethod().getMethodName();
		ExtentManager.logskip("Test skipped " +testName);
	}

	
	//Triggered when a suite starts
	@Override
	public void onStart(ITestContext context) 
	//When we start testng.xml, automatically the reports get initialized using this getReporter() method.
	//onStart method triggers when suite starts, it will call getReporter() method.
	
	{
		//Initialize the extent reporter
		ExtentManager.getReporter();
	}

	//Triggered when the suite ends
	@Override
	public void onFinish(ITestContext context) {
		//Flush the Extent Reports
		//Once it is done, it will automatically end the suite
		ExtentManager.endTest();
	}

	
	
	
}

//Note:- In API Testing point of view, we need to make changes on the TestListener.java class [onTestSuccess, onTestFailure]  
//too because when test gets fail or pass then we call different methods. Inside onTestSuccess and onTestFailure methods,
//we put conditions.
