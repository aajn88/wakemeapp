package com.doers.wakemeapp.common.utils;

/**
 * Utility class to offer standard and common environmental methods
 *
 * @author <a href="mailto:aajn88@gmail.com">Antonio Jimenez</a>
 */
public class EnvironmentUtils {

  /** This variable indicates if the current environment is in debug **/
  private static boolean mIsDebug;

  /** Private constructor to avoid instances **/
  private EnvironmentUtils() {
  }

  /**
   * This method establishes the current environment status for debug
   *
   * @param isDebug
   *         Indicates whether the current environment is in debug or not
   */
  public static void setIsDebug(boolean isDebug) {
    mIsDebug = isDebug;
  }

  /**
   * This method indicates whether the current environment is in debug
   *
   * @return True if the current environment is in debug. Otherwise returns False
   */
  public static boolean isDebug() {
    return mIsDebug;
  }

}
