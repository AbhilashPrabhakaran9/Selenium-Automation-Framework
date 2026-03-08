package com.orangehrm.utilities;




import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;



public class LoggerManager //Creating the logs in script
{
	
	//This method returns a Logger instance for the provided class
	//Logger is an interface.It returns logger interface so that we can log for particular class 
	
	public static Logger getLogger(Class<?> clazz) //Logger instance
	
	//Here i am providing the class for which we want the log, i am entering Class<?> clazz 
	//Class<?> clazz means Class generics (instance of any type of class) Here ? means unknown, so that it takes
	//any type of instance of the class. We can give the variable name like clazz.
	
	
	{
		return LogManager.getLogger();//LogManager = class , getLogger() = method
	}

}
