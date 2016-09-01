package com.doers.wakemeapp.business.services.impl;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.doers.wakemeapp.business.R;
import com.doers.wakemeapp.business.services.api.IAlarmsService;
import com.doers.wakemeapp.business.services.api.IPlaylistsService;
import com.doers.wakemeapp.common.model.alarms.Alarm;
import com.doers.wakemeapp.common.model.audio.Playlist;
import com.doers.wakemeapp.common.utils.DateUtils;
import com.doers.wakemeapp.persistence.managers.api.IAlarmsManager;

import org.apache.commons.lang3.Validate;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Implementation of the exposed alarms services in {@link IAlarmsService}
 *
 * @author <a href="mailto:aajn88@gmail.com">Antonio Jimenez</a>
 */
public class AlarmsService implements IAlarmsService {

  /** Tag for logs **/
  private static final String TAG = AlarmsService.class.getSimpleName();

  /** The default time for snoozing alarms (in millis) **/
  private static final int DEFAULT_SNOOZE_TIME = 5 * 60 * 1000;

  /** Alarm wake up **/
  private static final String ALARM_WAKE_UP = "com.doers.wakemeapp.ALARM_WAKE_UP";

  /** ID threshold **/
  private static final int ID_THRESHOLD = 10;

  /** Alarm threshold in seconds to activate alarm before it occurs **/
  private static final int THRESHOLD_MILLIS = 0;

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
  public void setUpAlarm(Alarm alarm) {
    validateFields(alarm);
    mAlarmsManager.createOrUpdate(alarm);
    setUpAlarmLaunch(alarm);
  }

  @Override
  public Alarm findAlarmById(int id) {
    Alarm alarm = mAlarmsManager.findById(id);
    if (alarm != null && alarm.getPlaylist() != null) {
      alarm.setPlaylist(mPlaylistsService.findPlaylistById(alarm.getPlaylist().getId()));
    }
    return alarm;
  }

  /**
   * This method sets up the alarm times, in that way, the device will start at the expecting times
   * of the user
   *
   * @param alarm
   *         Alarm to be set up
   */
  private void setUpAlarmLaunch(Alarm alarm) {
    boolean isEnable = alarm.getEnable();
    boolean[] scheduledDays = alarm.getScheduledDays();
    for (int i = 0; i < scheduledDays.length; i++) {
      scheduleAlarm(alarm, i, isEnable && scheduledDays[i]);
    }
  }

  /**
   * This method schedules or stops an alarm given its day
   *
   * @param alarm
   *         Alarm to be scheduled
   * @param day
   *         Day to be scheduled [0-7)
   * @param enable
   *         If the given alarm must to be enable or disable
   */
  private void scheduleAlarm(Alarm alarm, int day, boolean enable) {
    AlarmManager am = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
    int alarmId = alarm.getId() * ID_THRESHOLD + day;
    long nextAlarmMillis = calculateMillisForNextAlarm(alarm, day);
    Intent alarmIntent = new Intent(ALARM_WAKE_UP);
    alarmIntent.putExtra(ALARM_ID, alarm.getId());
    alarmIntent.putExtra(ALARM_DAY, day);
    PendingIntent pi = PendingIntent.getBroadcast(mContext, alarmId, alarmIntent,
            PendingIntent.FLAG_UPDATE_CURRENT);
    String action;
    if (enable) {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        am.setWindow(AlarmManager.RTC_WAKEUP, nextAlarmMillis - THRESHOLD_MILLIS, 0, pi);
      } else {
        am.set(AlarmManager.RTC_WAKEUP, nextAlarmMillis - THRESHOLD_MILLIS, pi);
      }
      action = "scheduled";
    } else {
      am.cancel(pi);
      action = "cancelled";
    }

