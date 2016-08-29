package com.doers.wakemeapp.business.services.api;

import com.doers.wakemeapp.common.model.audio.Playlist;
import com.doers.wakemeapp.common.model.audio.Song;

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
   * @param songs
   *         Selected songs for the playlist
   *
   * @return Created playlist. Returns null if an error occurred
   */
  Playlist createPlaylist(String name, List<Song> songs);

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
  boolean updatePlaylist(int playlistId, String name, List<Song> songs);

  /**
   * This method finds a playlist given its ID
   *
   * @param playlistId
   *         Playlist ID
   *
   * @return Playlist instance or null if it was not found
   */
  Playlist findPlaylistById(int playlistId);

  /**
   * This method gets all stored playlists
   *
   * @return All stored playlists
   */
  List<Playlist> getAllPlaylists();

  /**
   * This method returns the default playlist
   *
   * @return The default playlist
   */
  Playlist getDefaultPlaylist();
}
