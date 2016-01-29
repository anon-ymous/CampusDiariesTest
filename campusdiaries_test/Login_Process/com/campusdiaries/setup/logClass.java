package com.campusdiaries.setup;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

public class logClass {

	private static Logger Log = Logger.getLogger(Logger.class.getName());//

	public static void confFile(){
		DOMConfigurator.configure("log4j.xml");
	}

	public static void startTestCase(String sTestCaseName){
		Log.info("*************************Started Testing******************************");
		Log.info("                             "+sTestCaseName+ "                            ");
	}

	public static void endTestCase(String sTestCaseName){
		Log.info("*************************End of test case" +sTestCaseName +"******************************\n\n");
	}

	public static void info(String message) {
		Log.info(message);
	}

	public static void warn(String message) {
		Log.warn(message);
	}

	public static void error(String message) {
		Log.error(message);
	}

	public static void fatal(String message) {
		Log.fatal(message);
	}

	public static void debug(String message) {
		Log.debug(message);
	}


}
