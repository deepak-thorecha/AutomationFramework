package com.AutomationQE.Framework;

import org.apache.commons.logging.impl.Log4JLogger;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

public class DriverManager {

    /*
    This simple line does all the mutlithread magic.
    For more details please refer to the link above :)
    */
    private static ThreadLocal<WebDriver> remoteWebDriver = new ThreadLocal<WebDriver>();
    static Log4JLogger log;

    static {
        log = new Log4JLogger();
        		//Log4JLogger.getLogger(DriverManager.class);
    }

    public static WebDriver getDriver() {
        log.debug("Getting instance of remote driver");
        return remoteWebDriver.get();
    }

    static void setWebDriver(WebDriver driver) {
        log.debug("Setting the driver");
        remoteWebDriver.set(driver);
    }

    /**
     * Returns a string containing current browser name, its version and OS name.
     * This method is used in the the *WebDriverListeners to change the test name.
     * */
    public static String getBrowserInfo(){
        log.debug("Getting browser info");
        // we have to cast WebDriver object to RemoteWebDriver here, because the first one does not have a method
        // that would tell you which browser it is driving. (sick!)
        Capabilities cap = ((RemoteWebDriver) DriverManager.getDriver()).getCapabilities();
        String b = cap.getBrowserName();
        String os = cap.getPlatform().toString();
        String v = cap.getVersion();
        return String.format("%s v:%s %s", b, v, os);
    }
}