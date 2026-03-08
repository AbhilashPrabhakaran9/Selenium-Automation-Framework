package com.orangehrm.utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelReaderUtility {
	// We need methods here, 1st method is get sheet data to get the data from
	// worksheet and another one is how we are
	// going to capture the cell value.
	// Note: Data from excel are of different formats like Boolean (True/False),
	// Numeric, date format

	// Main or major method
	public static List<String[]> getSheetData(String filePath, String sheetName) // static = call the method using class
																					// name
	// List<String[]> = List of arrays of strings [return type] = i am getting
	// multiple data in excel sheet
	// getSheetData() = method
	// Will do inserting the return type later.

	{

		// Creating data variable which is defined as a list of arrays of string
		List<String[]> data = new ArrayList<>(); // Object of array list
		// We need one data variable where how we are going to store the data. So i will
		// create list of array of string (List<String[]>) , data =
		// reference variable , new ArrayList<>() = Object of array list. Here we are
		// using arraylist because that is resizable
		// array and we can store the data in array list, because we don't know how many
		// data will be there like how many data rows will be there.

		// We will give the reference as List interface.

		// This will finally store the data when we read the excel file.
		// -------
		// We need to create to read the data to access the excel file, we need to
		// create the object of file input stream and we
		// need to create the object of workbook as well, the particular sheet like how
		// many sheets we are working on with excel
		// file we need to create the object of that as well. When we work with file
		// input stream then it will throws the
		// declaration exception, then either we need to do try catch or we need to
		// close the declaration. For to doing this
		// i will use try with resources statement.

		try (FileInputStream fis = new FileInputStream(filePath); Workbook workbook = new XSSFWorkbook(fis))
		// FileInputStream fis=new FileInputStream(filePath) = Creating the object of
		// FileInputStream class with file path passing,
		// Using this fis we will read the excel file and to work with excel file we
		// need to create another object.

		// Note:- We have written just inside try with resources. This statement is
		// called try with resources [try(FileInputStream
		// fis=new FileInputStream(filePath); Workbook workbook= new XSSFWorkbook()).]
		// Inside that
		// bracket we can write we can give the statements. So no need to close using
		// finally block like automatically once
		// resources are done like objects are done with whatever activities which we
		// are going to perform with those objects,
		// it will be automatically closed like this fis. I am going to create another
		// one for workbook. [Workbook workbook= new
		// XSSFWorkbook()]

		{

			Sheet sheet = workbook.getSheet(sheetName); // Accessing sheet

			if (sheet == null) {
				throw new IllegalArgumentException("Sheet " + sheetName + " doesn't exists");
			}

			// iterate through rows
			for (Row row : sheet) // for each loop , Row = an interface

			{
				if (row.getRowNum() == 0) // Ignoring the first row containing username ,password headers and continue
											// with next row
				{
					continue;
				}

				// Read all cells in the row
				// I will read row wise first and then cells wise
				// I will create one temporary variable (rowData) like array to store the values
				// and later on we will add that into
				// our main data (data)
				List<String> rowData = new ArrayList<>(); // rowData = temporary variable
				for (Cell cell : row) // for each loop - iterating through entire row
				// cell - Cell interface reference variable
				{
					rowData.add(getCellValue(cell));
				}

				// Convert rowData to String[] array
				data.add(rowData.toArray(new String[0])); // We are going to create the array with zero object, we can
															// simply use
				// new String[0]. So this will convert into string array and then we will add
				// that array into our main array [data]
				// which we have declared at the top.
			}

		}

		catch (IOException e) {
			e.printStackTrace(); // In Java, e.printStackTrace(); is a method call used to print detailed
									// information about an
			// exception when an error occurs. It is commonly used inside a catch block.
		}

		return data; // That will give us List<String[]> = List of arrays of strings [return type]

	}

	// *************************

	// Cell value method
	private static String getCellValue(Cell cell) // Cell - an interface
	{
		if (cell == null) // cell value is null here
		{
			return "";

		}

		switch (cell.getCellType()) // Here we are writing switch cases because we get different data. So to format
									// those data,
		// we use switch case

		{
		case STRING:
			return cell.getStringCellValue(); // getting string value

		case NUMERIC:
			if (DateUtil.isCellDateFormatted(cell)) // Date format
			{
				return cell.getDateCellValue().toString();
			}
			return String.valueOf((int) cell.getNumericCellValue()); // Decimal value , cast with int for converting to
																		// integer
		// value

		case BOOLEAN:
			return String.valueOf(cell.getBooleanCellValue()); // True or false Boolean value

		default:
			return "";
		}

	}

}
