package com.doers.wakemeapp.business.services.impl;

import android.content.Context;

import com.doers.wakemeapp.business.R;
import com.doers.wakemeapp.business.services.api.IAlarmsService;
import com.doers.wakemeapp.business.services.api.IPlaylistsService;
import com.doers.wakemeapp.common.model.alarms.Alarm;
import com.doers.wakemeapp.persistence.managers.api.IAlarmsManager;

import org.apache.commons.lang3.Validate;

import java.util.Calendar;
import java.util.List;

/**
 * Implementation of the exposed alarms services in {@link IAlarmsService}
 *
 * @author <a href="mailto:aajn88@gmail.com">Antonio Jimenez</a>
 */
public class AlarmsService implements IAlarmsService {

  /** Week days count **/
  private static final int WEEK_DAYS_COUNT = 7;

  /** Alarms Manager **/
  private final IAlarmsManager mAlarmsManager;

  /** Playlists service **/
  private final IPlaylistsService mPlaylistsService;

  /** Application context **/
  private final Context mContext;

  /**
   * Alarms Service constructor
   *
   * @param context
   *         App context
   * @param alarmsManager
   *         Alarms manager
   * @param playlistsService
   *         Playlist service
   */
  public AlarmsService(Context context, IAlarmsManager alarmsManager,
                       IPlaylistsService playlistsService) {
    mContext = context;
    mAlarmsManager = alarmsManager;
    mPlaylistsService = playlistsService;
  }

  /**
   * This method creates or updates an alarm
   *
   * @param alarm
   *         Alarm to be created or updated
   */
  @Override
  public void createOrUpdateAlarm(Alarm alarm) {
    validateFields(alarm);
    mAlarmsManager.createOrUpdate(alarm);
  }

  /**
   * This method validates all the fields in the alarm
   *
   * @param alarm
   *         Alarm to be checked
   */
  private void validateFields(Alarm alarm) {
    Validate.notNull(alarm, "The alarm cannot be null");
    Validate.notNull(alarm.getName(), "Alarm name cannot be null");
    Validate.notNull(alarm.getHour(), "Alarm's hour is required");
    Validate.notNull(alarm.getMinute(), "Alarm's minute is required");
    Validate.notNull(alarm.getPlaylist(), "Alarm's playlist cannot be null");
  }

  /**
   * This method gets all the stored alarms
   *
   * @return All stored alarms
   */
  @Override
  public List<Alarm> getAllAlarms() {
    return mAlarmsManager.all();
  }

  /**
   * This method creates a default alarm instance
   *
   * @return Default alarm
   */
  @Override
  public Alarm getDefaultAlarm() {
    Alarm defaultAlarm = new Alarm();
    defaultAlarm.setScheduledDays(getDefaultDays());

    Calendar now = Calendar.getInstance();
    defaultAlarm.setHour(now.get(Calendar.HOUR_OF_DAY));
    defaultAlarm.setMinute(now.get(Calendar.MINUTE));
    defaultAlarm.setName(mContext.getString(R.string.playlist_default_title));
    defaultAlarm.setPlaylist(mPlaylistsService.getDefaultPlaylist());

    return defaultAlarm;
  }

  /**
   * This method gets the default scheduled days
   *
   * @return Default scheduled days
   */
  private boolean[] getDefaultDays() {
    boolean[] scheduledDates = new boolean[WEEK_DAYS_COUNT];
    for (int i = 0; i < WEEK_DAYS_COUNT; i++) {
      scheduledDates[i] = true;
    }
    return scheduledDates;
  }
}
