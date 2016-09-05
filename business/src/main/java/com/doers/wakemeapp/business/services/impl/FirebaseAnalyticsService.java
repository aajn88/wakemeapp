package com.doers.wakemeapp.business.services.impl;

import android.os.Bundle;

import com.doers.wakemeapp.business.services.api.IFirebaseAnalyticsService;
import com.doers.wakemeapp.business.services.constants.FirebaseEvent;
import com.doers.wakemeapp.common.utils.EnvironmentUtils;
import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * Firebase analytics services
 *
 * @author <a href="mailto:aajn88@gmail.com">Antonio Jimenez</a>
 */
public class FirebaseAnalyticsService implements IFirebaseAnalyticsService {

  /** The firebase analytics instance **/
  private final FirebaseAnalytics mFirebaseAnalytics;

  /**
   * Constructor for firebase instance
   *
   * @param firebaseAnalytics
   *         Firebase analytics instance
   */
  public FirebaseAnalyticsService(FirebaseAnalytics firebaseAnalytics) {
    this.mFirebaseAnalytics = firebaseAnalytics;
  }


  @Override
  public void logEvent(FirebaseEvent event) {
    logEvent(event, null);
  }

  @Override
  public void logEvent(FirebaseEvent event, Bundle extras) {
    if (!isValidEnvironment() || event == null) {
      return;
    }

    mFirebaseAnalytics.logEvent(event.name(), extras == null ? new Bundle() : extras);
  }

  /**
   * This method indicates if the current environment is a valid status
   *
   * @return True if this is a valid environment
   */
  private boolean isValidEnvironment() {
    return mFirebaseAnalytics != null && !EnvironmentUtils.isDebug();
  }

}
