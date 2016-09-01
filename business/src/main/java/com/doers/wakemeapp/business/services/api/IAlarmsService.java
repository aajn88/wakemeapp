package com.doers.wakemeapp.business.services.api;

import com.doers.wakemeapp.common.model.alarms.Alarm;

import java.util.List;

/**
 * This is the interface where all alarms services are exposed
 *
 * @author <a href="mailto:aajn88@gmail.com">Antonio Jimenez</a>
 */
public interface IAlarmsService {

  /** Alarm ID extra **/
  String ALARM_ID = "ALARM_ID";

  /** Alarm day ID extra **/
  String ALARM_DAY = "ALARM_DAY";

  /**
   * This method creates or updates an alarm
   *
   * @param alarm
   *         Alarm to be created or updated
   */
  void setUpAlarm(Alarm alarm);

  /**
   * This method finds an alarm
   *
   * @param id
   *         Alarm ID
   *
   * @return Found alarm. If not, null is returned
   */
  Alarm findAlarmById(int id);

  /**
   * This method gets all the stored alarms
   *
   * @return All stored alarms
   */
  List<Alarm> getAllAlarms();

  /**
   * This method creates a default alarm instance and store it in the database
   *
   * @return Created and stored alarm
   */
  Alarm getNewAlarm();

  /**
   * This method creates a default alarm instance
   *
   * @return Default alarm
   */
  Alarm getDefaultAlarm();

  /**
   * This method snoozes the given alarm for the established time
   *
   * @param alarmId
   *         Alarm ID to be snoozed
   * @param day
   *         Snoozed day
   */
  void snoozeAlarm(int alarmId, int day);

  /**
   * This method removes an alarm given its ID
   *
   * @param alarmId
   *         ID of the alarm to be removed
   */
  void deleteAlarm(int alarmId);

}
