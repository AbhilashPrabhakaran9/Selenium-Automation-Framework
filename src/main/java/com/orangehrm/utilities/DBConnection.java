package com.orangehrm.utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.Logger;

import com.orangehrm.base.BaseClass;

public class DBConnection {
	
	//To make the database connection we need 3 variables
	private static final String DB_URL = "jdbc:mysql://localhost:3306/orangehrm";//static = constant value
	//3306 = port number , orangehrm = Database name
	private static final String DB_USERNAME = "root";
	private static final String DB_PASSWORD = ""; 
	private static final Logger logger = BaseClass.logger;
	
	//getDBConnection() = DB connection method
	public static Connection getDBConnection() //Return type is Connection because will return the object of Connection [conn]
	 
	{
		
		try {
			logger.info("Starting DB Connection...");
			//To make the connection, set the JDBC driver on DriverManager class
			
			Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);//Connection = interface
			//Stored in conn = Connection interface's variable or object
			
			logger.info("DB Connection successful");
			
			return conn;
		} 
		
		catch (SQLException e) {
			logger.error("Error while establishing the DB connection");
			e.printStackTrace();
			return null; //In case if you are not able to make the connection
		}
	}
	
/*
 * In Selenium Java programs, the Map interface (commonly implemented as a HashMap) is a crucial data structure 
 * used to store and manage data in key-value pairs, enhancing code organization, readability, and maintainability 
 * in automation frameworks. 	
	*/
	
	//Method to get the data from database
	//Get the employee details from DB and store in a map
	public static Map<String , String> getEmployeeDetails(String employee_id) //String employee_id = parameter
	//Map<string, string=""> = return type - Will store the values in Key value pair. Here using key as String data type
	//and value as String data type. Like if you get the data from database as first name as key and corresponding value like
	//ABHILASH employee name. So that's how we are going to store in map interface in hash map. [Reference of Map interface]
	{
		String query = "SELECT emp_firstname, emp_middle_name, emp_lastname FROM hs_hr_employee WHERE employee_id ="
				+employee_id;
		
		/*
		 * In Selenium Java programming, Map and HashMap are used to store and manage test data, locators, and configuration 
		 * settings in key-value pairs, which makes test scripts more scalable, readable, and maintainable. 

Map vs. HashMap
Map is an interface in the Java Collections Framework, defining the contract for storing data in unique key-value pairs.
HashMap is a concrete class that implements the Map interface using a hash table. It is widely used because it provides 
excellent performance (average constant time, O(1), for basic operations like put() and get()). It does not guarantee 
the order of elements. 
		 * 
		 */
		
		
		//We need to store the values that get employee details into map, so we need to create the object of HashMap interface
		Map<String , String> employeeDetails = new HashMap<> ();//employeeDetails = variable name
		//Map<String , String=""> = we have the reference of Map interface
		
		//TRY WITH RESOURCES STATEMENT [Inside this we will create different objects]
		//We need to create 3 objects to make the statement to execute the query, they are conn , stmt , rs
		
		try (Connection conn = getDBConnection();//Here we will create one connection object [reference of Connection interface] 
				//and call the getDBConnection() method. First of all we need to make the connection before writing the query. 
		    Statement stmt = conn.createStatement();//We have to create a statement, for this i am using the conn variable and 
				//createStatement() method, Using this i can create the statement. 
		    
			ResultSet rs = stmt.executeQuery(query)) //Executing the query and store the query result in ResultSet's object or 
		//reference variable
		{
			
			logger.info("Executing query: " + query);//After the above 3 statements, the query will be getting executed
			
			//To store all the values into the map, we need to iterate one by one
			//Here we will create 3 variables [firstName, middleName, lastName] to get the data from the query like emp_firstname
			//, emp_middle_name, emp_lastname 
			if(rs.next()) {
				String firstName = rs.getString("emp_firstname");
				String middleName = rs.getString("emp_middle_name");
				String lastName = rs.getString("emp_lastname");
				
				
				//Store in a map
				//Here employeeDetails = variable which we created before
				//put() method to store values to Map
				employeeDetails.put("firstName", firstName);// "firstName" = KEY, firstName = VALUE from database [Value i get
				//from getString() method with ResultSet's object [rs.getString("emp_firstname");]
				//KEY name i can change as per my interest
				employeeDetails.put("middleName", middleName!=null? middleName:"");//Using Ternary operator, using which we can 
				//check null values.If middleName not equal to null, it will print the middle name, Otherwise print blank value.
				employeeDetails.put("lastName", lastName);
				
				logger.info("Query executed successfully");
				logger.info("Employee Data Fetched: " +employeeDetails);
				
			}
			else {
				logger.error("Employee not found");
			}
		}
		
		catch(Exception e) {
			logger.info("Errr while executing query");
			e.printStackTrace(); //Print the error
		}
		return employeeDetails; //This is the variable we have where we store the values in Map interface.
				
				
				
	}

}
