package com.doers.wakemeapp.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.doers.wakemeapp.common.model.alarms.Alarm;
import com.doers.wakemeapp.common.model.audio.Playlist;
import com.doers.wakemeapp.common.model.audio.Song;
import com.j256.ormlite.android.AndroidConnectionSource;
import com.j256.ormlite.android.AndroidDatabaseConnection;
import com.j256.ormlite.android.DatabaseTableConfigUtil;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.support.DatabaseConnection;
import com.j256.ormlite.table.DatabaseTableConfig;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;


/**
 * The DBHelper to manage the SQLite DB in Android
 *
 * @author <a href="mailto:antonio-jimenez@accionplus.com">Antonio A. Jimenez N.</a>
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

  /** Logs Tag **/
  private static final String TAG_LOG = DatabaseHelper.class.getName();

  /** DB name **/
  private static final String DB_NAME = "dbd_control.db";

  /** DB Version **/
  private static final int DB_VERSION = 3;

  /** The connection source **/
  protected AndroidConnectionSource connectionSource = new AndroidConnectionSource(this);

  /**
   * Inits the DB Helper
   *
   * @param context
   *         The application Context
   */
  public DatabaseHelper(Context context) {
    super(context, DB_NAME, null, DB_VERSION);
  }

  /**
   * What to do when your database needs to be created. Usually this entails creating the
   * tables and
   * loading any
   * initial data.
   * <p/>
   * <p>
   * <b>NOTE:</b> You should use the connectionSource argument that is passed into this method
   * call
   * or the one
   * returned by getConnectionSource(). If you use your own, a recursive call or other unexpected
   * results may result.
   * </p>
   *
   * @param database
   *         Database being created.
   * @param connectionSource
   *         To use get connections to the database to be created.
   */
  @Override
  public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
    setUpDatabase(database);
  }

  /**
   * This method sets up the SQLite DB and all its tables
   *
   * @param db
   *         DB Connection
   */
  private void setUpDatabase(SQLiteDatabase db) {
    setUpDatabase(db, null, null);
  }

  /**
   * This method sets up the DB when a new version update is requested. If any of both the {@code
   * oldVersion} or the {@code newVersion} is null, then the updated process will be skipped, the
   * regular build process will be executed
   *
   * @param db
   *         The DB connection
   * @param oldVersion
   *         DB old version
   * @param newVersion
   *         DB new version
   */
  private void setUpDatabase(SQLiteDatabase db, Integer oldVersion, Integer newVersion) {
    DatabaseConnection con = connectionSource.getSpecialConnection();
    boolean cleanSpecial = false;
    if (con == null) {
      con = new AndroidDatabaseConnection(db, true);

      try {
        connectionSource.saveSpecialConnection(con);
        cleanSpecial = true;
      } catch (SQLException e) {
        throw new IllegalStateException("The special connection could not be stored", e);
      }
    }

    try {
      if (oldVersion == null || newVersion == null) {
        onCreate();
      } else {
        onUpgrade(oldVersion, newVersion);
      }
    } finally {
      if (cleanSpecial) {
        connectionSource.clearSpecialConnection(con);
      }
    }
  }

  /**
   * Creates the DB scheme and tables
   */
  private void onCreate() {
    try {
      Log.i(TAG_LOG, "DB onCreate");

      // List of tables to be created
      TableUtils.createTable(connectionSource, Song.class);
      TableUtils.createTable(connectionSource, Playlist.class);
      TableUtils.createTable(connectionSource, Alarm.class);

      Log.i(TAG_LOG, "DB successfully created");
    } catch (SQLException e) {
      Log.e(TAG_LOG, "An error has occurred while creating the DB", e);
      throw new RuntimeException(e);
    }
  }

  /**
   * What to do when your database needs to be updated. This could mean careful migration of old
   * data to new data.
   * Maybe adding or deleting database columns, etc..
   * <p/>
   * <p>
   * <b>NOTE:</b> You should use the connectionSource argument that is passed into this method
   * call
   * or the one
   * returned by getConnectionSource(). If you use your own, a recursive call or other unexpected
   * results may result.
   * </p>
   *
   * @param database
   *         Database being upgraded.
   * @param connectionSource
   *         To use get connections to the database to be updated.
   * @param oldVersion
   *         The version of the current database so we can know what to do to the database.
   * @param newVersion
   *         The version of the old database so we can know what to do to the database.
   */
  @Override
  public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int
          oldVersion, int newVersion) {
    setUpDatabase(database, oldVersion, newVersion);
  }

  /**
   * Updates the DB given its {@code oldVersion} and its new {@code newVersion}
   *
   * @param oldVersion
   *         The old version of the DB
   * @param newVersion
   *         The new version of the DB
   */
  private void onUpgrade(int oldVersion, int newVersion) {
    try {
      Log.i(TAG_LOG, "The DB onUpgrade");

      // Just in this case: Drop all tables and create the DB again
      TableUtils.dropTable(connectionSource, Alarm.class, true);
      TableUtils.dropTable(connectionSource, Playlist.class, true);
      TableUtils.dropTable(connectionSource, Song.class, true);

      onCreate();
    } catch (SQLException e) {
      Log.e(TAG_LOG, "An error has occurred while updating the DB", e);
      throw new RuntimeException(e);
    }
  }

  /**
   * This method obtains a DAO given its Class
   * <p>
   * Source: https://goo.gl/6LIYy2
   *
   * @param clazz
   *         The DAO class
   * @param <D>
   *         DAO super class
   * @param <T>
   *         Requested DAO class
   *
   * @return The DAO instance
   *
   * @throws SQLException
   */
  public <D extends Dao<T, ?>, T> D getDao(Class<T> clazz) throws SQLException {
    // lookup the dao, possibly invoking the cached database config
    Dao<T, ?> dao = DaoManager.lookupDao(connectionSource, clazz);
    if (dao == null) {
      // try to use our new reflection magic
      DatabaseTableConfig<T> tableConfig = DatabaseTableConfigUtil
              .fromClass(connectionSource, clazz);
      if (tableConfig == null) {
        /**
         * Note: We have to do this to get to see if they are using the deprecated
         * annotations like
         * {@link DatabaseFieldSimple}.
         */
        dao = (Dao<T, ?>) DaoManager.createDao(connectionSource, clazz);
      } else {
        dao = (Dao<T, ?>) DaoManager.createDao(connectionSource, tableConfig);
      }
    }

    @SuppressWarnings("unchecked")
    D castDao = (D) dao;
    return castDao;
  }
}
