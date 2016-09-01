package com.doers.wakemeapp.business.services.impl;

import android.content.Context;
import android.media.RingtoneManager;
import android.net.Uri;

import com.doers.wakemeapp.business.R;
import com.doers.wakemeapp.business.services.api.IPlaylistsService;
import com.doers.wakemeapp.business.utils.IOUtils;
import com.doers.wakemeapp.common.model.audio.Playlist;
import com.doers.wakemeapp.common.model.audio.Song;
import com.doers.wakemeapp.persistence.managers.api.IPlaylistsManager;
import com.doers.wakemeapp.persistence.managers.api.ISongsManager;

import org.apache.commons.lang3.Validate;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the {@link IPlaylistsService} exposed methods
 *
 * @author <a href="mailto:aajn88@gmail.com">Antonio Jimenez</a>
 */
public class PlaylistsService implements IPlaylistsService {

  /** Application context **/
  private final Context mContext;

  /** Playlists manager **/
  private final IPlaylistsManager mPlaylistsManager;

  /** Songs manager **/
  private final ISongsManager mSongsManager;

  /**
   * Playlists Service constructor
   *
   * @param context
   *         Application context
   * @param playlistsManager
   *         Playlists Manager
   * @param songsManager
   *         Songs Manager
   */
  public PlaylistsService(Context context, IPlaylistsManager playlistsManager,
                          ISongsManager songsManager) {
    this.mContext = context;
    this.mPlaylistsManager = playlistsManager;
    this.mSongsManager = songsManager;
    createDefaultPlaylist();
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
    return createPlaylist(name, songs, false);
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
  private Playlist createPlaylist(String name, List<Song> songs, boolean isDefault) {
    validatePlaylistFields(name, songs);

    Playlist playlist = new Playlist();
    playlist.setName(name);
    playlist.setSongs(songs);
    playlist.setDefault(isDefault);
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

  @Override
  public Playlist findPlaylistById(int playlistId) {
    Playlist playlist = mPlaylistsManager.findById(playlistId);
    if (playlist != null) {
      playlist.setSongs(mSongsManager.findByPlaylistId(playlistId));
    }
    return playlist;
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

  @Override
  public Playlist getDefaultPlaylist() {
    return mPlaylistsManager.findDefaultPlaylist();
  }

  @Override
  public boolean deletePlaylist(int playlistId) {
    Playlist playlist = findPlaylistById(playlistId);
    if (playlist == null) {
      // The playlist doesn't exist
      return true;
    }
    if (playlist.isDefault()) {
      return false;
    }

    mPlaylistsManager.deleteById(playlistId);
    return true;
  }

  /**
   * This method creates the default playlist if there is no stored playlist
   */
  private void createDefaultPlaylist() {
    Playlist defaultPlaylist = getDefaultPlaylist();
    if (defaultPlaylist != null) {
      return;
    }
    Uri defaultAlarm = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
    Song song = new Song();
    song.setName(mContext.getString(R.string.default_song_name));
    song.setPath(IOUtils.getPath(mContext, defaultAlarm));
    List<Song> songs = new ArrayList<>(1);
    songs.add(song);
    createPlaylist(mContext.getString(R.string.default_playlist_name), songs, true);
  }

}
