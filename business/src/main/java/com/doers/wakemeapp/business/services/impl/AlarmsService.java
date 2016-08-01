package com.doers.wakemeapp.business.services.impl;

import com.doers.wakemeapp.business.services.api.IAlarmsService;
import com.doers.wakemeapp.common.model.alarms.Alarm;
import com.doers.wakemeapp.persistence.managers.api.IAlarmsManager;

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

    /**
     * Alarms Service constructor
     *
     * @param alarmsManager
     *         Alarms manager
     */
    public AlarmsService(IAlarmsManager alarmsManager) {
        mAlarmsManager = alarmsManager;
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
        // TODO: Set default playlist

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