    Log.d(TAG, "Alarm " + action + " for day: " + day + " at " +
            DateUtils.format(new Date(nextAlarmMillis), DateUtils.DEFAULT_FORMAT));

  }

  /**
   * This method calculates the remaining millis for the next time the alarm should be triggered
   *
   * @param alarm
   *         Alarm
   * @param day
   *         Expected day
   *
   * @return Remaining millis for the requested day and time
   */
  private long calculateMillisForNextAlarm(Alarm alarm, int day) {
    Calendar now = Calendar.getInstance();
    Calendar expectedTime = Calendar.getInstance();
    expectedTime.set(Calendar.HOUR_OF_DAY, alarm.getHour());
    expectedTime.set(Calendar.MINUTE, alarm.getMinute());
    expectedTime.set(Calendar.SECOND, 0);
    expectedTime.set(Calendar.MILLISECOND, 0);
    int curDay = now.get(Calendar.DAY_OF_WEEK) - 2;
    curDay = curDay < 0 ? WEEK_DAYS_COUNT - 1 : curDay;
    int nextDays = day - curDay;
    nextDays = nextDays < 0 ? nextDays + WEEK_DAYS_COUNT : nextDays;
    if (nextDays == 0 && expectedTime.compareTo(now) < 0) {
      nextDays += WEEK_DAYS_COUNT;
    }
    expectedTime.add(Calendar.DAY_OF_YEAR, nextDays);
    return expectedTime.getTimeInMillis();
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

  @Override
  public Alarm getNewAlarm() {
    Alarm newAlarm = getDefaultAlarm();
    setUpAlarm(newAlarm);
    return newAlarm;
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
    defaultAlarm.setEnable(true);

    return defaultAlarm;
  }

  @Override
  public boolean deletePlaylist(int playlistId) {
    Playlist playlist = mPlaylistsService.findPlaylistById(playlistId);
    if (playlist == null) {
      // The playlist has been already deleted or not exists
      return true;
    }
    if (playlist.isDefault()) {
      return false;
    }
    Playlist defaultPlaylist = mPlaylistsService.getDefaultPlaylist();
    List<Alarm> alarms = mAlarmsManager.findByPlaylistId(playlistId);
    for (Alarm alarm : alarms) {
      alarm.setPlaylist(defaultPlaylist);
      setUpAlarm(alarm);
    }
    mPlaylistsService.deletePlaylist(playlistId);
    return true;
  }

  @Override
  public void snoozeAlarm(int alarmId, int day) {
    Alarm alarm = findAlarmById(alarmId);
    if (alarm == null) {
      return;
    }

    int requestCode = alarmId * THRESHOLD_MILLIS + 1;
    AlarmManager am = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
    Intent alarmIntent = new Intent(ALARM_WAKE_UP);
    alarmIntent.putExtra(ALARM_ID, alarm.getId());
    alarmIntent.putExtra(ALARM_DAY, day);
    PendingIntent pi = PendingIntent.getBroadcast(mContext, requestCode, alarmIntent,
            PendingIntent.FLAG_UPDATE_CURRENT);
    Calendar now = Calendar.getInstance();
    now.set(Calendar.SECOND, 0);
    long nextAlarmMillis = now.getTimeInMillis() + DEFAULT_SNOOZE_TIME;
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
      am.setWindow(AlarmManager.RTC_WAKEUP, nextAlarmMillis - THRESHOLD_MILLIS, 0, pi);
    } else {
      am.set(AlarmManager.RTC_WAKEUP, nextAlarmMillis - THRESHOLD_MILLIS, pi);
    }

    Log.d(TAG, "Alarm snoozed for day: " + day + " at " +
            DateUtils.format(new Date(nextAlarmMillis), DateUtils.DEFAULT_FORMAT));
  }

  @Override
  public void deleteAlarm(int alarmId) {
    Alarm alarm = findAlarmById(alarmId);
    if (alarm == null) {
      return;
    }

    alarm.setEnable(false);
    setUpAlarm(alarm);
    mAlarmsManager.deleteById(alarmId);
    Log.d(TAG, "The alarm " + alarmId + " has been deleted");
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
