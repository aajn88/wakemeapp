package com.doers.wakemeapp.controllers.alarms;

import android.net.Uri;
import android.os.Binder;

/**
 * Sound service to play and stop the sounds
 *
 * @author <a href="mailto:aajn88@gmail.com">Antonio Jimenez</a>
 */
public interface ISoundService {

  /**
   * This method plays the requested {@code audioUri}
   *
   * @param requestCode
   *         Request code
   * @param audioUri
   *         Audio to be played
   */
  void playSound(int requestCode, Uri audioUri);

  /**
   * This method stops the sound for the given {@code requestCode} (if is still playing)
   *
   * @param requestCode
   *         Request code of the sound to be stoped
   *
   * @return True if the sound associated to the given {@code requestCode} was playing and was
   * successfully stopped. False in case the sound was already stopped
   */
  boolean stopSound(int requestCode);

  /**
   * The binder for the sound service
   */
  abstract class SoundBinder extends Binder {

    /**
     * This method returns an instance of the {@link ISoundService}
     *
     * @return {@link ISoundService} instance
     */
    public abstract ISoundService getService();
  }

}
