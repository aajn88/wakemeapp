package com.doers.wakemeapp.business.services.api;

import android.os.Bundle;

import com.doers.wakemeapp.business.services.constants.FirebaseEvent;
import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * This is the Firebase Analytics exposed services.
 *
 * @author <a href="mailto:aajn88@gmail.com">Antonio Jimenez</a>
 */
public interface IFirebaseAnalyticsService {

  /**
   * This method logs to firebase a given event
   *
   * @param event
   *         Event to be logged
   */
  void logEvent(FirebaseEvent event);

  /**
   * This method logs to firebase a given event
   *
   * @param event
   *         Event to be logged
   * @param extras
   *         Extras to be sent. Possible extras can be found in {@link Extra}
   */
  void logEvent(FirebaseEvent event, Bundle extras);

  /**
   * These are the possible extras for log events
   */
  class Extra extends FirebaseAnalytics.Param {

    /** Number of items **/
    public static final String NUMBER_OF_ITEMS = "number_of_items";
  }

}
