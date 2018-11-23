/*
 *This test is for Easy Access to Offers & Verification of message display with total search results
 *
 *Created By: Shraddha Jamdade
 *Date: 22-Nov-2018
 *  
 */

package com.raisin.challenge;

import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;

import com.raisin.basetest.BaseTest;
import com.raisin.basetest.Xls_Reader;
import com.raisin.hybrid.util.DataReader;
import com.raisin.hybrid.util.ExtentManager;
import com.relevantcodes.extentreports.LogStatus;

import org.testng.asserts.SoftAssert;


public class EasyAccessOffers extends BaseTest {
	public String TC = "EasyAccess_TC";
	public Xls_Reader xls = null;

	//Soft Asserts are declared
	@BeforeMethod
	public void setup() {
		s_assert = new SoftAssert();
	}

	@Test(dataProvider = "tcdata")
	public void EasyAccessTest(Hashtable<String, String> ht) 
	{

		//Load all locators from config.properties file
		loadconfig();
		report = ExtentManager.getInstance();
		test = report.startTest("EasyAccess_TC" + ht.toString());

		// read the runmode status of testcase and data combination and execute
		// the testcase accordingly
		if (!DataReader.isRunnable(xls, "Suite", TC) || ht.get("RunMode").equals("N")) {
			test.log(LogStatus.SKIP, TC + " is passed");
			throw new SkipException("skip testcase as runmode flag is 'N'");
		}
		launchbrowser(ht.get("Browser"));
		test.log(LogStatus.INFO, "Browser launch successfully");
		driver.navigate().to(ht.get("URL"));
		//Wait until Pop up appears
		dynamicwait(100, "Ok_xpath");
		//Click on Ok button over pop up
		click("Ok_xpath");
		//Wait until control returns to offer page
		dynamicwait(30, "Offers_xpath");
		takescreenshot(test);
		//Verify that Offer page is displayed
		s_assert.assertTrue(getelemnt("Offers_xpath").isDisplayed());
		test.log(LogStatus.PASS, "Offers page is launch successfully");

		//Select Easy Access checkbox
		selectchkbox("EasyAccess_xpath");
		dynamicwait(30, "EasyAccess_xpath");
		takescreenshot(test);
		//Verify that Easy Access Checkbox is selected
		s_assert.assertTrue(getelemnt("EasyAccess_xpath").isSelected());
		test.log(LogStatus.PASS, "Easy Access Checkbox on Offers page is selected successfully");

		//Verify Message gets displayed post selection of Easy Access Checkbox on Offer page
		dynamicwait(30, "Msg_xpath");
		String SearchResult=gettext("Msg_xpath");
		s_assert.assertEquals(SearchResult,"10 offers match your search");
		takescreenshot(test);
		test.log(LogStatus.PASS,SearchResult + " is displayed successfully");

		int MsgResult=Integer.parseInt(SearchResult.replaceAll("\\D", ""));

		((JavascriptExecutor) driver).executeScript("scroll(800,800)");

		click("Showbtn_xpath");


		((JavascriptExecutor) driver).executeScript("scroll(1200,1200)");
		driver.manage().timeouts().implicitlyWait(500, TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(1500, TimeUnit.SECONDS);

		//To count all offer search results 
		By cssSelector=By.cssSelector("[id^='prot-item-']");
		List <WebElement> Elm= driver.findElements(cssSelector);
		int ListSize=Elm.size();
		s_assert.assertEquals(MsgResult, ListSize);
		takescreenshot(test);
		test.log(LogStatus.PASS, "Total of offers match with the Search results message is correctly displayed");
	}

	//This method will display all soft asserts

	@AfterMethod
	public void teardowbn() {
		try {
			s_assert.assertAll();
		} catch (Error e) {
			test.log(LogStatus.FAIL, e.getMessage());
		}
		report.endTest(test);
		report.flush();

	}

	@AfterClass
	public void close() {
		if (driver != null)
			driver.quit();

	}

	@DataProvider(name = "tcdata")
	public Object[][] tcdata() {
		xls = new Xls_Reader(System.getProperty("user.dir") + "\\TestData\\Raisin_TestData.xlsx");
		return DataReader.getdata(xls, "TestData", TC);

	}
}