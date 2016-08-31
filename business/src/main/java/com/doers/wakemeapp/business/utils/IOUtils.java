package com.doers.wakemeapp.business.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;

/**
 * Utility class that offers common and standard I/O methods
 *
 * @author <a href="mailto:aajn88@gmail.com">Antonio Jimenez</a>
 */
public final class IOUtils {

  /** Tag for logs **/
  private static final String TAG = IOUtils.class.getName();

  /** Audio request **/
  private static final String AUDIO_REQUEST = "audio/*";

  /** Content scheme **/
  private static final String CONTENT_SCHEME = "content";

  /** Private constructor to avoid instances **/
  private IOUtils() {
  }

  /**
   * This method requests an Audio file from the user. The activity should implement {@link
   * Activity#onActivityResult(int, int, Intent)} method to manage the process response
   *
   * @param activity
   *         Activity that should implement {@link Activity#onActivityResult(int, int, Intent)}
   * @param requestId
   *         Request Id which will be used to create the file request
   */
  public static void requestAudio(Activity activity, int requestId) {
    Intent intentRequestAudio;
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
      intentRequestAudio = new Intent(Intent.ACTION_OPEN_DOCUMENT);
      intentRequestAudio.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
    } else {
      intentRequestAudio = new Intent(Intent.ACTION_GET_CONTENT);
    }
    intentRequestAudio.setType(AUDIO_REQUEST);
    activity.startActivityForResult(intentRequestAudio, requestId);
  }

  /**
   * This method gets the filename from a URI
   *
   * @param context
   *         App context
   * @param uri
   *         target URI
   *
   * @return Filename if it can be retrieved, otherwise returns null
   */
  public static String getFileName(Context context, Uri uri) {
    String result = null;
    if (uri.getScheme().equals(CONTENT_SCHEME)) {
      Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
      try {
        if (cursor != null && cursor.moveToFirst()) {
          result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
        }
      } finally {
        cursor.close();
      }
    }
    if (result == null) {
      result = uri.getPath();
      int cut = result.lastIndexOf('/');
      if (cut != -1) {
        result = result.substring(cut + 1);
      }
    }
    return result;
  }

  /**
   * This method gets the real path from a URI
   *
   * @param context
   *         App contezt
   * @param uri
   *         Uri to be extracted
   *
   * @return Real URI path
   */
  public static String getPath(Context context, Uri uri) {
    String result;
    Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
    if (cursor == null) { // Source is Dropbox or other similar local file path
      result = uri.getPath();
    } else {
      cursor.moveToFirst();
      int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
      try {
        result = cursor.getString(idx);
      } catch (IllegalStateException e) {
        Log.e(TAG, "Error getting URI path", e);
        result = uri.toString();
      }
      cursor.close();
    }
    return result;
  }

}
