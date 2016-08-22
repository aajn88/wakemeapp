package com.doers.wakemeapp.business.services.api;

import com.doers.wakemeapp.common.model.alarms.Alarm;

import java.util.List;

/**
 * This is the interface where all alarms services are exposed
 *
 * @author <a href="mailto:aajn88@gmail.com">Antonio Jimenez</a>
 */
public interface IAlarmsService {

  /**
   * This method creates or updates an alarm
   *
   * @param alarm
   *         Alarm to be created or updated
   */
  void createOrUpdateAlarm(Alarm alarm);

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

}
