package com.doers.wakemeapp.controllers.alarms;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.doers.wakemeapp.R;
import com.doers.wakemeapp.business.services.api.IAlarmsService;
import com.doers.wakemeapp.common.model.alarms.Alarm;
import com.doers.wakemeapp.common.model.audio.Song;
import com.doers.wakemeapp.common.utils.DateUtils;
import com.doers.wakemeapp.controllers.common.BaseActivity;
import com.doers.wakemeapp.di.components.DiComponent;

import net.frakbot.glowpadbackport.GlowPadView;

import java.util.Calendar;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * This main objective of this activity is to notify the user that an alarm has been launched
 *
 * @author <a href="mailto:aajn88@gmail.com">Antonio Jimenez</a>
 */
public class LaunchAlarmActivity extends BaseActivity implements GlowPadView.OnTriggerListener {

  /** Tag for logs **/
  private static final String TAG = LaunchAlarmActivity.class.getSimpleName();

  /** Stop alarm **/
  private static final int STOP_ALARM = 2;

  /** Snooze alarm **/
  private static final int SNOOZE_ALARM = 0;

  /** Vibration Delay **/
  private static final int VIBRATION_DELAY = 500;

  /** Vibration pattern **/
  private static final long[] PATTERN = {0, 200, 500};

  /** Alarm action GlowPadView **/
  @BindView(R.id.alarm_action_gpv)
  GlowPadView mAlarmActionGpv;

  /** Alarm title TextView **/
  @BindView(R.id.alarm_title_rtv)
  TextView mAlarmTitleRtv;

  /** Alarm time TextView **/
  @BindView(R.id.alarm_time_rtv)
  TextView mAlarmTimeRtv;

  /** Alarm service **/
  @Inject
  IAlarmsService mAlarmsService;

  /** The received alarm **/
  private Alarm mCurrentAlarm;

  /** System vibrator **/
  private Vibrator mVibrator;

  /** The alarm {@link MediaPlayer} **/
  private MediaPlayer mAlarmMediaPlayer;

  /** Must keep playing? **/
  private boolean mKeepPlaying = false;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_launch_alarm);
    mCurrentAlarm =
            mAlarmsService.findAlarmById(getIntent().getIntExtra(IAlarmsService.ALARM_ID, -1));
    if (mCurrentAlarm == null) {
      finish();
      return;
    }

    prepareActivity();
    mVibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
    mAlarmActionGpv.setOnTriggerListener(this);
    mAlarmTitleRtv.setText(mCurrentAlarm.getName());
    Calendar now = Calendar.getInstance();
    now.set(Calendar.HOUR_OF_DAY, mCurrentAlarm.getHour());
    now.set(Calendar.MINUTE, mCurrentAlarm.getMinute());
    mAlarmTimeRtv.setText(DateUtils.format(now.getTime(), DateUtils.TIME_FORMAT));

    executeAlarm();
  }

  /**
   * This method executes the alarm. Chooses a song to be played, starts vibrating and create a
   * notification message
   */
  private void executeAlarm() {
    List<Song> songs = mCurrentAlarm.getPlaylist().getSongs();
    int randSong = new Random().nextInt(songs.size());
    Song selectedSong = songs.get(randSong);
    String songPath = selectedSong.getPath();
    mKeepPlaying = true;
    mAlarmMediaPlayer = MediaPlayer.create(this, Uri.parse(songPath));
    if (mAlarmMediaPlayer != null) {
      mAlarmMediaPlayer.setLooping(true);
      mAlarmMediaPlayer.start();
    }

    new Handler().post(new Runnable() {
      @Override
      public void run() {
        mVibrator.vibrate(PATTERN, -1);
        if (mKeepPlaying) {
          new Handler().postDelayed(this, VIBRATION_DELAY);
        }
      }
    });
  }

  /**
   * This method prepares the activity to be displayed even if the device is locked
   */
  private void prepareActivity() {
    Window win = getWindow();
    win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
            WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
    win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
            WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
  }

  /**
   * This method processes the user's action. E.g. stops the alarm if the user selects {@link
   * #STOP_ALARM} or snooze the alarm if the user selects {@link #SNOOZE_ALARM}
   *
   * @param action
   *         User's action that could be {@link #STOP_ALARM} or {@link #SNOOZE_ALARM}
   */
  private void processAction(int action) {
    switch (action) {
      default:
      case STOP_ALARM:
        stopAlarm();
        break;
      case SNOOZE_ALARM:
        snoozeAlarm();
        break;
    }
    stopPlaying();
  }

  /**
   * This method stops the playing music and the device vibrations
   */
  private void stopPlaying() {
    mKeepPlaying = false;
    if (mAlarmMediaPlayer != null && mAlarmMediaPlayer.isPlaying()) {
      mAlarmMediaPlayer.stop();
    }
  }

  /**
   * This method stops the current alarm
   */
  private void stopAlarm() {
    Log.i(TAG, "Stopping alarm");
    finish();
  }

  /**
   * This method snoozes the current alarm
   */
  private void snoozeAlarm() {
    Log.i(TAG, "Snoozing alarm");
    // TODO: Snooze alarm
    finish();
  }

  @Override
  protected void injectComponent(DiComponent diComponent) {
    diComponent.inject(this);
  }

  @Override
  public void onBackPressed() {
  }

  @Override
  public void onGrabbed(View v, int handle) {
  }

  @Override
  public void onReleased(View v, int handle) {
  }

  @Override
  public void onTrigger(View v, int target) {
    Log.d(TAG, "Target triggered! ID=" + target);
    mAlarmActionGpv.reset(true);
    if (mVibrator.hasVibrator()) {
      mVibrator.vibrate(200);
    }
    processAction(target);
  }

  @Override
  public void onGrabbedStateChange(View v, int handle) {
  }

  @Override
  public void onFinishFinalAnimation() {
  }
}
