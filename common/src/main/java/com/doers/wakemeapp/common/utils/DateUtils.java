package com.doers.wakemeapp.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utility class for Date common and standard methods
 *
 * @author <a href="mailto:aajn88@gmail.com">Antonio Jimenez</a>
 */
public final class DateUtils {

  /** Default format date **/
  public static final String DEFAULT_FORMAT = "dd/MM/yyyy HH:mm";

  /** Time format **/
  public static final String TIME_FORMAT = "HH:mm";

  /** Private constructor to avoid instances **/
  private DateUtils() {
  }

  /**
   * This method formats a given Date to a given format
   *
   * @param date
   *         Date to be formatted
   * @param format
   *         Format to be applied
   *
   * @return Resultinf date format
   */
  public static String format(Date date, String format) {
    SimpleDateFormat formatter = new SimpleDateFormat(format);
    return formatter.format(date);
  }

}
