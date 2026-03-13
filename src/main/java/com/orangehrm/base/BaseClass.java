package com.orangehrm.base;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;
import org.testng.asserts.SoftAssert;

import com.orangehrm.actiondriver.ActionDriver;
import com.orangehrm.utilities.ExtentManager;
import com.orangehrm.utilities.LoggerManager;



public class BaseClass {
	
	protected static Properties prop; 
	
	/*protected → access modifier (accessible within the same package and subclasses)

	static → belongs to the class, not to instances

	Properties → data type (a Java class from java.util)

	prop → variable name*/
	
	//Instance variable as protected access modifier
	//We are using static here because static belongs to the class level
	//Carries same instance (value) for the other test classes. 
	
	
	
	//I am using protected because this can be used within the same package
	//and it can be accessed in other package as well if that class will be extended by any other class.
	//Let say we are using other test class which is in other package, So we can easily use these 
	//properties in child classes.
	
	//protected static WebDriver driver;//Instance variable as protected access modifier
	
	//private static ActionDriver actionDriver;
	
	//Created 2 variables - driver and actionDriver using ThreadLocal class
	private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
	//<> = java generics
	
	private static ThreadLocal<ActionDriver> actionDriver = new ThreadLocal<>();
	
	/*
	 * In Selenium Java, ThreadLocal is a Java class used to create thread-safe WebDriver instances when running tests 
	 * in parallel.
       It ensures that each thread gets its own separate copy of a variable, instead of sharing one common object 
	   across threads.
	   
	   When you run tests in parallel (for example using TestNG parallel execution), multiple threads try to use the 
	   same WebDriver instance.

Without ThreadLocal, this causes problems like:

Browser sessions getting mixed up

Tests interfering with each other

Random failures

Session ID override errors


To avoid this, we use ThreadLocal<WebDriver> so that:

✅ Each test thread gets its own independent WebDriver
❌ No WebDriver sharing between threads
	 * 
	 */
	
	//DEFINED LOGGER here
		public static final Logger logger=LoggerManager.getLogger(BaseClass.class);//final = Once we initialize, we cannot
		//change it.
		
		//This is how we can create logger,
		//Note: Now we need to use this logger object and call different unimplemented methods from logger interface.
	
	//ThreadLocal = Java class
	//Creating Thread Local object for SoftAssert
	//softAssert = reference name
	protected ThreadLocal<SoftAssert> softAssert = ThreadLocal.withInitial(SoftAssert::new);//:: = lamda expression , new = keyword
	//withInitial = It initialize soft assert object only when it is accessed for the first time. so we can use this method 
	//and later on we can create one getter method to use that object
	//Access modifier is protected because Within the package it should be accessible.
	
	//GETTER METHOD FOR SOFT ASSERTION
	//I am creating a getter method, so that can be called to access the object outside other classes.
	//Copy getSoftAssert() method from here and can call inside DBVerificationTest.java
	public SoftAssert getSoftAssert() //getSoftAssert() = method
	//This will give the return type of SoftAssert object and simply we have to call one method softAssert.get()
	
	{
		return softAssert.get();
	}
	
	
	
	@BeforeSuite //Because we will load this before running the suite.
	public void loadConfig() throws IOException {
		//Load/start the configuration file
		prop=new Properties();
				
		//Read the file
		FileInputStream fis=new FileInputStream(System.getProperty("user.dir")+"/src/main/resources/config.properties");
		//I have updated the root directory of project --- For code pushing to github
				
		//Load the file
		prop.load(fis);
		
		logger.info("config.properties file loaded");
		
		//Start the extent report
		//ExtentManager.getReporter();	 -- This has been implemented in TestListener class	
		
		
	}
	
	
	@BeforeMethod
	@Parameters("browser")
	public synchronized void setup(String browser) throws IOException //Here we are using synchronized. What happens when we declare 
	//the method as Synchronized. In parallel testing or multithreading or synchronisation, only one thread  can 
	//execute the particular method which is declared as synchronised on particular object.So when i declared this
	//setup method as synchronized so only one thread can use this method at a time even though we will run the script
	//in parallel mode but it will be used at a time by single thread.So this setup method i have declared as 
	//synchronized and you can see one more method as well. so inside that we launch the browser and configure the browser
	//two methods are there which are internal methods of baseclass. So we declaring synchronization to the internal 
	//method which is launch browser.So at the same time only one thread will use this method to create the object 
	//of particular driver, let's say we are passing the chromedriver chrome over here, so we will use we create the 
	//the object of chromedriver over here and we just use the set method to set the driver.
	//For tearDown() method, we need/should use synchronization, while closing the browser.
	
