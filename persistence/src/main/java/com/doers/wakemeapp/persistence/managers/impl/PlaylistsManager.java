package com.doers.wakemeapp.persistence.managers.impl;

import com.doers.wakemeapp.common.model.alarms.Playlist;
import com.doers.wakemeapp.persistence.DatabaseHelper;
import com.doers.wakemeapp.persistence.managers.api.IPlaylistsManager;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.sql.SQLException;

/**
 * Implementation of the playlists' queries
 *
 * @author <a href="mailto:aajn88@gmail.com">Antonio Jimenez</a>
 */
@Singleton
public class PlaylistsManager extends CrudManager<Playlist, Integer> implements IPlaylistsManager {

    /**
     * This is the main constructor of the CrudManager
     *
     * @param helper
     *         The DBHelper
     *
     * @throws SQLException
     *         If there's an error creating the Entity's DAO
     */
    @Inject
    public PlaylistsManager(DatabaseHelper helper) throws SQLException {
        super(helper, Playlist.class);
    }
}
