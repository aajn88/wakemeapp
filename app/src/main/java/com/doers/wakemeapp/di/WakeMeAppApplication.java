package com.doers.wakemeapp.di;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.doers.wakemeapp.BuildConfig;
import com.doers.wakemeapp.common.utils.EnvironmentUtils;
import com.doers.wakemeapp.di.components.DaggerDiComponent;
import com.doers.wakemeapp.di.components.DiComponent;
import com.doers.wakemeapp.di.modules.ManagersModule;
import com.doers.wakemeapp.di.modules.ServicesModule;
import com.google.firebase.analytics.FirebaseAnalytics;

import io.fabric.sdk.android.Fabric;

/**
 * This is the WakeMeApp Application where Dependency Injection is set up
 *
 * @author <a href="mailto:aajn88@gmail.com">Antonio Jimenez</a>
 */
public class WakeMeAppApplication extends Application {

  /** Dependency Injection component **/
  private DiComponent component;

  /** {@link FirebaseAnalytics} instance **/
  private FirebaseAnalytics mFirebaseAnalytics;

  @Override
  public void onCreate() {
    super.onCreate();
    EnvironmentUtils.setIsDebug(BuildConfig.DEBUG);
    if (!EnvironmentUtils.isDebug()) {
      Fabric.with(this, new Crashlytics());
    }
    mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
    component = DaggerDiComponent.builder().servicesModule(new ServicesModule(this))
            .managersModule(new ManagersModule(this)).build();
  }

  /**
   * This method returns the Firebase analytics instance
   *
   * @return {@link FirebaseAnalytics} instance
   */
  public FirebaseAnalytics getFirebaseAnalyticsInstance() {
    return mFirebaseAnalytics;
  }

  /**
   * This method returns the Dagger injector
   *
   * @return Dagger injector
   */
  public DiComponent getInjector() {
    return component;
  }

}

