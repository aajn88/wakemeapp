package com.doers.wakemeapp.di.modules;

import android.content.Context;

import com.doers.wakemeapp.business.services.api.IAlarmsService;
import com.doers.wakemeapp.business.services.api.IFirebaseAnalyticsService;
import com.doers.wakemeapp.business.services.api.IPlaylistsService;
import com.doers.wakemeapp.business.services.impl.AlarmsService;
import com.doers.wakemeapp.business.services.impl.FirebaseAnalyticsService;
import com.doers.wakemeapp.business.services.impl.PlaylistsService;
import com.doers.wakemeapp.di.WakeMeAppApplication;
import com.doers.wakemeapp.persistence.managers.api.IAlarmsManager;
import com.doers.wakemeapp.persistence.managers.api.IPlaylistsManager;
import com.doers.wakemeapp.persistence.managers.api.ISongsManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author <a href="mailto:aajn88@gmail.com">Antonio Jimenez</a>
 */
@Module
public class ServicesModule {

  /** Context to be injected into dependencies **/
  private final Context mContext;

  /**
   * Services Module constructor
   *
   * @param context
   *         Application context
   */
  public ServicesModule(Context context) {
    mContext = context;
  }

  /**
   * Bind of the {@link IAlarmsService} with its implementation
   *
   * @return Alarms Service implementation
   */
  @Provides
  @Singleton
  public IAlarmsService alarmsService(Context context, IAlarmsManager alarmsManager,
                                      IPlaylistsService playlistsService) {
    return new AlarmsService(context, alarmsManager, playlistsService);
  }

  /**
   * Bind of the {@link IPlaylistsService} with its implementation
   *
   * @param context
   *         Application context
   * @param playlistsManager
   *         Instance of Playlists Manager
   * @param songsManager
   *         Instance of Songs Manager
   *
   * @return Playlists Service implementation
   */
  @Provides
  @Singleton
  public IPlaylistsService playlistsService(Context context, IPlaylistsManager playlistsManager,
                                            ISongsManager songsManager) {
    return new PlaylistsService(context, playlistsManager, songsManager);
  }

  /**
   * Bind of the {@link IFirebaseAnalyticsService} with its implementation
   *
   * @param context
   *         Application context
   *
   * @return {@link IFirebaseAnalyticsService} instance
   */
  @Provides
  @Singleton
  public IFirebaseAnalyticsService firebaseAnalyticsService(Context context) {
    return new FirebaseAnalyticsService(
            ((WakeMeAppApplication) context).getFirebaseAnalyticsInstance());
  }

  /**
   * Injection of the application context
   *
   * @return Application context
   */
  @Provides
  public Context context() {
    return mContext;
  }

}
