package com.doers.wakemeapp.persistence.managers.impl;

import com.doers.wakemeapp.common.model.alarms.Alarm;
import com.doers.wakemeapp.persistence.DatabaseHelper;
import com.doers.wakemeapp.persistence.managers.api.IAlarmsManager;

import java.sql.SQLException;

/**
 * Implementation of the alarms' queries
 *
 * @author <a href="mailto:aajn88@gmail.com">Antonio Jimenez</a>
 */
public class AlarmsManager extends CrudManager<Alarm, Integer> implements IAlarmsManager {

    /**
     * This is the main constructor of the CrudManager
     *
     * @param helper
     *         The DBHelper
     *
     * @throws SQLException
     *         If there's an error creating the Entity's DAO
     */
    public AlarmsManager(DatabaseHelper helper) throws SQLException {
        super(helper, Alarm.class);
    }
}