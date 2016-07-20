package com.doers.wakemeapp.business.services.api;

import com.doers.wakemeapp.common.model.alarms.Playlist;

import java.util.List;

/**
 * This is the interface where all playlists services are exposed
 *
 * @author <a href="mailto:aajn88@gmail.com">Antonio Jimenez</a>
 */
public interface IPlaylistsService {

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
    Playlist createPlaylist(String name, List<String> songsPath);

}
