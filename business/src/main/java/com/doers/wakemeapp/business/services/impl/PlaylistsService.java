package com.doers.wakemeapp.business.services.impl;

import com.doers.wakemeapp.business.services.api.IPlaylistsService;
import com.doers.wakemeapp.common.model.audio.Playlist;
import com.doers.wakemeapp.common.model.audio.Song;
import com.doers.wakemeapp.persistence.managers.api.IPlaylistsManager;
import com.doers.wakemeapp.persistence.managers.api.ISongsManager;

import org.apache.commons.lang3.Validate;

import java.util.List;

/**
 * Implementation of the {@link IPlaylistsService} exposed methods
 *
 * @author <a href="mailto:aajn88@gmail.com">Antonio Jimenez</a>
 */
public class PlaylistsService implements IPlaylistsService {

    /** Playlists manager **/
    private final IPlaylistsManager mPlaylistsManager;

    /** Songs manager **/
    private final ISongsManager mSongsManager;

    /**
     * Playlists Service constructor
     *
     * @param playlistsManager
     *         Playlists Manager
     * @param songsManager
     *         Songs Manager
     */
    public PlaylistsService(IPlaylistsManager playlistsManager, ISongsManager songsManager) {
        this.mPlaylistsManager = playlistsManager;
        this.mSongsManager = songsManager;
    }

    /**
     * This method creates a playlist given its name and its songs
     *
     * @param name
     *         Playlist name
     * @param songs
     *         Selected songs for the playlist
     *
     * @return Created playlist. Returns null if an error occurred
     */
    @Override
    public Playlist createPlaylist(String name, List<Song> songs) {
        validatePlaylistFields(name, songs);

        Playlist playlist = new Playlist();
        playlist.setName(name);
        playlist.setSongs(songs);
        boolean success = mPlaylistsManager.createOrUpdate(playlist);
        if (success) {
            persistSongs(songs, playlist);
        }

        return success ? playlist : null;
    }

    /**
     * This method persists songs list
     *
     * @param songs
     *         Songs to be persisted
     * @param playlist
     *         Owner playlist
     */
    private void persistSongs(List<Song> songs, Playlist playlist) {
        for (Song song : songs) {
            song.setPlaylist(playlist);
            mSongsManager.createOrUpdate(song);
        }
    }

    /**
     * This method validates the playlist's fields
     *
     * @param name
     *         Playlist name
     * @param songs
     *         Playlist songs
     */
    private void validatePlaylistFields(String name, List<Song> songs) {
        Validate.notNull(name, "Playlist name cannot be null");
        Validate.notNull(name, "Playlist's songs path cannot be null");
        Validate.isTrue(!name.trim().isEmpty(), "Playlist name cannot be empty");
        Validate.isTrue(!songs.isEmpty(), "Playlist's songs path name cannot be empty");
    }

    /**
     * This method updates an existing playlist
     *
     * @param playlistId
     *         Playlist Id to be updated
     * @param name
     *         New playlist's name
     * @param songs
     *         New playlist's songs
     *
     * @return True if the playlist was updated. Otherwise returns False
     */
    @Override
    public boolean updatePlaylist(int playlistId, String name, List<Song> songs) {
        Playlist playlist = mPlaylistsManager.findById(playlistId);
        if (playlist == null) {
            return false;
        }

        validatePlaylistFields(name, songs);

        playlist.setName(name);
        playlist.setSongs(songs);

        return mPlaylistsManager.createOrUpdate(playlist);
    }

    /**
     * This method gets all stored playlists
     *
     * @return All stored playlists
     */
    @Override
    public List<Playlist> getAllPlaylists() {
        List<Playlist> playlists = mPlaylistsManager.all();

        for (Playlist playlist : playlists) {
            playlist.setSongs(mSongsManager.findByPlaylistId(playlist.getId()));
        }

        return playlists;
    }

}
