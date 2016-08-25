package com.doers.wakemeapp.di;

import android.app.Application;

import com.doers.wakemeapp.di.components.DaggerDiComponent;
import com.doers.wakemeapp.di.components.DiComponent;
import com.doers.wakemeapp.di.modules.ManagersModule;
import com.doers.wakemeapp.di.modules.ServicesModule;

/**
 * This is the WakeMeApp Application where Dependency Injection is set up
 *
 * @author <a href="mailto:aajn88@gmail.com">Antonio Jimenez</a>
 */
public class WakeMeAppApplication extends Application {

  /** Dependency Injection component **/
  DiComponent component;

  @Override
  public void onCreate() {
    super.onCreate();
    component = DaggerDiComponent.builder().servicesModule(new ServicesModule(this))
            .managersModule(new ManagersModule(this)).build();
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

