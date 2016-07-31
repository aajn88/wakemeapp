package com.doers.wakemeapp.controllers.playlists;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.doers.wakemeapp.R;
import com.doers.wakemeapp.business.services.api.IPlaylistsService;
import com.doers.wakemeapp.controllers.common.BaseActivity;
import com.doers.wakemeapp.di.components.DiComponent;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Activity where playlists are managed. They can be added, edited or deleted
 *
 * @author <a href="mailto:aajn88@gmail.com">Antonio A. Jimenez N.</a>
 */
public class PlaylistsManagerActivity extends BaseActivity implements View.OnClickListener {

    /** Playlists RecyclerView **/
    @BindView(R.id.playlists_rv)
    RecyclerView mPlaylistsRv;

    /** Add playlist FAB **/
    @BindView(R.id.add_playlist_fab)
    FloatingActionButton mAddPlaylistFab;

    /** Playlist Service **/
    @Inject
    IPlaylistsService mPlaylistsService;

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
        setTitle(R.string.playlists);

        mAddPlaylistFab.setOnClickListener(this);
        mPlaylistsRv.setLayoutManager(new LinearLayoutManager(this));
        mPlaylistsRv.setAdapter(new PlaylistsAdapter(this, mPlaylistsService.getAllPlaylists()));
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
                AddPlaylistActivity.startActivity(this);
                break;
        }
    }
}
