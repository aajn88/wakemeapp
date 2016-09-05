package com.doers.wakemeapp.controllers.alarms;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Vibrator;

/**
 * Sound service to play and stop the sounds
 *
 * @author <a href="mailto:aajn88@gmail.com">Antonio Jimenez</a>
 */
public class SoundService extends Service implements ISoundService {

  /** Vibration Delay **/
  private static final int VIBRATION_DELAY = 500;

  /** Vibration pattern **/
  private static final long[] PATTERN = {0, 200, 500};

  /** The Binder instance **/
  private SoundBinder mBinder = new SoundBinder() {
    @Override
    public ISoundService getService() {
      return SoundService.this;
    }
  };

  /** System vibrator **/
  private Vibrator mVibrator;

  /** The alarm {@link MediaPlayer} **/
  private MediaPlayer mAlarmMediaPlayer;

  /** The audio manager **/
  private AudioManager mAudioManager;

  /** Must keep playing? **/
  private boolean mKeepPlaying = false;

  /** Indicates if the vibration is still alive **/
  private boolean mIsStillAlive = false;

  /** Current request code **/
  private int mCurrentRequestCode = -1;

  /** Current user volume **/
  private int mUserVolume = -1;

  /** Default constructor **/
  public SoundService() {
  }

  @Override
  public void onCreate() {
    super.onCreate();
    mVibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
    mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
  }

  @Override
  public IBinder onBind(Intent intent) {
    return mBinder;
  }

  @Override
  public void playSound(int requestCode, Uri audioUri) {
    stopAndRelease();

    mCurrentRequestCode = requestCode;
    mUserVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

    mAlarmMediaPlayer = MediaPlayer.create(this, audioUri);
    if (mAlarmMediaPlayer != null) {
      mAlarmMediaPlayer.setLooping(true);
      mAlarmMediaPlayer.start();
    }

    setVolume(mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));

    startVibration();
  }

  /**
   * This method sets given volume for the device
   *
   * @param volume
   *         Volume value to be set
   */
  private void setVolume(int volume) {
    mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, AudioManager.FLAG_PLAY_SOUND);
  }

  /**
   * This method starts the vibration
   */
  private void startVibration() {
    if (mKeepPlaying) {
      return;
    }
    // In case still vibration, then wait to avoid concurrency problems
    int delay = mIsStillAlive ? VIBRATION_DELAY * 2 : 0;
    mKeepPlaying = true;
    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        mIsStillAlive = true;
        mVibrator.vibrate(PATTERN, -1);
        if (mKeepPlaying) {
          new Handler().postDelayed(this, VIBRATION_DELAY);
        } else {
          mIsStillAlive = false;
        }
      }
    }, delay);
  }

  /**
   * This method stops and releases the current media player
   */
  private void stopAndRelease() {
    stopVibration();
    stopSound();
    releaseSound();
    mCurrentRequestCode = -1;
  }

  /**
   * This method stops the current vibration
   */
  private void stopVibration() {
    mKeepPlaying = false;
  }

  /**
   * This method stops the current sound if it is playing
   */
  private void stopSound() {
    if (mAlarmMediaPlayer == null) {
      return;
    }
    if (mAlarmMediaPlayer.isPlaying()) {
      mAlarmMediaPlayer.stop();
      mAlarmMediaPlayer.reset();
    }
    if (mUserVolume != -1) {
      setVolume(mUserVolume);
    }
    mUserVolume = -1;
  }

  /**
   * This method releases the media player
   */
  private void releaseSound() {
    if (mAlarmMediaPlayer == null) {
      return;
    }
    mAlarmMediaPlayer.release();
    mAlarmMediaPlayer = null;
  }

  @Override
  public boolean stopSound(int requestCode) {
    if (mCurrentRequestCode != requestCode) {
      return false;
    }
    stopAndRelease();
    return true;
  }

}
