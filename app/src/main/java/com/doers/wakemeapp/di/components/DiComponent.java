package com.doers.wakemeapp.di.components;

import android.content.Context;

import com.doers.wakemeapp.controllers.MainActivity;
import com.doers.wakemeapp.controllers.alarms.AlarmManagerActivity;
import com.doers.wakemeapp.controllers.alarms.AlarmReceiver;
import com.doers.wakemeapp.controllers.alarms.AlarmsAdapter;
import com.doers.wakemeapp.controllers.alarms.LaunchAlarmActivity;
import com.doers.wakemeapp.controllers.playlists.AddPlaylistActivity;
import com.doers.wakemeapp.controllers.playlists.PlaylistsManagerActivity;
import com.doers.wakemeapp.di.modules.ManagersModule;
import com.doers.wakemeapp.di.modules.ServicesModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Dependency Injection component where all Activities, fragments and adapters are injected
 *
 * @author <a href="mailto:aajn88@gmail.com">Antonio Jimenez</a>
 */
@Singleton
@Component(modules = {ManagersModule.class, ServicesModule.class})
public interface DiComponent {

  /**
   * Context injection
   *
   * @return Injected context
   */
  Context context();

  // Common Activities
  void inject(MainActivity activity);

  // Alarm Activities
  void inject(AlarmManagerActivity activity);

  void inject(AlarmsAdapter adapter);

  // Playlist Activities
  void inject(AddPlaylistActivity activity);

  void inject(PlaylistsManagerActivity activity);

  void inject(LaunchAlarmActivity launchAlarmActivity);

  // Broadcast receivers
  void inject(AlarmReceiver alarmReceiver);

}
