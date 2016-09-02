package com.doers.wakemeapp.controllers.playlists;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MenuItem;
import android.view.View;

import com.doers.wakemeapp.R;
import com.doers.wakemeapp.business.services.api.IPlaylistsService;
import com.doers.wakemeapp.common.model.audio.Playlist;
import com.doers.wakemeapp.controllers.common.BaseActivity;
import com.doers.wakemeapp.custom_views.common.Snackbar;
import com.doers.wakemeapp.custom_views.decorations.InitialSpaceItemDecoration;
import com.doers.wakemeapp.di.components.DiComponent;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Activity where playlists are managed. They can be added, edited or deleted
 *
 * @author <a href="mailto:aajn88@gmail.com">Antonio A. Jimenez N.</a>
 */
public class PlaylistsManagerActivity extends BaseActivity implements View.OnClickListener {

  /** Add playlist request code **/
  private static final int ADD_PLAYLIST_REQUEST_CODE = 316;

  /** Playlists RecyclerView **/
  @BindView(R.id.playlists_rv)
  RecyclerView mPlaylistsRv;

  /** Add playlist FAB **/
  @BindView(R.id.add_playlist_fab)
  FloatingActionButton mAddPlaylistFab;

  /** Playlist Service **/
  @Inject
  IPlaylistsService mPlaylistsService;

  /** The playlists adapter **/
  private PlaylistsAdapter mAdapter;

  /**
   * This method starts Playlists Manager Activity given a context
   *
   * @param context
   *         Application context
   */
  public static void startActivity(Context context) {
    if (context == null) {
      return;
    }

    Intent intent = new Intent(context, PlaylistsManagerActivity.class);
    context.startActivity(intent);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_playlists_manager);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    setTitle(R.string.playlists);

    mAddPlaylistFab.setOnClickListener(this);
    mPlaylistsRv.setLayoutManager(new LinearLayoutManager(this));
    mPlaylistsRv.addItemDecoration(new InitialSpaceItemDecoration(
            (int) getResources().getDimension(R.dimen.condensedVerticalMargin)));
    loadPlaylists();

    ItemTouchHelper.SimpleCallback callback =
            new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
              @Override
              public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                    RecyclerView.ViewHolder target) {
                return false;
              }

              @Override
              public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                deletePlaylist(viewHolder.getAdapterPosition());
              }
            };

    ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
    itemTouchHelper.attachToRecyclerView(mPlaylistsRv);
  }

  /**
   * This method loads the stored playlists
   */
  private void loadPlaylists() {
    mAdapter = new PlaylistsAdapter(this, mPlaylistsService.getAllPlaylists());
    mPlaylistsRv.setAdapter(mAdapter);
  }

  private void deletePlaylist(int position) {
    Playlist playlist = mAdapter.getItem(position);
    if (!mPlaylistsService.deletePlaylist(playlist.getId())) {
      mAdapter.notifyItemChanged(position);
      Snackbar.make(mPlaylistsRv, R.string.default_playlist_cannot_delete, Snackbar.LENGTH_SHORT)
              .show();
    } else {
      mAdapter.remove(position);
      Snackbar.make(mPlaylistsRv, R.string.playlist_deleted, Snackbar.LENGTH_SHORT).show();
    }
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        finish();
        return true;
    }
    return super.onOptionsItemSelected(item);
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
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.add_playlist_fab:
        AddPlaylistActivity.startActivity(this, ADD_PLAYLIST_REQUEST_CODE);
        break;
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (requestCode == ADD_PLAYLIST_REQUEST_CODE ||
            requestCode == AddPlaylistActivity.EDIT_PLAYLIST_REQUEST_CODE &&
                    resultCode == RESULT_OK) {
      int msg = requestCode == ADD_PLAYLIST_REQUEST_CODE ? R.string.playlist_created :
              R.string.playlist_saved;
      loadPlaylists();
      Snackbar.make(mPlaylistsRv, msg, Snackbar.LENGTH_SHORT).show();
    }
  }
}
