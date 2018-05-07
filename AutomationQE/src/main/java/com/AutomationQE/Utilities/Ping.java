package com.AutomationQE.Utilities;

import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.impl.Log4JLogger;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotSelectableException;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.testng.Assert;

import com.AutomationQE.Framework.DriverManager;

@SuppressWarnings("deprecation")
public class Ping {

	private static int MAX_DURATION = 0;
	private static int PING_INTERVAL = 5;
	private static FluentWait<WebDriver> wait;
	static Log4JLogger log;
	static {
		log = new Log4JLogger();
		if(MAX_DURATION == 0) MAX_DURATION = 60;
		wait = new FluentWait<WebDriver>(DriverManager.getDriver())
				.ignoring(NoSuchElementException.class).ignoring(ElementNotVisibleException.class).ignoring(StaleElementReferenceException.class)
				.ignoring(ElementNotSelectableException.class).ignoring(WebDriverException.class)
				.pollingEvery(PING_INTERVAL, TimeUnit.SECONDS)
				.withTimeout(MAX_DURATION, TimeUnit.SECONDS);
	}
	
	public Ping(int durationInSeconds) {
		MAX_DURATION = durationInSeconds;
	}
	
	private void firstCheck(Object object) {
		if(!object.getClass().equals(By.class) || !object.getClass().equals(WebElement.class)) {
			log.error("Not a valid element or locator value...!");
			Assert.fail("Not a valid element or locator value...!");
		}
	}
	
	public boolean checkVisibility(Object object) {
		firstCheck(object);
		return ((object.getClass().equals(By.class)) ? 
				wait.until(ExpectedConditions.visibilityOfElementLocated((By) object)).isDisplayed() :
				wait.until(ExpectedConditions.visibilityOf((WebElement) object)).isDisplayed());
	}
	
	public boolean checkElementClickable(Object object) {
		firstCheck(object);
		return ((object.getClass().equals(By.class)) ? 
				wait.until(ExpectedConditions.elementToBeClickable((By) object)).isEnabled() :
				wait.until(ExpectedConditions.elementToBeClickable((WebElement) object)).isEnabled());
	}
	
	public WebElement getElementAfterVisibilityCheck(By locator) {
		checkVisibility(locator);
		return DriverManager.getDriver().findElement(locator);
	}
	
	public boolean checkAttributeIsPresent(Object object, String attribName) {
		firstCheck(object);
		WebElement element = null;
		if(object.getClass().equals(By.class))
			element = DriverManager.getDriver().findElement((By)object);
		else element = (WebElement)object;
		
		JavascriptExecutor executor = (JavascriptExecutor) DriverManager.getDriver();
		Object allAttrib = executor.executeScript("var items = {}; for (index = 0; index < arguments[0].attributes.length; ++index) { items[arguments[0].attributes[index].name] = arguments[0].attributes[index].value }; return items;", element);
		return allAttrib.toString().contains(attribName);
	}
	
	
}
