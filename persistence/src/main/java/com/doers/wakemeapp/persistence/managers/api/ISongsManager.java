package com.doers.wakemeapp.persistence.managers.api;

import com.doers.wakemeapp.common.model.audio.Song;

import java.util.List;

/**
 * This is the songs manager that offers CRUD and query methods for entity management
 *
 * @author <a href="mailto:aajn88@gmail.com">Antonio Jimenez</a>
 */
public interface ISongsManager extends ICrudManager<Song, Integer> {

  /**
   * This method finds the playlist's songs given its ID
   *
   * @param playlistId
   *         Requested playlist ID
   *
   * @return List of songs that match with the playlist ID
   */
  List<Song> findByPlaylistId(int playlistId);

}
