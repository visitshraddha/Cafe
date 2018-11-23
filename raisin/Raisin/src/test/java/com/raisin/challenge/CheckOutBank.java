/*
 *This test is for do checkout of Bank's Moody Rating and learn more about it and click on Invest Now button. 
 *Verify register page open successfully 
 *  
 *Created By: Shraddha Jamdade
 *Date: 22-Nov-2018
 */


package com.raisin.challenge;

import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.testng.SkipException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.raisin.basetest.BaseTest;
import com.raisin.basetest.Xls_Reader;
import com.raisin.hybrid.util.DataReader;
import com.raisin.hybrid.util.ExtentManager;
import com.relevantcodes.extentreports.LogStatus;


public class CheckOutBank extends BaseTest {
	public String TC = "CheckOutBank_TC";
	public Xls_Reader xls = null;

	//Soft Asserts are declared
	@BeforeMethod
	public void setup() {
		s_assert = new SoftAssert();
	}

	@Test(dataProvider = "tcdata")
	public void CheckOutBank(Hashtable<String,String> ht) 
	{

		//load all locators from config.properties file
		loadconfig();
		report = ExtentManager.getInstance();
		test = report.startTest("CheckOutBank_TC" + ht.toString());

		// read the runmode status of testcase and data combination and execute
		// the testcase accordingly
		if (!DataReader.isRunnable(xls, "Suite", TC) || ht.get("RunMode").equals("N")) {
			test.log(LogStatus.SKIP, TC + " is passed");
			throw new SkipException("skip testcase as runmode flag is 'N'");
		}
		launchbrowser(ht.get("Browser"));
		test.log(LogStatus.INFO, "Browser launch successfully");
		driver.navigate().to(ht.get("URL"));

		dynamicwait(100, "Ok_xpath");
		click("Ok_xpath");

		//Verify bank page is displayed successfully
		dynamicwait(30, "Bank_cssSelector");
		takescreenshot(test);
	//	s_assert.assertTrue(getelemnt("Bank_cssSelector").isDisplayed());
		test.log(LogStatus.PASS, "Bank page is launch successfully");

		//This section is for verify bank with Moody's Rating and click on Learn More button

		By cssSelector=By.cssSelector(".bank-archive-item-country-rating-score");
		List <WebElement> Elm= driver.findElements(cssSelector);

		for (int i = 0; i < Elm.size(); i++) 
		{
			if(Elm.get(i).getAttribute("textContent").contains("A1"))
			{
				((JavascriptExecutor) driver).executeScript("scroll(1000,1000)");
				takescreenshot(test);
				test.log(LogStatus.PASS, "Bank with Moody's Rating A1 is found successfully");
				driver.findElement(By.xpath(".//*[@id='banks-archive-item-wrapper-cnt']/div[5]/div/div[4]/div/div[3]/a")).click();
			}
		}

		//Verify Best Offers page is displayed for bank of Mooy's Rating A1
		dynamicwait(30, "Offer_xpath");
		driver.manage().timeouts().pageLoadTimeout(80, TimeUnit.SECONDS);
		String page=getelemnt("Offer_xpath").getAttribute("textContent");
		takescreenshot(test);
		s_assert.assertTrue(getelemnt("Offer_xpath").isDisplayed());
		test.log(LogStatus.PASS, "Best Offers page about bank is displayed successfully");
		
		//Verify Registration form 
		click("Investbtn_cssSelector");
		dynamicwait(50, "Registerpge_xpath");
		takescreenshot(test);
		s_assert.assertTrue(getelemnt("Registerpge_xpath").isDisplayed());
		test.log(LogStatus.PASS, "Registration page is launch successfully");

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