	//For configureBrowser(), we don't need synchronisation.
	//Need synchronisation for setup(), launchBrowser(), tearDown() methods.
	{
		System.out.println("Setting up WebDriver for: " + this.getClass().getSimpleName()); /*We will get/print the 
		class name*/
		launchBrowser(browser);
		configureBrowser();
		staticWait(2);
		
		logger.info("WebDriver initialized and Browser maximized");
		logger.trace("This is a trace message");
		logger.error("This is a error message");
		logger.debug("This is a debug message");
		logger.fatal("This is a fatal message");
		logger.warn("This is a warn message");
		
		
		
		//Initialize the actionDriver only once
		/*if(actionDriver == null) {
			actionDriver=new ActionDriver(driver);
			logger.info("ActionDriver instance is created." + Thread.currentThread().getId());
			//Thread.currentThread().getId() will print the Thread id.
		}*/
		
		//Initialize ActionDriver for the current Thread
		actionDriver.set(new ActionDriver(getDriver()));//This is how we can initialize action driver by 
		//creating the object of action driver and we pass the getDriver because we are passing webdriver
		//instance in the action driver.This is how we can set the action driver that is using actionDriver
		//.set() method.
		
		logger.info("ActionDriver initialized for thread: " + Thread.currentThread().getId() );
		
		
	}
	
	//WRONG CODES - 1
	/*private synchronized void launchBrowser() {
		
		//Initialize the webDriver based on browser defined in config.properties file
		String browser=prop.getProperty("browser"); //getProperty() method can read properties from properties file.
		
		//I can also use switch case here instead of if else if ladder
		if(browser.equalsIgnoreCase("chrome")) {
		
		//Different Arguments when we launch the browser -Added these below steps before pushing code to github repository	
		//ChromeOptions - Behaviour of chrome
		ChromeOptions options = new ChromeOptions(); //Object of ChromeOptions class
		
		
		options.addArguments("--headless=true"); //Run chrome in headless mode
		options.addArguments("--disable-gpu"); //Disable GPU for headless mode
		options.addArguments("--window-size=1920,1080"); //Set window size
		options.addArguments("--disable-notifications"); //Disable browser notification
		options.addArguments("--no-sandbox"); //Required for some CI environments like
		options.addArguments("--disable-dev-shm-usage"); //Resolve issues in resource  
		
			
		//driver=new ChromeDriver();
		driver.set(new ChromeDriver(options));//New changes as per Thread
		
		ExtentManager.registerDriver(getDriver()); //We need to register the driver here. 
		//In ExtentManager, we have created one method called registerDriver(), for that we have stored the value in key value
		//pair in Map variable driverMap. So there we are using like to store thread ID and Webdriver instance. so we will call 
		//this method to register the driver when we start our driver.
		logger.info("ChromeDriver instance is created.");
				}
				
		else if(browser.equalsIgnoreCase("firefox")) {
			
		//Create FirefoxOptions
		FirefoxOptions options = new FirefoxOptions(); //Object of FirefoxOptions class
		options.addArguments("--headless=true"); //Run firefox in headless mode
		options.addArguments("--disable-gpu"); //Disable GPU rendering (useful for....)
		options.addArguments("--width=1920"); //Set browser width
		options.addArguments("--height=1080"); //Set browser height
		options.addArguments("--disable-notifications"); //Disable browser notification
		options.addArguments("--no-sandbox"); //Required for CI/CD environments
		options.addArguments("--disable-dev-shm-usage"); //Resolve issues in resource , Prevent crashes in low- ---  	
			
		//driver=new FirefoxDriver();
		driver.set(new FirefoxDriver(options));//New changes as per Thread
		ExtentManager.registerDriver(getDriver());
		logger.info("FirefoxDriver instance is created.");
				}
				
		else if(browser.equalsIgnoreCase("edge")) {
			
		//Create EdgeOptions
		EdgeOptions options = new EdgeOptions(); //Object of EdgeOptions class
		options.addArguments("--headless=true"); //Run edge in headless mode
		options.addArguments("--disable-gpu"); //Disable GPU acceleration
		options.addArguments("--window-size=1920,1080"); //Set window size
		options.addArguments("--disable-notifications"); //Disable pop-up notification
		options.addArguments("--no-sandbox"); //Needed for CI/CD
		options.addArguments("--disable-dev-shm-usage"); //Prevent resource-limited- -----
		
		//I have added the behaviors of browsers [options] because when we work with CI/CD with JENKINS environment, mostly we
		//run the script using HEADLESS mode.
			
		//driver=new EdgeDriver();
		driver.set(new EdgeDriver(options));//New changes as per Thread
		ExtentManager.registerDriver(getDriver());
		logger.info("EdgeDriver instance is created.");
				}
				
		else {
		throw new IllegalArgumentException("Browser not supported:" +browser);
				}
		
	}*/
	
