package com.doers.wakemeapp.persistence.managers.api;

import com.doers.wakemeapp.common.model.alarms.Alarm;

import java.util.List;

/**
 * Exposed queries for alarms' manager
 *
 * @author <a href="mailto:aajn88@gmail.com">Antonio Jimenez</a>
 */
public interface IAlarmsManager extends ICrudManager<Alarm, Integer> {

  /**
   * This method finds the alarms that are set with the given playlist ID
   *
   * @param playlistId
   *         Playlist ID to be found
   *
   * @return List of {@link Alarm}s that has been set up with the requested playlist ID
   */
  List<Alarm> findByPlaylistId(int playlistId);
}
