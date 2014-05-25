package p000t040;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class Euler019SunDate {
	
	public static void main(String[] args) {
		Calendar cal = new GregorianCalendar();
		cal.set(Calendar.YEAR, 1901);
		cal.set(Calendar.MONTH, Calendar.JANUARY);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		while(cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY){
			cal.add(Calendar.DAY_OF_YEAR, 1);
		}
		int count = 0;
		while(cal.get(Calendar.YEAR) < 2001){
			if(cal.get(Calendar.DAY_OF_MONTH) == 1){
				count++;
			}
			cal.add(Calendar.DAY_OF_YEAR, 7);
		}
		System.out.println(count);
	}

}
