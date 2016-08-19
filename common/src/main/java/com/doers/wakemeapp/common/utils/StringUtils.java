package com.doers.wakemeapp.common.utils;

/**
 * String Utils class
 *
 * @author <a href="mailto:aajn88@gmail.com">Antonio A. Jimenez N.</a>
 */
public final class StringUtils {

  /** Empty String **/
  public static final String EMPTY_STRING = "";

  /** String null **/
  private static final String STRING_NULL = "null";

  /** Private constructor to avoid instances **/
  private StringUtils() {
  }

  /**
   * This method performs String.format method but every null value will be replaced to "null"
   *
   * @param string
   *         Target String
   * @param params
   *         String parametters
   *
   * @return Formatted String
   */
  public static String format(String string, Object... params) {
    for (int i = 0; i < params.length; i++) {
      if (params[i] == null) {
        params[i] = STRING_NULL;
      }
    }
    return String.format(string, params);
  }
}
