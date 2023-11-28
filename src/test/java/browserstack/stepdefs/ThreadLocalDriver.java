package browserstack.stepdefs;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;

public class ThreadLocalDriver {

    private static ThreadLocal<AppiumDriver<?>> appiumDriver = new ThreadLocal<>();

    public synchronized static void setAppiumDriver(AndroidDriver<AndroidElement> driver) {
        appiumDriver.set(driver);
    }



    public synchronized static AppiumDriver<?> getAppiumDriver() {
        return BaseTest.getAppiumDriver();
    }
    
    

}
