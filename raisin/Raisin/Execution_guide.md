Framework used Hybrid POM framework.

How to execute:

* Go to testng.xml under Raisin project, right click into it and Run as TestNGsuite.

Reports
* Execution result with latest date & timestamp will be generated under Project workspace raisin-> report folder. 
 
 
Tests:
basetest pacakge:
BaseTest:
BaseTest class contains all common keywords which is extended in all other classes.

XlsReader:
This class is copied imported into package inorder to work on xls common methods.

# Util package:
DataReader:
This class is having methods to iterate with TestData.xls which is use for passing testdata into test.
In this class getcellData is used which is from xlsReader class.

ExtentManager:
This class singleton object is created for Extentreports object.
The name of report is intialize with date & timestamp in this class.

#TestData:
TestData folder contains excelsheet which used for test data for all 3 scenarios.
Suite sheet contains all test scenarios.
TestData sheet contains all 3 tests with prerequisite test data.

#config.properties 
This file holds all locator details used in entire test.

#Challenge package:

EasyAccessOffers:
In this class, on Offers page, verified selection of EasyAccess checkbox message with total search result of bank offers 

RegisterOffer:
In this class, on Offers page, verified high interest rate bank  and clicked on Register for registration.  

CheckOutBank:
In this class, on Bank page, verified bank with Mood's Rating A1 & clicked on Learn More and clicked on Invest Now for registration.  




 

