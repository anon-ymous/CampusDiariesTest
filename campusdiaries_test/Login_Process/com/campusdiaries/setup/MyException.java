package com.campusdiaries.setup;

import java.util.GregorianCalendar;

public class MyException extends Exception {
	public MyException(String message) {
        super(message);
        GregorianCalendar gcalendar = new GregorianCalendar();
        System.out.println(message+"\n_"+gcalendar.getTimeInMillis());
    }
}
