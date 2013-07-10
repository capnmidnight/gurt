package gurt.util;

import java.awt.Window;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.JOptionPane;

/**
 * A simple logging class. Extracts the useful information from an exception
 * instance and tags it with a time stamp. Also, counts the total number of
 * errors.
 * 
 * @author Owner
 */
public class ErrorHandler {
	private static Calendar calender = GregorianCalendar.getInstance();

	private static int errorCount = 0;

	/**
	 * Show error information on the console window. Unrolls the causes of the
	 * thrown exception. Adds a time stamp to the message.
	 * 
	 * @param e -
	 *            the exception that caused the error
	 * @param msg -
	 *            a message to accompany the exception
	 */
	public static void printError(Exception e, String msg) {
		System.out.println(makeMessage(e, msg));
	}

	public static void showErrorDialog(Exception e, String msg, Window parent) {
		JOptionPane.showMessageDialog(parent, makeMessage(e, msg));
	}
	private static String makeMessage(Exception e, String msg) {
		errorCount++;
		Date now = calender.getTime();
		StringBuilder out = new StringBuilder();
		out.append(errorCount);
		out.append(".) [");
		out.append(now.toString());
		out.append("] ");
		out.append(msg);
		out.append("\n");
		Throwable temp = e;
		while (temp != null) {
			out.append("\t");
			out.append(temp.getMessage());
			out.append("\n");
			temp = temp.getCause();
		}
		return out.toString();
	}

}
