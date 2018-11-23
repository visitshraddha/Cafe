/*
 *This test is for Verify Maximum interest rate of bank and Register for it.
 *
 *Created By: Shraddha Jamdade
 *Date: 22-Nov-2018
 *  
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


public class RegisterOffer extends BaseTest {
	public String TC = "RegisterOffer_TC";
	public Xls_Reader xls = null;

	//Soft Asserts are declared
	@BeforeMethod
	public void setup() {
		s_assert = new SoftAssert();
	}

	@Test(dataProvider = "tcdata")
	public void RegisterOffer(Hashtable<String, String> ht) 
	{
//load all locators from config.properties file
		loadconfig();
		report = ExtentManager.getInstance();
		test = report.startTest("RegisterOffer_TC" + ht.toString());

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
		//Click on Ok button over pop up
		click("Ok_xpath");
		dynamicwait(30, "Offers_xpath");
		takescreenshot(test);
		
		//Verify that Offer page is displayed
		s_assert.assertTrue(getelemnt("Offers_xpath").isDisplayed());
		test.log(LogStatus.PASS, "Offers page is launch successfully");
		//Select Easy Access checkbox
		selectchkbox("EasyAccess_xpath");
		dynamicwait(30, "EasyAccess_xpath");
		takescreenshot(test);
		//Assert Easy checkbox is selected
		s_assert.assertTrue(getelemnt("EasyAccess_xpath").isSelected());
		test.log(LogStatus.PASS, "Easy Access Checkbox on Offers page is selected successfully");

		//Verify Message displayed post selection of Easy Access Checkbox on Offer page
		dynamicwait(30, "Msg_xpath");
		String SearchResult=gettext("Msg_xpath");
		s_assert.assertEquals(SearchResult,"10 offers match your search");
		takescreenshot(test);
		test.log(LogStatus.PASS,SearchResult + " is displayed successfully");
		int MsgResult=Integer.parseInt(SearchResult.replaceAll("\\D", ""));
		
		((JavascriptExecutor) driver).executeScript("scroll(1000,1000)");
		//Click on Show more button
		click("Showbtn_xpath");
	
		((JavascriptExecutor) driver).executeScript("scroll(0,0)");
		driver.manage().timeouts().implicitlyWait(150, TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(150, TimeUnit.SECONDS);

		//This sector is for verify maximum interest rate from list of offers
		int cnt=0;
		By css=By.cssSelector(".prot-offer-header-interest-value.ng-binding");
		List<WebElement>allRates=driver.findElements(css);
		double max=Double.parseDouble(allRates.get(0).getAttribute("textContent"));
		for (int i = 0; i < allRates.size(); i++) {
			double tmp=Double.parseDouble(allRates.get(i).getAttribute("textContent"));

			if(tmp >= max)
			{
				max=tmp;
				cnt=i;

			}		 

		}
		takescreenshot(test);
		test.log(LogStatus.PASS, "Maximum interest provided by bank is " +max);
		//Click on Register button of max interest rate
		driver.findElement(By.cssSelector(".product-cta.ng-scope span:nth-of-type(1)")).click();

		dynamicwait(80, "Registerpge_xpath");
		takescreenshot(test);
		//Assert Registration page is displayed
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