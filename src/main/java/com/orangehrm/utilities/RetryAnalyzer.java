package com.orangehrm.utilities;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer //IRetryAnalyzer = interface
	
{
	//VARIABLES
	private int retryCount = 0; //Number of retries
	private static final int maxRetryCount =2; //Maximum number of retries --- CONSTANT VALUE
	
	@Override
	public boolean retry(ITestResult result) {
		if(retryCount<maxRetryCount) {
			retryCount++;
			return true; //Retry the test
		}
		return false;
	}

}

//Case:-
//If it is passed in the second run [Means first time], it won't execute again. This will execute again when we have failure. 



//Note:
//Static varaiable -- In Java, when a variable is declared static, it means the variable belongs to the 
//class itself, not to individual objects of the class.

//Static variable -- Belongs to class -- One shared copy
//Instance variable -- Belongs to Object -- One per object
