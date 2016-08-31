package com.doers.wakemeapp.controllers.alarms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.doers.wakemeapp.business.services.api.IAlarmsService;
import com.doers.wakemeapp.common.model.alarms.Alarm;
import com.doers.wakemeapp.common.utils.DateUtils;
import com.doers.wakemeapp.di.WakeMeAppApplication;

import java.util.Calendar;

import javax.inject.Inject;

/**
 * Alarm broadcast receiver to init the alarm
 *
 * @author <a href="mailto:aajn88@gmail.com">Antonio Jimenez</a>
 */
public class AlarmReceiver extends BroadcastReceiver {

  /** Tag for logs **/
  private static final String TAG = AlarmReceiver.class.getSimpleName();

  @Inject
  IAlarmsService mAlarmsService;

  /** Default constructor **/
  public AlarmReceiver() {
  }

  @Override
  public void onReceive(Context context, Intent intent) {
    Log.i(TAG, "Alarm has been launched");
    int alarmId = intent.getIntExtra(IAlarmsService.ALARM_ID, -1);
    final int alarmDay = intent.getIntExtra(IAlarmsService.ALARM_DAY, -1);
    if (alarmId == -1 || alarmDay == -1) {
      Log.w(TAG, "Alarm data has not been sent correctly. The alarm couldn't be launched");
      return;
    }
    injectFields((WakeMeAppApplication) context.getApplicationContext());
    Alarm alarm = mAlarmsService.findAlarmById(alarmId);
    if (!isValidAlarm(alarm, alarmDay)) {
      return;
    }

    Calendar now = Calendar.getInstance();
    Log.d(TAG, "Alarm launched at: " + DateUtils.format(now.getTime(), "dd/MM/yyyy HH:mm:ss") +
            " for day: " + alarmDay);

    Intent alarmLaunchIntent = new Intent(context, LaunchAlarmActivity.class);
    alarmLaunchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    alarmLaunchIntent.putExtra(IAlarmsService.ALARM_ID, alarmId);
    alarmLaunchIntent.putExtra(IAlarmsService.ALARM_DAY, alarmDay);
    context.startActivity(alarmLaunchIntent);
  }

  /**
   * This method checks if is a valid alarm. I.e. is activated, and alarm day has not been
   * deactivated
   *
   * @param alarm
   *         Alarm to be checked
   * @param alarmDay
   *         Alarm day to be checked
   *
   * @return True if is a valid alarm. Otherwise returns false
   */
  private boolean isValidAlarm(Alarm alarm, int alarmDay) {
    return alarm != null && alarm.getEnable() && alarm.getScheduledDays()[alarmDay];
  }

  /**
   * This method injects the class fields
   *
   * @param context
   *         Context
   */
  private void injectFields(WakeMeAppApplication context) {
    context.getInjector().inject(this);
  }

}
