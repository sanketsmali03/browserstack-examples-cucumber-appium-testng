package browserstack.stepdefs;

import browserstack.utils.Utility;
import com.browserstack.local.Local;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterMethod;

import java.io.FileReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;


public class BaseTest {

   // public static WebDriverWait wait;
    public static Map<String, String> options;
    protected static String URL = "https://bstackdemo.com";
    public static DesiredCapabilities caps = new DesiredCapabilities();
    private static String username;
    private static String accessKey;
    private static final String PASSED = "passed";
    private static final String FAILED = "failed";
    private static final String REPO_NAME = "browserstack-examples-cucumber-testng - ";
    private static final String WEBDRIVER_CHROME_DRIVER = "webdriver.chrome.driver";
    public static String env = "";
    public static JSONObject config;
    public static ThreadLocal<Map<String, String>> envCapabilities = new ThreadLocal<Map<String, String>>();
    DesiredCapabilities desiredCapabilities = new DesiredCapabilities();

    public static ThreadLocal<AndroidDriver<AndroidElement>> driverThreadLocal = new ThreadLocal<>();





    private static final ArrayList<Integer> numbers = new ArrayList<>();
    private static final Random random = new Random();

    static {
        for (int i = 0; i <= 2; i++) {
            numbers.add(i);
        }
    }

    public static synchronized int getUniqueNumber() {
        if (numbers.isEmpty()) {
            System.out.println("All numbers have been picked.");
            return -1;
        }
        int index = random.nextInt(numbers.size());
        int randomNumber = numbers.get(index);
        numbers.remove(index);
        return randomNumber;
    }


    @Before
    public synchronized void startup() throws Exception {
            DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
            JSONParser parser = new JSONParser();
            JSONObject config = (JSONObject) parser.parse(new FileReader("src/test/resources/config/config.json"));
            JSONArray envs = (JSONArray) config.get("environments");
            int index = BaseTest.getUniqueNumber();
            Map<String, String> envCapabilities = (Map<String, String>) envs.get(index);
            Set<String> envKeys = envCapabilities.keySet();
            envKeys.stream().filter(key -> key != null).forEach(key -> desiredCapabilities.setCapability(key, envCapabilities.get(key)));
            Map<String, String> capabilities = (Map<String, String>) config.get("capabilities");
            Set<String> capabilityKeys = capabilities.keySet();
            capabilityKeys.stream().filter(key -> key != null).forEach(key -> desiredCapabilities.setCapability(key, capabilities.get(key)));
            String username = System.getenv("BROWSERSTACK_USERNAME");
            username = username == null ? (String) config.get("username") : username;
            String accessKey = System.getenv("BROWSERSTACK_ACCESS_KEY");
            accessKey = accessKey == null ? (String) config.get("access_key") : accessKey;
            String app = System.getenv("BROWSERSTACK_APP_ID");
            if (app != null && !app.isEmpty()) {
                desiredCapabilities.setCapability("app", app);
            }

        driverThreadLocal.set(new AndroidDriver<>(new URL("http://" + username + ":" + accessKey + "@" + config.get("server") + "/wd/hub"), desiredCapabilities));
       // driverThreadLocal.set(driverThreadLocal);
        driverThreadLocal.get().manage().timeouts().implicitlyWait(120, TimeUnit.SECONDS);


    }

    public synchronized static AppiumDriver<?> getAppiumDriver() {
        return driverThreadLocal.get();
    }


    @After
    public void teardown(Scenario scenario) throws Exception {


        if (scenario.isFailed()) {
            Utility.setSessionStatus(driverThreadLocal.get(), FAILED,
                    String.format("%s failed.", scenario.getName()));

        } else {
            Utility.setSessionStatus(driverThreadLocal.get(), PASSED,
                    String.format("%s passed.", scenario.getName()));

        }


        driverThreadLocal.get().quit();

    }

    @AfterMethod
    public void tearDown() throws Exception {

        if (local != null) {
            local.stop();
        }

    }
}
