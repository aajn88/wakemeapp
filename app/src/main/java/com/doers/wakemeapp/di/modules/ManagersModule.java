package com.doers.wakemeapp.di.modules;

import android.content.Context;
import android.util.Log;

import com.doers.wakemeapp.persistence.DatabaseHelper;
import com.doers.wakemeapp.persistence.managers.api.IAlarmsManager;
import com.doers.wakemeapp.persistence.managers.api.IPlaylistsManager;
import com.doers.wakemeapp.persistence.managers.api.ISongsManager;
import com.doers.wakemeapp.persistence.managers.impl.AlarmsManager;
import com.doers.wakemeapp.persistence.managers.impl.PlaylistsManager;
import com.doers.wakemeapp.persistence.managers.impl.SongsManager;

import java.sql.SQLException;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * This is the managers module
 *
 * @author <a href="mailto:aajn88@gmail.com">Antonio Jimenez</a>
 */
@Module
public class ManagersModule {

    /** Tag for logs **/
    private static final String TAG = ManagersModule.class.getName();

    /** Context to be injected into dependencies **/
    private final Context mContext;

    /**
     * Managers Module constructor
     *
     * @param context
     *         Application context
     */
    public ManagersModule(Context context) {
        this.mContext = context;
    }

    /**
     * Provides with an instance of {@link DatabaseHelper}
     *
     * @return {@link DatabaseHelper} instance
     */
    @Provides
    @Singleton
    public DatabaseHelper databaseHelper() {
        return new DatabaseHelper(mContext);
    }

    /**
     * Bind of the {@link IAlarmsManager} with its implementation
     *
     * @param helper
     *         DB Helper
     *
     * @return Implementation of the Alarms Manager
     */
    @Provides
    @Singleton
    public IAlarmsManager alarmsManager(DatabaseHelper helper) {
        try {
            return new AlarmsManager(helper);
        } catch (SQLException e) {
            Log.e(TAG, "An error occurred while creating the instance of the Alarms Manager", e);
        }
        return null;
    }

    /**
     * Bind of the {@link IPlaylistsManager} with its implementation
     *
     * @param helper
     *         DB Helper
     *
     * @return Implementation of the Playlists Manager
     */
    @Provides
    @Singleton
    public IPlaylistsManager playlistsManager(DatabaseHelper helper) {
        try {
            return new PlaylistsManager(helper);
        } catch (SQLException e) {
            Log.e(TAG, "An error occurred while creating the instance of the Playlists Manager", e);
        }
        return null;
    }

    /**
     * Bind of the {@link ISongsManager} with its implementation
     *
     * @param helper
     *         DB Helper
     *
     * @return Implementation of the Songs Manager
     */
    @Provides
    @Singleton
    public ISongsManager songsManager(DatabaseHelper helper) {
        try {
            return new SongsManager(helper);
        } catch (SQLException e) {
            Log.e(TAG, "An error occurred while creating the instance of the Songs Manager", e);
        }
        return null;
    }

}
