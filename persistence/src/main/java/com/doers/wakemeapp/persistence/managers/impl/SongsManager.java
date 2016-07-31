package com.doers.wakemeapp.persistence.managers.impl;

import android.util.Log;

import com.doers.wakemeapp.common.model.audio.Song;
import com.doers.wakemeapp.persistence.DatabaseHelper;
import com.doers.wakemeapp.persistence.managers.api.ISongsManager;

import java.sql.SQLException;
import java.util.List;

/**
 * Implementation of the Songs Manager
 *
 * @author <a href="mailto:aajn88@gmail.com">Antonio Jimenez</a>
 */
public class SongsManager extends CrudManager<Song, Integer> implements ISongsManager {

    /** Tag for logs **/
    private static final String TAG = SongsManager.class.getName();

    /**
     * This is the main constructor of the CrudManager
     *
     * @param helper
     *         The DBHelper
     *
     * @throws SQLException
     *         If there's an error creating the Entity's DAO
     */
    public SongsManager(DatabaseHelper helper) throws SQLException {
        super(helper, Song.class);
    }

    /**
     * This method finds the playlist's songs given its ID
     *
     * @param playlistId
     *         Requested playlist ID
     *
     * @return List of songs that match with the playlist ID
     */
    @Override
    public List<Song> findByPlaylistId(int playlistId) {
        List<Song> songs = null;
        try {
            songs = getDao().queryBuilder().where().eq(Song.PLAYLIST_ID, playlistId).query();
        } catch (SQLException e) {
            Log.e(TAG, String.format(
                    "An error occurred while finding all the songs by playlist ID: %s", playlistId),
                    e);
        }
        return songs;
    }
}
