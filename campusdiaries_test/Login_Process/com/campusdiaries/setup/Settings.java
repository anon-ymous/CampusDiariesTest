package com.campusdiaries.setup;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

@SuppressWarnings({"unused"})

public class Settings {

	//Setup properties file to take input IDs from it..
	Properties locator = null;
	public void settings() throws IOException, InterruptedException
	{
		locator = new Properties();
		locator.load(new FileInputStream(System.getProperty("user.dir") + "\\inputs\\ObjectRepo.properties"));
	}

	// Go to SDM Client URL on browser (pass driver instance as parameter)
	public void goToSDMCliURL(WebDriver driver) throws IOException, InterruptedException{
		driver.manage().window().maximize();
		driver.get(locator.getProperty("SDMCliURL"));
		driver.findElement(By.xpath(locator.getProperty("VM-Management"))).click();
		debugLogging("Clicked on VM Management", "Info");
		driver.manage().timeouts().implicitlyWait(11500, TimeUnit.MILLISECONDS);
	}

	//Choose a firefox profile for Tests (Give name of profile as input parameter)
	public FirefoxProfile selectProfile(String profileName){
		ProfilesIni allProfiles = new ProfilesIni();
		FirefoxProfile profil = allProfiles.getProfile(profileName);
		boolean acceptUntrustedSsl = true;
		profil.setAcceptUntrustedCertificates(acceptUntrustedSsl);
		logClass.confFile();
		return profil;
	}

	//Take a screenshot with timestamp in it's name if error is there (pass driver instance as parameter)
	public void takeScreenShotForDriver(WebDriver driver) throws IOException{
		String workingDirectory = System.getProperty("user.dir"); 
		File imageFile=((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		GregorianCalendar gcalendar = new GregorianCalendar();
		String failureImageFileName=workingDirectory+ File.separator+"Screenshots"+File.separator+"Screenshots"+ dateFormat.format(new java.util.Date())+File.separator +gcalendar.get(Calendar.HOUR)+gcalendar.get(Calendar.MINUTE)+"_"+gcalendar.getTimeInMillis()+".png";
		File failureImageFile=new File(failureImageFileName);
		FileUtils.moveFile(imageFile, failureImageFile);
		debugLogging("Somthing went wrong. Check Screenshot.", "Error");
	}

	//Read inputs from file 
	//pass File name and key as parameter
	public String readFromFile(String findProperty) throws IOException{
		Properties pr = new Properties();
		pr.load(new FileInputStream(System.getProperty("user.dir") + "\\inputs\\input.properties"));
		String output = pr.getProperty(findProperty);
		debugLogging("Read from file 'input.properties' for "+findProperty+": "+output, "Info");
		//System.out.println("Read from file "+fileName +" for "+findProperty+": "+output);
		return output;
	}

	// Fill value in text box
	public void fillValue(WebDriver driver,String xPath,String value) throws IOException{
		driver.findElement(By.xpath(locator.getProperty(xPath))).clear();
		driver.findElement(By.xpath(xPath)).sendKeys(readFromFile(value));
	}
	// Click the button by it's xPath
	public void clickButtonxPath(WebDriver driver,String buttonxPath) throws IOException, MyException{
		float opacity = Float.parseFloat(driver.findElement(By.xpath(buttonxPath)).getCssValue("opacity"));
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(buttonxPath)));

		if(opacity==1 && driver.findElement(By.xpath(buttonxPath)).isEnabled()){		
			debugLogging("Opacity of button "+driver.findElement(By.xpath(buttonxPath)).getText().replaceAll("[\r\n]+", " :")+" is: "+opacity, "Info");
			debugLogging("Is element enabled: "+driver.findElement(By.xpath(buttonxPath)).isEnabled(), "Info");
			debugLogging("Clicked on : "+driver.findElement(By.xpath(buttonxPath)).getText().replaceAll("[\r\n]+", " "), "Info");
			driver.findElement(By.xpath(buttonxPath)).click();
		}
		else{
			takeScreenShotForDriver(driver);
			throw new MyException("Check if button is enabled or not..");
		}
	}

	//Find mail and click on it
	//pass driver instance and string which is to be find as parameter
	public void findMailAndClick(WebDriver driver, String input1,String input2) throws IOException, InterruptedException, MyException{
		//setup();
		WebElement table = driver.findElement(By.className("Cp"));
		List<WebElement> cells = table.findElements(By.xpath(".//*[local-name(.)='tr']"));
		//System.out.println(cells.size()+"\n\n");

		if(input2!=null && input1 !=null)
		{
			for(WebElement e : cells)
			{
				//debugLogging(e.getText(), "Info");
				if(e.getText().contains(input1) && e.getText().contains(input2) )
				{
					e.click();
					//debugLogging("Checker:", "Info");
					debugLogging("ID and class: "+e.getAttribute("id")+" "+e.getAttribute("class"), "Info");
				}
			}
		}

		else{
			debugLogging("Please check if mail came or not ..", "Error");
			takeScreenShotForDriver(driver);
			throw new MyException("Error occurred: Either mail hasn't arrived or couldn't able to find mail\n Check Screenshot for the same ..");
		}
	}
	// To print messages in log file as well as on console..
	public void debugLogging(String logMessage,String infoOrError){

		if(infoOrError.equals("Info")){
			logClass.info(logMessage);
			System.out.println(logMessage);
		}
		else if(infoOrError.equals("Error")){
			logClass.error(logMessage);
			System.out.println(logMessage);
		}
		else{
			System.out.println(logMessage);
			System.out.println("Please give a valid input.\nValid input are: 'Info' and 'Error': \n"+logMessage);
		}
	}

}

