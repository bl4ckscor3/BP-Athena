package de.tudarmstadt.informatik.ukp.athenakp.crawler;

import static java.lang.Integer.parseInt;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;

/**
 * A class with methods that might be useful across a variety of conferences / scraping tasks
 * currently mostly focused on converting Strings to LocalDates and LocalTimes
 * @author Julian Steitz, Daniel Lehmann
 */
public class CrawlerToolset {

	/**Converts a time in String format to a LocalDate instance
	 * @param timeString time in String format (e.g. 9:00 or 09:00)
	 * @return corresponding LocalTime instance or null if the String was not in the expected format
	 */
	public static LocalTime acl2018ConvertStringToTime(String timeString){
		String[] hoursAndMinutes = timeString.split(":", 2);
		try{
			int startHours = parseInt(hoursAndMinutes[0]);
			int startMinutes = parseInt(hoursAndMinutes[1]);
			return LocalTime.of(startHours, startMinutes);

		}catch (IndexOutOfBoundsException | NumberFormatException e){
			System.err.println("invalid Input, got: "+ timeString
					+ " expected: 9:00 or 09:00");
			return null;
		}
	}

	/**
	 * A method which constructs an Array holding the beginning and end of a conference or an event
	 * @param dateString Date (day - day + months + year) in String format e.g. "15-20 July 2018")
	 * @return an Array of LocalDates with two entries, the beginning and end of the date range or an empty array if
	 * dateString was in the wrong format
	 */
	public static LocalDate[] acl2018ConvertStringToDateRange(String dateString){
		LocalDate[] dateRange = new LocalDate[2];
		try {
			String[] daysMonthsYearAndLocation = dateString.split(" ");
			String daysRange = daysMonthsYearAndLocation[0];
			String[] startAndEndDay = daysRange.split("-", 2);

			dateRange[0] = stringToLocalDate(startAndEndDay[0],
					daysMonthsYearAndLocation[1], daysMonthsYearAndLocation[2]);
			dateRange[1] = stringToLocalDate(startAndEndDay[1],
					daysMonthsYearAndLocation[1], daysMonthsYearAndLocation[2]);
			return dateRange;
		}catch (IndexOutOfBoundsException e){
			System.err.println("invalid format. expected dateString to be 15-20 July 2018 and got: " + dateString);
			return dateRange;
		}

	}

	/**
	 * A method which converts day,month and year strings into a LocalDate
	 * @param dayDescription day as String "11"
	 * @param monthDescription month as String "July"
	 * @param yearDescription year as String "2018"
	 * @return LocalDate corresponding to the parameters
	 */
	public static LocalDate stringToLocalDate(String dayDescription, String monthDescription, String yearDescription){
		int day = parseInt(dayDescription);
		int year = parseInt(yearDescription);
		Month monthObject = Month.valueOf(monthDescription.toUpperCase());
		return LocalDate.of(year, monthObject,day);
	}

	/**
	 * Gets the number of the given month (e.g. April would be 4)
	 * @param month The month in english
	 * @return The month's index, -1 if the input is not a month
	 */
	public static int getMonthIndex(String month) {
		String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

		for(int i = 0; i < months.length; i++) {
			if(months[i].equalsIgnoreCase(month))
				return i + 1;
		}

		return -1;
	}

	/**
	 * Temporarily stores information about a paper
	 * title: The complete title of the paper
	 * year: The year the paper was released in
	 * month: The month the paper was released in
	 */
	public static class PaperStore
	{
		public String title, year, month;

		@Override
		public String toString()
		{
			return title + ";;" + year + ";;" + month;
		}
	}
}