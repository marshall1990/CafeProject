package com.marshall.cafeproject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import android.util.Log;

public final class DateConverter {

	private static SimpleDateFormat dateFormat;

	public static Date stringToDate(String stringDate) {
		Date date = new Date();
		setDateFormat();

		try {
			date = dateFormat.parse(stringDate);
		} catch (Exception e) {
			Log.e("DateConverter stringToDate()", e.toString());
		}

		return date;
	}

	public static String dateToString(Date date) {
		setDateFormat();

		return dateFormat.format(date);
	}

	private static void setDateFormat() {
		dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ENGLISH);
	}

	public static boolean isPossibleReservation(String reservationDate) {
		Date now = new Date();
		try {
			now = dateFormat.parse(dateToString(now));
		} catch (ParseException e) {
			Log.e("DateConverter isPossibleReservation()", e.toString());
		}

		Date reservation = stringToDate(reservationDate);

		long secs = (now.getTime() - reservation.getTime()) / 1000;
		int hours = (int) (secs / 3600);

		if (hours < 24)
			return true;
		else
			return false;
	}
}
