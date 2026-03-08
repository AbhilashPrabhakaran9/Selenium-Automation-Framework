package com.orangehrm.utilities;

import java.util.List;

import org.testng.annotations.DataProvider;



public class DataProviders 

{
	//FILE PATH
	//We need to read the excel file , user.dir = root directory of project
	private static final String FILE_PATH = System.getProperty("user.dir")+"/src/test/resources/testdata/TestData.xlsx";
	
	//
	@DataProvider(name="validLoginData") //I can give any name here but while calling this method (validLoginData() method name )
	//i need to provide this data provider name
	public static Object[] [] validLoginData() //validLoginData() method name 
	{
		return getSheetData("validLoginData"); //validLoginData = sheetName
	}
	
	
	@DataProvider(name="inValidLoginData") //I can give any name here
	public static Object[] [] inValidLoginData() //inValidLoginData() method name 
	{
		return getSheetData("inValidLoginData"); //inValidLoginData = sheetName
	}
	
	@DataProvider(name="emplVerification") //I can give any name here
	public static Object[] [] emplVerification() //emplVerification method name 
	{
		return getSheetData("emplVerification"); //emplVerification = sheetName
	}
	
	
	//Creating one method here. To work with Data providers, we need to return the data as object of 2D array that will accept
	//any data type. Here i am giving the return type as Object of 2D array. 
	private static Object[] [] getSheetData(String sheetName) //From sheetName from which we will get the data.
	
	//Now we have to call getSheetData() method from ExcelReaderUtility class.
	{
		List<String[]> sheetData = ExcelReaderUtility.getSheetData(FILE_PATH, sheetName); //This will return List of array of 
		//strings (List<String[]>) , sheetData = variable
		
		//We need to create the object of 2D array to store those values
		//data = name
		//We need to create object of Object array, Now we need to initialize with sheetData with number of rows giving as .size
		//so it will gives the number of rows that will be the total number of rows in excel sheet. [sheetData.size()]
		//Total number of columns in the first row = [sheetData.get(0).length]. This is how we can initialize both.
		Object[] [] data = new Object[sheetData.size()] [sheetData.get(0).length];	
		
		//Note:- sheetData.size() = sheetData is a List like List<String[]> ,  .size() = a method of List, It returns the number 
		//of elements (rows) in the list.
		
		//sheetData.get(0).length = sheetData.get(0) gets the first element from the list , That first element is usually an array
		//(like Object[]) , .length is a property of arrays 
		//(not a method). It returns the number of columns in that array
		
		
	//Writing for loop to iterate the entire rows to get the data from the excel sheet	
	for(int i=0;i<sheetData.size();i++) {
		data[i] = sheetData.get(i); //data[i] = represents the entire rows, will get the values and store in data
		//sheetData.get(i) = This represents the entire rows, and we get the username and password columns and that will be 
		//converted. Because we have already iterated through ExcelReaderUtility all rows and columns and that will be stored
		//in this data object (2D array) (Object[] [] data) when we call this method. (getSheetData(String sheetName)). So it will
		//be stored in this 2D object array (Object[] []). So our username,password column values will be stored in this
		//2D array.
		
	}
	
	return data;

}
	
}


