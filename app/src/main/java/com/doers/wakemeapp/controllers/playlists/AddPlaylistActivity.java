package com.doers.wakemeapp.controllers.playlists;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.doers.wakemeapp.R;
import com.doers.wakemeapp.business.services.api.IPlaylistsService;
import com.doers.wakemeapp.business.utils.IOUtils;
import com.doers.wakemeapp.common.model.audio.Playlist;
import com.doers.wakemeapp.common.model.audio.Song;
import com.doers.wakemeapp.common.utils.StringUtils;
import com.doers.wakemeapp.controllers.common.BaseActivity;
import com.doers.wakemeapp.di.components.DiComponent;
import com.doers.wakemeapp.utils.ViewUtils;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Activity to add a playlist
 *
 * @author <a href="mailto:aajn88@gmail.com">Antonio A. Jimenez N.</a>
 */
public class AddPlaylistActivity extends BaseActivity implements View.OnClickListener {

  /** Tag for logs **/
  private static final String TAG = AddPlaylistActivity.class.getName();

  /** Playlist ID **/
  private static final String PLAYLIST_ID = "PLAYLIST_ID";

  /** Song request ID **/
  private static final int SONG_REQUEST_ID = 16;

  /** Edit playlist request code **/
  public static final int EDIT_PLAYLIST_REQUEST_CODE = 123;

  /** Add song FAB **/
  @BindView(R.id.add_song_fab)
  FloatingActionButton mAddSongFab;

  /** No songs TextView **/
  @BindView(R.id.no_songs_tv)
  TextView mNoSongsTv;

  /** Songs RecyclerView **/
  @BindView(R.id.playlist_songs_rv)
  RecyclerView mPlaylistSongsRv;

  /** Playlist name **/
  @BindView(R.id.playlist_name_et)
  EditText mPlaylistNameEt;

  /** Playlists Service **/
  @Inject
  IPlaylistsService mPlaylistsService;

  /** Songs adapter **/
  private SongsAdapter mAdapter;

  /** Playlist to be edited **/
  private Playlist mPlaylist;

  /**
   * This method starts Add Playlists Activity given a context and the request code
   *
   * @param activity
   *         Application context
   * @param requestCode
   *         Request code for result
   */
  public static void startActivity(Activity activity, int requestCode) {
    startActivity(activity, null, requestCode);
  }

  /**
   * This method starts Add Playlists Activity given a context a playlistId to be edited and the
   * request code
   *
   * @param activity
   *         Application context
   * @param playlistId
   *         PlaylistId to be edited. If null, then add playlist process is performed
   * @param requestCode
   *         Request code for result
   */
  public static void startActivity(Activity activity, Integer playlistId, int requestCode) {
    if (activity == null) {
      return;
    }

    Intent intent = new Intent(activity, AddPlaylistActivity.class);
    if (playlistId != null) {
      intent.putExtra(PLAYLIST_ID, playlistId);
    }
    activity.startActivityForResult(intent, requestCode);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add_playlist);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    setTitle(null);

    int playlistId = getIntent().getIntExtra(PLAYLIST_ID, -1);
    if (playlistId != -1) {
      mPlaylist = mPlaylistsService.findPlaylistById(playlistId);
      mAdapter = new SongsAdapter(this, mPlaylist.getSongs());
      mPlaylistNameEt.setText(mPlaylist.getName());
      mNoSongsTv.setVisibility(View.GONE);
    } else {
      mAdapter = new SongsAdapter(this);
      mNoSongsTv.setVisibility(View.VISIBLE);
    }

    mPlaylistSongsRv.setLayoutManager(new LinearLayoutManager(this));
    mPlaylistSongsRv.setAdapter(mAdapter);

    mAddSongFab.setOnClickListener(this);

  }

  @Override
  protected void onStop() {
    super.onStop();
    mAdapter.onStop();
  }

  /**
   * Injection component. This should be done if there are fields to be injected
   *
   * @param diComponent
   *         Dependency injection
   */
  @Override
  protected void injectComponent(DiComponent diComponent) {
    diComponent.inject(this);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.menu_check, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.check_item:
        savePlaylist();
        return true;
      case android.R.id.home:
        confirmExit();
        return true;
    }

    return super.onOptionsItemSelected(item);
  }

  /**
   * This method confirms the exit
   */
  private void confirmExit() {
    confirmExit(this, null, R.string.discard_playlist_msg, R.string.discard);
  }

  /**
   * This method saves the current playlist
   */
  private void savePlaylist() {
    if (!validateFields()) {
      return;
    }

    String playlistName = mPlaylistNameEt.getText().toString();
    List<Song> songs = mAdapter.getSongs();
    if (mPlaylist != null) {
      mPlaylistsService.updatePlaylist(mPlaylist.getId(), playlistName, songs);
    } else {
      mPlaylistsService.createPlaylist(playlistName, songs);
    }
    setResult(RESULT_OK);
    finish();
  }

  /**
   * This method validates all the fields. If there is an invalid field, then shows the
   * corresponding error.
   *
   * @return True if all the fields are valid. Otherwise returns False
   */
  private boolean validateFields() {
    if (mPlaylistNameEt.getText().length() == 0) {
      mPlaylistNameEt.setError(getString(R.string.mandatory_field));
      return false;
    }
    if (mAdapter.getSongs().isEmpty()) {
      ViewUtils.buildStandardDialogBuilder(this, R.string.empty_playlist_error).create()
              .show();
      return false;
    }
    return true;
  }

  @Override
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.add_song_fab:
        IOUtils.requestAudio(this, SONG_REQUEST_ID);
        break;
    }
  }

  @SuppressWarnings("WrongConstant")
  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (requestCode == SONG_REQUEST_ID && resultCode == RESULT_OK) {
      Uri songUri = data.getData();
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        int flags = data.getFlags() &
                (Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        getContentResolver().takePersistableUriPermission(songUri, flags);
      }
      Log.d(TAG, StringUtils.format("Selected song uri: %s", songUri));
      requestName(songUri);
    }
  }

  /**
   * This method requests the name of the song
   *
   * @param songUri
   *         Song URI
   */
  private void requestName(final Uri songUri) {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setTitle(R.string.type_song_title);

    final EditText nameEt = new EditText(this);
    nameEt.setText(IOUtils.getFileName(this, songUri));
    nameEt.setInputType(InputType.TYPE_CLASS_TEXT);
    builder.setView(nameEt)
            .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialogInterface, int i) {
                Song song = new Song();
                song.setName(nameEt.getText().toString());
                song.setPath(IOUtils.getPath(getActivity(), songUri));
                mAdapter.addSong(song);
                mNoSongsTv.setVisibility(View.GONE);
              }
            }).setNegativeButton(R.string.cancel, null);
    builder.show();
  }

  @Override
  public void onBackPressed() {
    confirmExit();
  }

}
