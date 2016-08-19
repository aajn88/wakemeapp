package com.doers.wakemeapp.persistence.managers.impl;

import android.util.Log;

import com.doers.wakemeapp.common.model.audio.Playlist;
import com.doers.wakemeapp.persistence.DatabaseHelper;
import com.doers.wakemeapp.persistence.managers.api.IPlaylistsManager;

import java.sql.SQLException;

/**
 * Implementation of the playlists' queries
 *
 * @author <a href="mailto:aajn88@gmail.com">Antonio Jimenez</a>
 */
public class PlaylistsManager extends CrudManager<Playlist, Integer> implements IPlaylistsManager {

  /** Tag for logs **/
  private static final String TAG = PlaylistsManager.class.getName();

  /**
   * This is the main constructor of the CrudManager
   *
   * @param helper
   *         The DBHelper
   *
   * @throws SQLException
   *         If there's an error creating the Entity's DAO
   */
  public PlaylistsManager(DatabaseHelper helper) throws SQLException {
    super(helper, Playlist.class);
  }

  @Override
  public Playlist findDefaultPlaylist() {
    Playlist playlist = null;

    try {
      playlist =
              getDao().queryBuilder().where().eq(Playlist.IS_DEFAULT_COLUMN, true).queryForFirst();
    } catch (SQLException e) {
      Log.e(TAG, "An error occurred while finding the default playlist", e);
    }

    return playlist;
  }
}
