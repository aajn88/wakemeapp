package com.doers.wakemeapp.business.services.impl;

import com.doers.wakemeapp.business.services.api.IPlaylistsService;
import com.doers.wakemeapp.common.model.alarms.Playlist;
import com.doers.wakemeapp.persistence.managers.api.IPlaylistsManager;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import org.apache.commons.lang3.Validate;

import java.util.List;

/**
 * Implementation of the {@link IPlaylistsService} exposed methods
 *
 * @author <a href="mailto:aajn88@gmail.com">Antonio Jimenez</a>
 */
@Singleton
public class PlaylistsService implements IPlaylistsService {

    /** Playlists manager **/
    @Inject
    private IPlaylistsManager mPlaylistsManager;

    /**
     * This method creates a playlist given its name and its songs
     *
     * @param name
     *         Playlist name
     * @param songsPath
     *         Selected songs for the playlist
     *
     * @return Created playlist. Returns null if an error occurred
     */
    @Override
    public Playlist createPlaylist(String name, List<String> songsPath) {
        Validate.notNull(name, "Playlist name cannot be null");
        Validate.notNull(name, "Playlist's songs path cannot be null");
        Validate.isTrue(!name.trim().isEmpty(), "Playlist name cannot be empty");
        Validate.isTrue(!songsPath.isEmpty(), "Playlist's songs path name cannot be empty");

        Playlist playlist = new Playlist();
        playlist.setName(name);
        playlist.setSongsPath(songsPath);

        return mPlaylistsManager.createOrUpdate(playlist) ? playlist : null;
    }

}
