package com.AutomationQE.Framework;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.testng.ITestContext;

public class BaseTest {
	
	private ThreadLocal<WebDriver> locThread = new ThreadLocal<WebDriver>();
	private ThreadLocal<RemoteWebDriver> remoteThread = new ThreadLocal<RemoteWebDriver>();
	private static boolean grid;
	private static final String platform;
	private static final String defaultDownloadPath;
	static {
		platform = System.getProperty("os.name").toUpperCase();
		File file = new File(System.getProperty("user.dir") + File.separatorChar + "DOWNLOADS");
		file.mkdir();
		defaultDownloadPath = file.getAbsolutePath();
	}
	
	public void setUp(ITestContext context) throws MalformedURLException {
		grid = getGrid(context);
		setDriver(context);
	}
	
	private boolean getGrid(ITestContext context) {
		if(context.getCurrentXmlTest().getAllParameters().containsKey("grid") &&
				!context.getCurrentXmlTest().getParameter("grid").isEmpty())
			return true;
		return false;
	}
	
	private void setDriver(ITestContext context) throws MalformedURLException {
		if(!grid) {
			if(platform.contains("WINDOWS")) {
				if(context.getCurrentXmlTest().getParameter("browser").toUpperCase().equals("CHROME")) {
					System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir")+File.separatorChar+"geckodriver.exe");
					FirefoxDriver ffDriver = new FirefoxDriver(new FirefoxOptions(setFFCapabilities()));
					locThread.set(ffDriver);
				}
				else if(context.getCurrentXmlTest().getParameter("browser").toUpperCase().equals("FIREFOX") || 
						context.getCurrentXmlTest().getParameter("browser").toUpperCase().equals("FF")) {
					System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir")+File.separatorChar+"chromedriver.exe");
					ChromeDriver chromeDriver = new ChromeDriver(setChromeCapabilities());
					locThread.set(chromeDriver);
				}
				else if(context.getCurrentXmlTest().getParameter("browser").toUpperCase().equals("SAFARI")) { 
					System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir")+File.separatorChar+"safaridriver.exe");
					locThread.set(new SafariDriver());
				}
			}
			else{
				if(context.getCurrentXmlTest().getParameter("browser").toUpperCase().equals("CHROME")) {
					System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir")+File.separatorChar+"geckodriver");
					FirefoxDriver ffDriver = new FirefoxDriver(new FirefoxOptions(setFFCapabilities()));
					locThread.set(ffDriver);
				}
				else if(context.getCurrentXmlTest().getParameter("browser").toUpperCase().equals("FIREFOX") || 
						context.getCurrentXmlTest().getParameter("browser").toUpperCase().equals("FF")) {
					System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir")+File.separatorChar+"chromedriver");
					ChromeDriver chromeDriver = new ChromeDriver(setChromeCapabilities());
					locThread.set(chromeDriver);
				}
				else if(context.getCurrentXmlTest().getParameter("browser").toUpperCase().equals("SAFARI")) { 
					System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir")+File.separatorChar+"safaridriver");
					locThread.set(new SafariDriver());
				}
			}
		}
		else {
			RemoteWebDriver remoteDriver = null;
			URL gridURL = new URL(context.getCurrentXmlTest().getParameter("grid"));
			if(platform.contains("WINDOWS")) {
				if(context.getCurrentXmlTest().getParameter("browser").toUpperCase().equals("CHROME")) {
					System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir")+File.separatorChar+"geckodriver.exe");
					remoteDriver = new RemoteWebDriver(gridURL, new FirefoxOptions(setFFCapabilities()));
				}
				else if(context.getCurrentXmlTest().getParameter("browser").toUpperCase().equals("FIREFOX") || 
						context.getCurrentXmlTest().getParameter("browser").toUpperCase().equals("FF")) {
					System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir")+File.separatorChar+"chromedriver.exe");
					remoteDriver = new RemoteWebDriver(gridURL, setChromeCapabilities());
				}
				else if(context.getCurrentXmlTest().getParameter("browser").toUpperCase().equals("SAFARI")) { 
					System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir")+File.separatorChar+"safaridriver.exe");
					
				}
				remoteThread.set(remoteDriver);
			}
			else{
				if(context.getCurrentXmlTest().getParameter("browser").toUpperCase().equals("CHROME")) {
					System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir")+File.separatorChar+"geckodriver");
					remoteDriver = new RemoteWebDriver(gridURL, new FirefoxOptions(setFFCapabilities()));
				}
				else if(context.getCurrentXmlTest().getParameter("browser").toUpperCase().equals("FIREFOX") || 
						context.getCurrentXmlTest().getParameter("browser").toUpperCase().equals("FF")) {
					System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir")+File.separatorChar+"chromedriver");
					remoteDriver = new RemoteWebDriver(gridURL, setChromeCapabilities());
				}
				else if(context.getCurrentXmlTest().getParameter("browser").toUpperCase().equals("SAFARI")) { 
					System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir")+File.separatorChar+"safaridriver");
					
				}
				remoteThread.set(remoteDriver);
			}
		
		}
		
	}
	
	private DesiredCapabilities setFFCapabilities() {
		FirefoxProfile profile = new FirefoxProfile();
		DesiredCapabilities dc = DesiredCapabilities.firefox();
		profile.setAcceptUntrustedCertificates(false);
		profile.setAssumeUntrustedCertificateIssuer(true);
		profile.setPreference("browser.download.folderList", 2);
		profile.setPreference("browser.helperApps.alwaysAsk.force", false);
		profile.setPreference("browser.download.manager.showWhenStarting",false); 
		profile.setPreference("browser.download.dir", defaultDownloadPath); 
		profile.setPreference("browser.download.downloadDir", defaultDownloadPath); 
		profile.setPreference("browser.download.defaultFolder", defaultDownloadPath); 
		profile.setPreference("browser.helperApps.neverAsk.saveToDisk","text/anytext ,text/plain,text/html,application/plain" );
		dc.setCapability(FirefoxDriver.PROFILE, profile);
		return dc;
	}
	
	private ChromeOptions setChromeCapabilities() {
		ChromeOptions options = new ChromeOptions();
		options.setAcceptInsecureCerts(true);
		options.addArguments("start-maximized");
		
		String downloadFilepath = System.getProperty("user,dir")+File.separatorChar+"Downloads";
		HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
		chromePrefs.put("profile.default_content_settings.popups", 0);
		chromePrefs.put("download.default_directory", downloadFilepath);
		options.setExperimentalOption("prefs", chromePrefs);
		options.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
		options.setCapability(ChromeOptions.CAPABILITY, options);
		return options;
	}
	
}
