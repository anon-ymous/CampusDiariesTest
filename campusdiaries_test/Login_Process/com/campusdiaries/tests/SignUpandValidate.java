/**
 * 
 */
package com.campusdiaries.tests;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.xml.sax.SAXException;

import com.campusdiaries.setup.MyException;
import com.campusdiaries.setup.Settings;

/**
 * @author Bhumit
 *
 */
public class SignUpandValidate {
	Settings settings = new Settings();
	Properties locator = null;
	WebDriver driver = new FirefoxDriver(settings.selectProfile("CD_test"));
	@BeforeClass(alwaysRun=true)
	public void setup() throws IOException, InterruptedException
	{
		locator=new Properties();
		locator.load(new FileInputStream(System.getProperty("user.dir") + "\\inputs\\objectRepo.properties"));
	}
	
	
	@Test(description="Signing up to Campus Diaries",priority=8)
	public void signUpCD() throws InterruptedException, IOException, ParserConfigurationException, SAXException, MyException {
			
		driver.get(locator.getProperty("URL"));
		settings.debugLogging("Navigated to "+locator.getProperty("URL"), "Info");
		
		settings.clickButtonxPath(driver, locator.getProperty("SignUP"));
		//obj.debugLogging("Clicked on: "+"", "Info");
		driver.switchTo().activeElement();
		
		settings.fillValue(driver, "MailID", "CDMailID");
		
		//driver.findElement(By.xpath(".//*[@id='edit-name']")).clear();
		//driver.findElement(By.xpath(".//*[@id='edit-name']")).sendKeys("mail@mail.com");
		
		settings.fillValue(driver, "Passwd", "CDPwd");
		
		//driver.findElement(By.xpath(".//*[@id='edit-pass']")).clear();
		//driver.findElement(By.xpath(".//*[@id='edit-pass']")).sendKeys("pass@123");
		
		settings.clickButtonxPath(driver, settings.readFromFile("Register"));
		settings.debugLogging("Clicked register: "+"", "Info");

	}


	@Test(description="Editing VM to given Location and Host",priority=9)
	public void validateEmail() throws InterruptedException, IOException, MyException{
		WebDriver drivermail = new FirefoxDriver(settings.selectProfile("CD_test"));
		drivermail.get(locator.getProperty("GMail"));
		//obj.clickButtonxPath(drivermail, ".//*[@id='gmail-sign-in']");
		settings.fillValue(drivermail, "GmailID", "GMailID");
		//drivermail.findElement(By.xpath(".//*[@id='Email']")).clear();
		//drivermail.findElement(By.xpath(".//*[@id='Email']")).sendKeys("f2012116@hyderabad.bits-pilani.ac.in");
		//obj.clickButtonxPath(drivermail, ".//*[@id='Email']");
		settings.clickButtonxPath(drivermail, locator.getProperty("Next"));
		Thread.sleep(900);
		settings.fillValue(drivermail, "GmailPwd", "GmailPwd");
		//drivermail.findElement(By.xpath(locator.getProperty("GmailPwd"))).clear();
		//drivermail.findElement(By.xpath(locator.getProperty("GmailPwd"))).sendKeys(obj.readFromFile("GmailPwd"));
		//obj.clickButtonxPath(drivermail, ".//*[@id='Email']");
		settings.clickButtonxPath(drivermail, locator.getProperty("SignIn"));
		Thread.sleep(6500);
		settings.findMailAndClick(drivermail, "Campus Diaries", "Verify Your Email");
		settings.debugLogging("Clicked:", "Info");
		
		Thread.sleep(4500);
		
		drivermail.findElement(By.linkText("Click Here")).click();
		
		Thread.sleep(8500);
		String handle = drivermail.getWindowHandle(); 

		for (String winHandle : drivermail.getWindowHandles()) {
			settings.debugLogging(winHandle, "Info");
			if(!handle.equals(winHandle))
				drivermail.switchTo().window(winHandle); 
		}
		Thread.sleep(9000);
		settings.debugLogging("Signed Up.. ", "Info");
		drivermail.quit();
		settings.debugLogging("Quitting .. ", "Info");
	}
	@AfterMethod
	public void Screenshots(ITestResult result) throws IOException, InterruptedException{
		if(!result.isSuccess()){
			settings.takeScreenShotForDriver(driver);
		}
	}

}