	//CORRECT CODE - 1
	private synchronized void launchBrowser(String browser) {

	    //String browser = prop.getProperty("browser"); // read from config.properties
		boolean isHeadless = Boolean.parseBoolean(prop.getProperty("headless")); // read headless flag
		
		boolean seleniumGrid =
		        Boolean.parseBoolean(prop.getProperty("seleniumGrid"));
		String gridURL = prop.getProperty("gridURL");

		if (seleniumGrid) {
		    try {

		        switch (browser.toLowerCase()) {

		            case "chrome":
		                ChromeOptions chromeOptions = new ChromeOptions();
		                if (isHeadless) {
		                chromeOptions.addArguments("--headless=new", "--disable-gpu", "--window-size=1920,1080");
		                }
		                driver.set(new RemoteWebDriver(new URL(gridURL), chromeOptions));
		                break;

		            case "firefox":
		                FirefoxOptions firefoxOptions = new FirefoxOptions();
		                if (isHeadless) {
		                firefoxOptions.addArguments("-headless", "--disable-gpu", "--window-size=1920,1080");
		                }
		                driver.set(new RemoteWebDriver(new URL(gridURL), firefoxOptions));
		                break;

		            case "edge":
		                EdgeOptions edgeOptions = new EdgeOptions();
		                if (isHeadless) {
		                edgeOptions.addArguments("--headless=new", "--disable-gpu", "--window-size=1920,1080");
		                }
		                driver.set(new RemoteWebDriver(new URL(gridURL), edgeOptions));
		                break;

		            default:
		                throw new IllegalArgumentException("Browser Not Supported: " + browser);
		        }

		        logger.info("RemoteWebDriver instance created for Grid in headless mode");

		    } catch (MalformedURLException e) {
		        throw new RuntimeException("Invalid Grid URL", e);
		    }
		}
		
		else {
	    

	    switch (browser.toLowerCase()) {
	        case "chrome":
	            ChromeOptions chromeOptions = new ChromeOptions();

	            if (isHeadless) {
	                // Use modern headless mode for Chrome >= 109
	                chromeOptions.addArguments("--headless=new");
	            }
	            chromeOptions.addArguments("--window-size=1920,1080");
	            chromeOptions.addArguments("--disable-notifications");
	            chromeOptions.addArguments("--no-sandbox");
	            chromeOptions.addArguments("--disable-dev-shm-usage");
	            chromeOptions.addArguments("--disable-infobars");
	            chromeOptions.addArguments("--remote-allow-origins=*");

	            driver.set(new ChromeDriver(chromeOptions));
	            logger.info("ChromeDriver initialized for thread: " + Thread.currentThread().getId());
	            break;

	        case "firefox":
	            FirefoxOptions firefoxOptions = new FirefoxOptions();

	            //firefoxOptions.setHeadless(isHeadless); // correct headless method for Firefox
	            
	            if (isHeadless) {
	            	firefoxOptions.addArguments("-headless"); // modern headless
	            }
	            
	            
	            firefoxOptions.addArguments("--width=1920");
	            firefoxOptions.addArguments("--height=1080");
	            firefoxOptions.addArguments("--disable-notifications");
	            firefoxOptions.addArguments("--no-sandbox");
	            firefoxOptions.addArguments("--disable-dev-shm-usage");

	            driver.set(new FirefoxDriver(firefoxOptions));
	            logger.info("FirefoxDriver initialized for thread: " + Thread.currentThread().getId());
	            break;

	        case "edge":
	            EdgeOptions edgeOptions = new EdgeOptions();

	            if (isHeadless) {
	                edgeOptions.addArguments("--headless=new"); // modern headless
	            }
	            edgeOptions.addArguments("--window-size=1920,1080");
	            edgeOptions.addArguments("--disable-notifications");
	            edgeOptions.addArguments("--no-sandbox");
	            edgeOptions.addArguments("--disable-dev-shm-usage");

	            driver.set(new EdgeDriver(edgeOptions));
	            logger.info("EdgeDriver initialized for thread: " + Thread.currentThread().getId());
	            break;

	        default:
	            throw new IllegalArgumentException("Browser not supported: " + browser);
	    }

	    // Register driver for Extent Reports
	    ExtentManager.registerDriver(getDriver());
	}
		
	}
	
	
	//WRONG CODE - 2
	/*//Configure browser settings such as implicit wait, maximize the browser and navigate to the URL
	private void configureBrowser() {
	//Implicit wait
	int implicitWait = Integer.parseInt(prop.getProperty("implicitWait")); //For converting String
	//to integer value
				
	driver.get().manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));//1
				
	//Maximize the browser
	getDriver().manage().window().maximize();//2.I can give driver.get() OR getDriver(), both are okay 
	//for this 2 code lines (1,2)
				
	//Navigate to URL
	try {
		getDriver().get(prop.getProperty("url"));
	} catch (Exception e) {
		System.out.println("Failed to navigate to the URL " + e.getMessage());
	}
	
	
	}*/
	
