package gr.uoa.di.madgik.datatransformation.harvester.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

public class GetCurrentDateTimestamp {

	public static Date getCurrentDateTimestamp() {
	    DateTime nowUTC = new DateTime(DateTimeZone.UTC);
	    return nowUTC.toLocalDateTime().toDate();
	}
	
	public static boolean isMoreThanTenMinutes(Date old) {
		long t1 = old.getTime();
		long t2 = GetCurrentDateTimestamp.getCurrentDateTimestamp().getTime();
		
		long diff = t2 - t1;
		long diffMinutes = diff / (60 * 1000) % 60; 
		if (diffMinutes>10) return true;
		else return false;
	}
	
	/** 
	 * granularity will have one of the above forms 
	 * "YYYY-MM-DD"
	 * "YYYY-MM-DDThh:mm:ssZ"
	 **/
	public static String convertTo(Date date, String granularity) {
		String toReturn = null;
		SimpleDateFormat simpleDateFormat = null;
		if (granularity.toLowerCase().equals("YYYY-MM-DDThh:mm:ssZ".toLowerCase()) || 
			granularity.toLowerCase().equals("yyyy-MM-dd'T'hh:mm:ss".toLowerCase()) || 	
			granularity.toLowerCase().equals("YYYY-MM-DDThh:mm:ss".toLowerCase()) ||
			granularity.toLowerCase().equals("YYYY-MM-DD'T'hh:mm:ss".toLowerCase())) {
			
			simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
			simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
			toReturn = (simpleDateFormat.format(date).concat("Z"));
		} else {
			simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
			toReturn = (simpleDateFormat.format(date));
		}
		return toReturn;
	}
	
}
