package com.orangehrm.actiondriver;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.orangehrm.base.BaseClass;
import com.orangehrm.utilities.ExtentManager;

public class ActionDriver  {

	private WebDriver driver; //variable driver
	
	private WebDriverWait wait; //For explicit wait
	
	public static final Logger logger = BaseClass.logger;
	
	//To initialise the driver and wait variables i need to create constructor of the class ActionDriver
	public ActionDriver(WebDriver driver) {
		this.driver=driver;
		
		//Hardcoding to Softcoding Duration.ofSeconds(30)
		int explicitWait= Integer.parseInt(BaseClass.getProp().getProperty("explicitWait"));//3rd way
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(explicitWait)); //This is how i cam make this dynamic
		
		//this.wait=new WebDriverWait(driver, Duration.ofSeconds(30)); --- 1st way
		
		/*int waitTime = Integer.parseInt(prop.getProperty("explicitWait")); ---- 2nd way and extends BaseClass
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(waitTime));*/
		logger.info("WebDriver instance is created");
	}
	
	//Different methods--> 
	//WAIT METHOD----Wait for element to be clickable, otherwise it will throw exception
	private void waitForElementToBeClickable(By by) {
		try {
			wait.until(ExpectedConditions.elementToBeClickable(by)); //Its a dynamic wait based on ExpectedConditions
		} catch (Exception e) {
			logger.error("Element is not clickable: " + e.getMessage());
		} //wait= WebDriverWait's reference , until() = method
		
		
	}
	
	//WAIT METHOD----Wait for Element to be visible
	private void waitForElementToBeVisible(By by) {
		try {
			wait .until(ExpectedConditions.visibilityOfElementLocated(by));
		} catch (Exception e) {
			logger.error("Element is not visible " + e.getMessage());
		}
	}
	
	//ACTION METHOD----Method to click an element 
	public void click(By by) //By class parameter
	{
		String elementDescription = getElementDescription(by);
		try {
			applyBorder(by,"green");
			waitForElementToBeClickable(by);
			driver.findElement(by).click();
			ExtentManager.logStep("clicked an element: " + elementDescription);
			logger.info("Clicked an element--->" + elementDescription);
			
		} catch (Exception e) {
			applyBorder(by,"red");
			System.out.println("Unable to click element " + e.getMessage());
			ExtentManager.logFailure(BaseClass.getDriver(), "Unable to click element:", elementDescription+"_unable to click");
			logger.error("Unable to click element");
		}
	}
	
	//ACTION METHOD----Method to enter text into an input field
	public void enterText(By by, String value) //Passing By class parameter and String parameter with value we have to enter
	{
		try {
			
			waitForElementToBeVisible(by);
			applyBorder(by,"green");
			//driver.findElement(by).clear();
			//driver.findElement(by).sendKeys(value);
			
			//Updated this step for easy access
			WebElement element=driver.findElement(by);
			element.clear();
			element.sendKeys(value);
			logger.info("Entered Text on: " + getElementDescription(by)+"--->"+value);
			
		} catch (Exception e) {
			applyBorder(by,"red");
			logger.error("Unable to enter the value:" + e.getMessage());
		}
		
	}
	
	//ACTION METHOD --- Method to get text from input field/element
	public String getText(By by) {
		try {
			
			waitForElementToBeVisible(by);
			applyBorder(by,"green");
			return driver.findElement(by).getText();
		} catch (Exception e) {
			applyBorder(by,"red");
			logger.error("Unable to get the text:" +e.getMessage());
			return "";
		}
	}
	
	//ACTION METHOD --- Method to compare two text -- Changed the return type
	public boolean compareText(By by,String expectedText) {
		try {
			waitForElementToBeVisible(by);
			
			String actualText=driver.findElement(by).getText();
			
			if(expectedText.equals(actualText)) {
				applyBorder(by,"green");
				logger.info("Text are matching:"+actualText+" equals "+expectedText);
				ExtentManager.logStepWithScreenshot(BaseClass.getDriver(), "Compare Text", "Text verified successfully! "+actualText +" equals "+expectedText);
				return true;
			}
			else {
				applyBorder(by,"red");
				logger.error("Text are not matching:"+actualText+" not equals "+expectedText);
				ExtentManager.logFailure(BaseClass.getDriver(), "Text comparison failed!", "Text comparison failed! "+actualText +" not equals "+expectedText);
				return false;
			}
		} catch (Exception e) {
			applyBorder(by,"red");
			logger.error("Unable to compare texts:"+ e.getMessage());
		}
		return false;
	}
	
	//ACTION METHOD --- Method to check if an element is displayed
	/*public boolean isDisplayed(By by) {
		try {
			waitForElementToBeVisible(by);
			boolean isDisplayed=driver.findElement(by).isDisplayed();
			
			if(isDisplayed) {
				System.out.println("Element is displaying");
				return isDisplayed;
			}
			else {
				return isDisplayed;
			}
		} catch (Exception e) {
			System.out.println("Element is not displayed:" + e.getMessage());
			return false;
		}
	}*/
	
	//Updated codes of ACTION METHOD --- Method to check if an element is displayed
	//This is how we can make the changes (eg:- ExtentManager.....) and automatically it will take the log
	public boolean isDisplayed(By by) {
		
		
		try {
			waitForElementToBeVisible(by);
			applyBorder(by,"green");
			logger.info("Element is displayed --->" +getElementDescription(by));
			ExtentManager.logStep("Element is displayed " + getElementDescription(by));
			
			ExtentManager.logStepWithScreenshot(BaseClass.getDriver(), "Element is displayed ", "Element is displayed " + getElementDescription(by));
			
			return driver.findElement(by).isDisplayed();
			} 
		
		catch (Exception e) {
			applyBorder(by,"red");
			logger.error("Element is not displayed:" + e.getMessage());
			ExtentManager.logFailure(BaseClass.getDriver(), "Element is not displayed: " , "Element is not displayed: "+ getElementDescription(by));
			return false;
		}
	}
	
	
	//WAIT METHOD --- Method to scroll to an element
	public void scrollToElement(By by) {
		try {
			applyBorder(by,"green");
			waitForElementToBeVisible(by);
			WebElement element = driver.findElement(by);
			JavascriptExecutor js=(JavascriptExecutor) driver;
			js.executeScript("arguments[0].scrollIntoView(true);",element);
			logger.info("Scrolled to element: " + by.toString());
		} catch (Exception e) {
			applyBorder(by,"red");
			logger.error("Unable to Scroll to element: " + by.toString() + "Exception: " + e.getMessage());
		}
		
		}
	
	//WAIT METHOD --- Wait for the page to load
	public void waitForPageLoad(int timeOutInSec)  
	{
	try {
		wait.withTimeout(Duration.ofSeconds(timeOutInSec)).until(WebDriver ->((JavascriptExecutor) WebDriver).
		executeScript("returndocument.readyState").equals("complete")); //Other states like loading, interactive
		
		logger.info("Page loaded successfully.");
	} catch (Exception e) {
		logger.error("Page did not load within " + timeOutInSec + "seconds.Exception: " + e.getMessage());
	}
		
		
		
	}
	
	//Method to get the description of an element using By locator
	public String getElementDescription(By locator)
	{
		//Check for null driver or locator to avoid Nullpointer exception
		if(driver==null)
			return "driver is null";
		
		if(locator == null)
			return "locator is null";
		
		try {
			//find the element using the locator
			WebElement element = driver.findElement(locator);
			
			//Get Element Attributes
			String name=element.getDomAttribute("name"); //getAttribute used earlier, it got deprecated so we are using
			//getDomAttribute()
			String id=element.getDomAttribute("id");
			String text=element.getText();
			String className = element.getDomAttribute("class");
			String placeHolder = element.getDomAttribute("placeholder");
			
			//Return the description based on element attributes
			if(isNotEmpty(name)) {
				return "Element with name: " + name;
			}
			
			else if(isNotEmpty(id)) {
				return "Element with id: " + id;
			}
			
			else if(isNotEmpty(text)) {
				return "Element with text:" + truncate(text,50);
			}
			else if(isNotEmpty(className)) {
				return "Element with class: " + className;
			}
			
			else if(isNotEmpty(placeHolder)) {
				
				return "Element with placeholder: " + placeHolder;
			}
		} catch (Exception e) {
			logger.error("Unable to describe the element" + e.getMessage());
		}
		return "Unable to describe the element";
		
		}
		
		//Utility method to check a String is not NULL or empty
		private boolean isNotEmpty(String value) {
			return value!=null && !value.isEmpty();
		
		}
		
		//Utility method to truncate long string
		private String truncate(String value, int maxLength) {
			if(value==null || value.length()<=maxLength) {
				return value;
			}
			
			return value.substring(0,maxLength)+"...";
		}
		
		//Utility method to border an element
		public void applyBorder(By by, String color) //applyBorder() = utility method / Prameters = By locator by, String color
		
		{
			
			try {
				
				//Locate the element
				WebElement element = driver.findElement(by); //Locator type =  by
				
				//Apply the border [Javascript code]
				String script = "arguments[0].style.border='3px solid "+color+"'"; //arguments[0] = 1st argument
				//apply.border='3px solid = CSS code
				
				JavascriptExecutor js = (JavascriptExecutor) driver; //JavascriptExecutor for executing the above java script
				//(JavascriptExecutor) driver = Type casting
				js.executeScript(script, element);
				
				logger.info("Applied the border with color "+color+ "to element: " +getElementDescription(by));
				//getElementDescription(by) = method
			} 
			
			catch (Exception e) {
				logger.warn("Failed to apply border to an element: " +getElementDescription(by),e);
			}
			
		}
		
		//****************NEWLY ADDED METHODS****************
		
		//Method to select a dropdown by visible text
		public void selectByVisibleText(By by, String value) {
			try {
				WebElement element = driver.findElement(by);
				new Select(element).selectByVisibleText(value);
				applyBorder(by,"green");
				logger.info("Selected dropdown value: " + value);
			} catch (Exception e) {
				applyBorder(by, "red");
				logger.error("Unable to select drop down value: " + value, e);
			}
		}
		
		//Method to select a dropdown by value
		public void selectByValue(By by, String value) {
			try {
				WebElement element = driver.findElement(by);
				new Select(element).selectByValue(value);
				applyBorder(by,"green");
				logger.info("Selected dropdown value by actual value: " + value);
			} catch (Exception e) {
				applyBorder(by, "red");
				logger.error("Unable to select drop down by value: " + value, e);
			}
		}
		
		//Method to select a dropdown by index
		public void selectByIndex(By by, int index) {
			
			try {
				WebElement element = driver.findElement(by);
				new Select(element).selectByIndex(index);
				applyBorder(by,"green");
				logger.info("Selected dropdown value by index: " + index);
			} catch (Exception e) {
				applyBorder(by, "red");
				logger.error("Unable to select drop down by value: " + index, e);
			}
			
		}
		
		//Method to get all options from a dropdown
		public List<String> getDropdownOptions(By by) {
			List<String> optionsList = new ArrayList<>();
			
			try {
				WebElement dropdownElement = driver.findElement(by);
				Select select = new Select(dropdownElement);
				for(WebElement option : select.getOptions()) {
					optionsList.add(option.getText());
				}
				applyBorder(by, "green");
				logger.info("Retrieved dropdown options for " + getElementDescription(by));
			} catch (Exception e) {
				applyBorder(by, "red");
				logger.error("Unable to get dropdown options: " + e.getMessage());
			}
			
			return optionsList;
			
		}
		
		//*************************JAVASCRIPT utility methods****************************
		
		//Method to click using JavaScript
		public void clickUsingJS(By by) {
			try {
				WebElement element = driver.findElement(by);
				((JavascriptExecutor)driver).executeScript("arguments[0].click();", element);
				applyBorder(by, "green");
				logger.info("Clicked element using JavaScript: " + getElementDescription(by));
			} catch (Exception e) {
				applyBorder(by, "red");
				logger.error("Unable to click using JavaScript", e);
			}
			
		}
		
		//Method to scroll to the bottom of the page
		public void scrollToBottom() {
			((JavascriptExecutor)driver).executeScript("window.scrollTo(0,document.body.scrollHeight);");
			logger.info("Scrolled to the bottomof the page.");
			
		}
		
		//Method to highlight an element using JavaScript
		public void highlightElementJS(By by) {
			try {
				WebElement element = driver.findElement(by);
				((JavascriptExecutor)driver).executeScript("arguments[0].style.border='3px solid yellow'",element);
				logger.info("Highlighted element using JavaScript: " + getElementDescription(by));
			} catch (Exception e) {
				logger.error("Unable to highlight element using JavaScript", e);
			}
			
		}
		
		//*************************WINDOW AND FRAME HANDLING*************************
		
		//Method to switch between browser windows
		public void switchToWindow(String windowTitle) {
			
			try {
				Set<String> windows = driver.getWindowHandles();
				for(String window : windows) {
					driver.switchTo().window(window);
					if(driver.getTitle().equals(windowTitle)) {
						logger.info("Switched to window: " + windowTitle);
						return;
					}
				}
				logger.warn("Window with title " + windowTitle + " not found.");
			} catch (Exception e) {
				logger.error("Unable to switch window", e);
			}
		}
		
		//Method to switch to an iframe
		public void switchToFrame(By by) {
			try {
				driver.switchTo().frame(driver.findElement(by));
				logger.info("Switched to iframe: " + getElementDescription(by));
			} catch (Exception e) {
				logger.error("Unable to switch to iframe", e);
			}
		}
		
		//Method to switch back to the default content
		public void switchToDefaultContent() {
			driver.switchTo().defaultContent();
			logger.info("Switched back to default content.");
		}
		
		//********************ALERT HANDLING******************
		
		//Method to accept an alert popup
		public void acceptAlert() {
			try {
				driver.switchTo().alert().accept();
				logger.info("Alert accepted");
			} catch (Exception e) {
				logger.error("No alert found to accept",  e);
			}
		}
		
		//Method to dismiss an alert popup
		public void dismissAlert() {
			try {
				driver.switchTo().alert().dismiss();
				logger.info("Alert dismissed");
			} catch (Exception e) {
				logger.error("No alert found to dismiss",  e);
			}
		}
		
		//Method to get alert text
		public String getAlertText() {
			try {
				return driver.switchTo().alert().getText();
			} catch (Exception e) {
				logger.error("No alert text found",  e);
				return "";
			}
		}
		
		//****************BROWSER ACTIONS*********************
		
		//refreshPage
		public void refreshPage() {
			try {
				driver.navigate().refresh();
				ExtentManager.logStep("Page refreshed successfully.");
				logger.info("Page refreshed successfully.");
			} catch (Exception e) {
				ExtentManager.logFailure(BaseClass.getDriver(), "Unable to refresh page", "refresh_page_failed");
				logger.error("Unable to refresh page: " + e.getMessage());
			}
		}
		
		//getCurrentURL
		public String getCurrentURL() {
			try {
				String url = driver.getCurrentUrl();
				ExtentManager.logStep("Current URL fetched: " + url);
				logger.info("Current URL fetched: " + url);
				return url;
			} catch (Exception e) {
				ExtentManager.logFailure(BaseClass.getDriver(), "Unable to fetch current URL", "get_current_url_failed");
				logger.error("Unable to fetch current URL: " + e.getMessage());
				return null;
			}
		}
		
		//maximizeWindow
		public void maximizeWindow() {
			try {
				driver.manage().window().maximize();
				ExtentManager.logStep("Browser window maximized.");
				logger.info("Browser window maximized.");
			} catch (Exception e) {
				ExtentManager.logFailure(BaseClass.getDriver(), "Unable to maximize window", "maximize_window_failed");
				logger.error("Unable to maximize window: " + e.getMessage());
			}
		}
		
		//********************ADVANCED WEBELEMENT ACTIONS**************************
		
		//moveToElement
		public void moveToElement(By by) {
			String elementDescription = getElementDescription(by);
			
			try {
				Actions actions = new Actions(driver);
				actions.moveToElement(driver.findElement(by)).perform();
				ExtentManager.logStep("Moved to element: " + elementDescription);
				logger.info("Moved to element --> " +  elementDescription);
			} catch (Exception e) {
				ExtentManager.logFailure(BaseClass.getDriver(), "Unable to move to element", elementDescription + "_move_failed");
				logger.error("Unable to move to element: " + e.getMessage());
			}
		}
		
		//dragAndDrop
		public void dragAndDrop(By source, By target) {
			
			String sourceDescription = getElementDescription(source);
			String targetDescription = getElementDescription(target);
			
			try {
				Actions actions = new Actions(driver);
				actions.dragAndDrop(driver.findElement(source), driver.findElement(target)).perform();
				ExtentManager.logStep("Dragged element: " + sourceDescription + " and dropped on " + targetDescription);
				logger.info("Dragged element: " + sourceDescription + " and dropped on " + targetDescription);
			} catch (Exception e) {
				ExtentManager.logFailure(BaseClass.getDriver(), "Unable to drag and drop", sourceDescription + "_drag_failed");
				logger.error("Unable to drag and drop: " + e.getMessage());
			}
			
			
		}
		
		//doubleClick
		public void doubleClick(By by) {
			String elementDescription = getElementDescription(by);
			
			try {
				Actions actions = new Actions(driver);
				actions.doubleClick(driver.findElement(by)).perform();
				ExtentManager.logStep("Double-clicked on element: " + elementDescription);
				logger.info("Double-clicked on element--> " + elementDescription);
			} catch (Exception e) {
				ExtentManager.logFailure(BaseClass.getDriver(), "Unable to double-click element", elementDescription + 
						"_doubleclick_failed");
				logger.error("Unable to double-click element: " + e.getMessage());
			}
			
		}
		
		//rightClick
		public void rightClick(By by) {
			String elementDescription = getElementDescription(by);
			
			try {
				Actions actions = new Actions(driver);
				actions.contextClick(driver.findElement(by)).perform();
				ExtentManager.logStep("Right-clicked on element: " + elementDescription);
				logger.info("Right-clicked on element--> " + elementDescription);
			} catch (Exception e) {
				ExtentManager.logFailure(BaseClass.getDriver(), "Unable to right-click element", elementDescription + 
						"_rightclick_failed");
				logger.error("Unable to right-click element: " + e.getMessage());
			}
			
		}
		
		//sendKeysWithActions
		public void sendKeysWithActions(By by, String value) {
			
			String elementDescription = getElementDescription(by);
			
			try {
				Actions actions = new Actions(driver);
				actions.sendKeys(driver.findElement(by), value).perform();
				ExtentManager.logStep("Sent keys to element: " + elementDescription + " | value: " + value);
				logger.info("Sent keys to element --> " + elementDescription + " | value: " + value);
			} catch (Exception e) {
				ExtentManager.logFailure(BaseClass.getDriver(), "Unable to send keys", elementDescription + 
						"_sendkeys_failed");
				logger.error("Unable to send keys to element: " + e.getMessage());
			}
			
		}
		
		//clearText
		public void clearText(By by) {
			
			String elementDescription = getElementDescription(by);
			
			try {
				driver.findElement(by).clear();
				ExtentManager.logStep("Cleared text in element: " + elementDescription);
				logger.info("Cleared text in element --> " + elementDescription);
			} catch (Exception e) {
				ExtentManager.logFailure(BaseClass.getDriver(), "Unable to clear text", elementDescription + 
						"_clear_failed");
				logger.error("Unable to clear text in element: " + e.getMessage());
			}
			
			
		}
		
		//Method to upload a file
		public void uploadFile(By by, String filePath) {
			
			try {
				driver.findElement(by).sendKeys(filePath);
				applyBorder(by, "green");
				logger.info("uploaded file: " , filePath);
			} catch (Exception e) {
				applyBorder(by, "red");
				logger.error("Unable to upload file: " + e.getMessage());
			}
			
		}
		
		
		
		}	
	
	//Note:- What we are going to do, whatever methods we have written so some methods we can directly call in our like logstep
//in our test methods and some of the methods we can directly put inside action related methods which we have written in 
//action driver.
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	 
	

