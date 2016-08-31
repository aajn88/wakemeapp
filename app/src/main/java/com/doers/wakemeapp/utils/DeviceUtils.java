package com.doers.wakemeapp.utils;

import android.os.Build;

/**
 * Utility class to offer and common device methods
 *
 * @author <a href="mailto:aajn88@gmail.com">Antonio A. Jimenez N.</a>
 */
public final class DeviceUtils {

  /**
   * Private constructor to avoid instances
   */
  private DeviceUtils() {
  }

  /**
   * This method indicates if the current device has Lollipop or greater
   *
   * @return True if the current OS is Lollipop or greater
   */
  public static boolean isLollipopOrGreater() {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
  }

}
