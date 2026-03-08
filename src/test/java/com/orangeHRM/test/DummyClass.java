package com.orangeHRM.test;

import org.testng.SkipException;
import org.testng.annotations.Test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.utilities.ExtentManager;

public class DummyClass extends BaseClass {
	
	@Test
	public void dummyTest() {
		//ExtentManager.startTest("DummyTest1 Test"); -- This has been implemented in TestListener class	
		String title=getDriver().getTitle();
		ExtentManager.logStep("Verifying the title");
		assert title.equals("OrangeHRM"):"Test failed - Title is not matching";
		
		System.out.println("Test passed - Title is matching");
		ExtentManager.logStep("Validation successful");
		
		/*ExtentManager.logskip("This case is skipped");
		throw new SkipException("Skipping the test as part of testing");*/
		
		}

}
