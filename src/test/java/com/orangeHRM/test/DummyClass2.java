package com.orangeHRM.test;

import org.testng.annotations.Test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.utilities.ExtentManager;

public class DummyClass2 extends BaseClass {
	
	@Test
	public void dummyTest2() {
		//Test checking
		//ExtentManager.startTest("DummyTest2 Test"); -- This has been implemented in TestListener class	
		String title=getDriver().getTitle();
		ExtentManager.logStep("Verifying the title");
		assert title.equals("OrangeHRM"):"Test failed - Title is not matching";
		
		System.out.println("Test passed - Title is matching");
		ExtentManager.logStep("Validation successful");
		}

}
