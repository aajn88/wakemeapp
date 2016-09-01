package com.doers.wakemeapp.persistence.managers.impl;

import android.util.Log;

import com.doers.wakemeapp.common.model.alarms.Alarm;
import com.doers.wakemeapp.persistence.DatabaseHelper;
import com.doers.wakemeapp.persistence.managers.api.IAlarmsManager;

import java.sql.SQLException;
import java.util.List;

/**
 * Implementation of the alarms' queries
 *
 * @author <a href="mailto:aajn88@gmail.com">Antonio Jimenez</a>
 */
public class AlarmsManager extends CrudManager<Alarm, Integer> implements IAlarmsManager {

  /** Tag for logs **/
  private static final String TAG = AlarmsManager.class.getSimpleName();

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

  @Override
  public List<Alarm> findByPlaylistId(int playlistId) {
    List<Alarm> alarms = null;
    try {
      alarms = getDao().queryBuilder().where().eq(Alarm.PLAYLIST_ID, playlistId).query();
    } catch (SQLException e) {
      Log.e(TAG, "An error occurred while finding alarms with playlistId = " + playlistId, e);
    }
    return alarms;
  }
}
