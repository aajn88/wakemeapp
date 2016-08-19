package com.doers.wakemeapp.persistence.managers.api;

import com.doers.wakemeapp.common.model.audio.Playlist;

/**
 * Exposed queries for playlists' manager
 *
 * @author <a href="mailto:aajn88@gmail.com">Antonio Jimenez</a>
 */
public interface IPlaylistsManager extends ICrudManager<Playlist, Integer> {

  /**
   * This method finds the default playlist
   *
   * @return The default playlist
   */
  Playlist findDefaultPlaylist();

}
