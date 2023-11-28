package browserstack.stepdefs;


import io.appium.java_client.MobileBy;
import io.appium.java_client.android.AndroidElement;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.util.List;


public class CommonSteps  {

	@Given("^I am on the 'Wikipedia' app$")
	public void iAmOnTheWikipediaApp() {
		// Assert.assertEquals(driver.currentActivity(),"org.wikipedia.main.MainActivity");
	}

	@When("^I submit the search term 'BrowserStack'$")
	public void iSubmitTheSearchTermBrowserStack() {

		AndroidElement skipElementBtn = (AndroidElement) new WebDriverWait(ThreadLocalDriver.getAppiumDriver(), 30).until(
				ExpectedConditions.elementToBeClickable(MobileBy.id(
						"org.wikipedia:id/fragment_onboarding_skip_button")));
		skipElementBtn.click();

		AndroidElement searchElement = (AndroidElement) new WebDriverWait(ThreadLocalDriver.getAppiumDriver(), 30).until(
				ExpectedConditions.elementToBeClickable(MobileBy.id("org.wikipedia:id/search_container")));
		searchElement.click();
		AndroidElement insertTextElement = (AndroidElement) new WebDriverWait(ThreadLocalDriver.getAppiumDriver(), 30).until(
				ExpectedConditions.elementToBeClickable(MobileBy.id("org.wikipedia:id/search_src_text")));
		insertTextElement.sendKeys("BrowserStack");
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Then("^The search result is not empty$")
	public void theSearchResultIsNotEmpty() {
		List<?> allProductsName = ThreadLocalDriver.getAppiumDriver().findElementsByClassName("android.widget.TextView");
        Assert.assertFalse(allProductsName.isEmpty());
	}


   
}