	//CORRECT CODE - 2
	private void configureBrowser() {
	    int implicitWait = Integer.parseInt(prop.getProperty("implicitWait"));
	    getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));

	    // Maximize only if not headless
	    boolean isHeadless = Boolean.parseBoolean(prop.getProperty("headless"));
	    if (!isHeadless) {
	        getDriver().manage().window().maximize();
	    }

	    try {
	        getDriver().get(prop.getProperty("url"));
	    } catch (Exception e) {
	        logger.error("Failed to navigate to URL: " + e.getMessage());
	    }
	}
	
	
	
	@AfterMethod
	public synchronized void tearDown() {
		if(getDriver()!=null) {
			try {
				getDriver().quit();
			} catch (Exception e) {
				System.out.println("Unable to quit the driver: " + e.getMessage());
			}
		}
		logger.info("WebDriver instance is closed.");
		driver.remove();
		actionDriver.remove();//What is happening here is that Once tearDown() executes, means once it quit the driver
		//then simply we will remove the object which we created at beginning.
		
		//driver=null;
		//actionDriver=null;
		
		//ExtentManager.endTest(); -- This has been implemented in TestListener class
		//After each test, we will call endTest() to flush/end the report
		
	}

	//Getter method for prop because prop is protected on top
	public static Properties getProp() //Here static because we do the load configuration once like we load 
	//the configuration @BeforeSuite. So just we will make it static. so that the value will be static through
	//out script. So that we can use or utilize this value in other classes as well.  
	{
		return prop;
	}
/*	
	//Driver getter method
	public WebDriver getDriver() //Declared as public.So Whenever we want from outside of the package, we can easily
	//use this.We can call this method outside and we can access the driver in instance variable using this
	//method.
	{
		return driver;
	}
	*/
	
	//Getter method for WebDriver
	public static WebDriver getDriver() {
		
		if(driver.get()==null) {
			System.out.println("WebDriver is not initialized");
			throw new IllegalStateException("WebDriver is not initialized");
		}
		
		return driver.get();
	}
	
	//Getter method for ActionDriver
		public static ActionDriver getActionDriver() {
			
			if(actionDriver.get()==null) {
				System.out.println("ActionDriver is not initialized");
				throw new IllegalStateException("ActionDriver is not initialized");
			}
			
			return actionDriver.get();
		}
	
	
	
	//Driver setter method
	public void setDriver(ThreadLocal<WebDriver> driver) //These are the controlled access to the driver variable
	{
		this.driver=driver;//this will talks about the instance variable (Left side) and this is the local variable (
		//Right side) passing through parameter. This is how we can change the value of driver.
	}
	
	
	//Static Wait for pause the thread for couple of seconds or debugging the code
	public void staticWait(int seconds) {
		LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(seconds)); //Works with nano seconds
	}
	
	
	}
