package com.AutomationQE.Framework;

import java.lang.reflect.Field;

import org.apache.commons.logging.impl.Log4JLogger;
import org.openqa.selenium.WebDriver;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestResult;
import org.testng.internal.BaseTestMethod;

/**
 * Author: 
 */public class LocalWebDriverListener implements IInvokedMethodListener {

    static Log4JLogger log = new Log4JLogger();

    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
        log.debug("BEGINNING: LocalWebDriverListener.beforeInvocation");
        if (method.isTestMethod()) {
            // get browser name specified in the TestNG XML test suite file
            String browserName = method.getTestMethod().getXmlTest().getLocalParameters().get("browserName");
            // get and set new instance of local WebDriver
            WebDriver driver = LocalDriverFactory.createInstance(browserName);
            DriverManager.setWebDriver(driver);
        } else {
            log.warn("Provided method is NOT a TestNG testMethod!!!");
        }
        log.debug("END: LocalWebDriverListener.beforeInvocation");
    }

    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
        log.debug("BEGINNING: LocalWebDriverListener.afterInvocation");
        if (method.isTestMethod()) {
            String browser = DriverManager.getBrowserInfo();
            try {
                // change the name of the test method that will appear in the report to one that will contain
                // also browser name, version and OS.
                // very handy when analysing results.
                BaseTestMethod bm = (BaseTestMethod)testResult.getMethod();
                Field f = bm.getClass().getSuperclass().getDeclaredField("m_methodName");
                f.setAccessible(true);
                String newTestName = testResult.getTestContext().getCurrentXmlTest().getName() + " - " + bm.getMethodName() + " - " + browser;
                log.info("Renaming test method name from: '" + bm.getMethodName() + "' to: '" + newTestName + "'");
                f.set(bm, newTestName);
            } catch (Exception ex) {
                System.out.println("ex:\n" + ex.getMessage());
            } finally {
                // close the browser
                WebDriver driver = DriverManager.getDriver();
                if (driver != null) {
                    driver.quit();
                }
            }
        }
        log.debug("END: LocalWebDriverListener.afterInvocation");
    }
}