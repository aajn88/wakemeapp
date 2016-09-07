package com.doers.wakemeapp.controllers.alarms;

import android.annotation.TargetApi;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
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
import com.doers.wakemeapp.utils.DeviceUtils;
import com.doers.wakemeapp.utils.ViewUtils;

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

  /** Stop alarm action **/
  private static final String STOP_ALARM_ACTION = "STOP_ALARM_ACTION";

  /** Snooze alarm action **/
  private static final String SNOOZE_ALARM_ACTION = "SNOOZE_ALARM_ACTION";

  /** Stop alarm **/
  private static final int STOP_ALARM = 2;

  /** Snooze alarm **/
  private static final int SNOOZE_ALARM = 0;

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

  /** The alarm day **/
  private int mAlarmDay;

  /** Formatted time **/
  private String mFormattedTime;

  /** Bound sound service **/
  private ISoundService mSoundService;

  /** System vibrator **/
  private Vibrator mVibrator;

  /** The current song to be played **/
  private String mCurrentSong;

  /** Notification manager **/
  private NotificationManager mNotificationManager;

  /** The service connection **/
  private ServiceConnection mServiceConnection = new ServiceConnection() {

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
      mSoundService = ((ISoundService.SoundBinder) iBinder).getService();
      mSoundService.playSound(mCurrentAlarm.getId(), Uri.parse(mCurrentSong));
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
      mSoundService = null;
    }
  };

  /** Notification broadcast receiver **/
  private BroadcastReceiver mNotificationReceiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
      int alarmId = intent.getIntExtra(IAlarmsService.ALARM_ID, -1);
      if (alarmId == -1 || alarmId != mCurrentAlarm.getId()) {
        return;
      }
      switch (intent.getAction()) {
        case STOP_ALARM_ACTION:
          processAction(STOP_ALARM);
          break;
        case SNOOZE_ALARM_ACTION:
          processAction(SNOOZE_ALARM);
          break;
      }
    }
  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_launch_alarm);
    Intent intent = getIntent();
    mCurrentAlarm = mAlarmsService.findAlarmById(intent.getIntExtra(IAlarmsService.ALARM_ID, -1));
    mAlarmDay = intent.getIntExtra(IAlarmsService.ALARM_DAY, -1);
    if (mCurrentAlarm == null || mAlarmDay == -1) {
      finish();
      return;
    }

    establishStatusBarColor();

    prepareActivity();
    mVibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
    mAlarmActionGpv.setOnTriggerListener(this);
    mAlarmTitleRtv.setText(mCurrentAlarm.getName());
    Calendar now = Calendar.getInstance();
    now.set(Calendar.HOUR_OF_DAY, mCurrentAlarm.getHour());
    now.set(Calendar.MINUTE, mCurrentAlarm.getMinute());
    mFormattedTime = DateUtils.format(now.getTime(), DateUtils.TIME_FORMAT);
    mAlarmTimeRtv.setText(mFormattedTime);

    executeAlarm();
    registerReceiver();
  }

  /**
   * This method registers the receiver for the notification actions
   */
  private void registerReceiver() {
    IntentFilter stopIntentFilter = new IntentFilter(STOP_ALARM_ACTION);
    IntentFilter snoozeIntentFilter = new IntentFilter(STOP_ALARM_ACTION);
    registerReceiver(mNotificationReceiver, stopIntentFilter);
    registerReceiver(mNotificationReceiver, snoozeIntentFilter);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    unregisterReceiver(mNotificationReceiver);
    unbindService(mServiceConnection);
  }

  /**
   * This method establishes the status bar color
   */
  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  private void establishStatusBarColor() {
    if (!DeviceUtils.isLollipopOrGreater()) {
      return;
    }
    Window window = getWindow();

    // clear FLAG_TRANSLUCENT_STATUS flag:
    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

    // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

    // finally change the color
    window.setStatusBarColor(ViewUtils.getColor(this, R.color.material_blue_grey_700));
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
    startSoundService(songPath);
    displayNotification(false);
  }

  /**
   * This method initializes the sound service and binds it to the activity
   *
   * @param songPath
   *         Song path to be played
   */
  private void startSoundService(final String songPath) {
    Intent soundServiceIntent = new Intent(this, SoundService.class);
    mCurrentSong = songPath;
    bindService(soundServiceIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
  }

  /**
   * This method displays the notification in the status bar
   *
   * @param isPostAlarm
   *         Indicates if the notification is shown after alarm snooze/stop
   */
  private void displayNotification(boolean isPostAlarm) {
    NotificationCompat.Builder builder =
            new NotificationCompat.Builder(this).setSmallIcon(R.drawable.ic_alarm)
                    .setContentTitle(mCurrentAlarm.getName())
                    .setContentText(mFormattedTime)
                    .setAutoCancel(true)
                    .setOngoing(!isPostAlarm);
    if (!isPostAlarm) {
      addNotificationActions(builder);
    }
    mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    mNotificationManager.notify(mCurrentAlarm.getId(), builder.build());
  }

  /**
   * This method adds the notification actions, i.e., Stop alarm and Snooze alarm
   *
   * @param builder
   *         Notification builder
   */
  private void addNotificationActions(NotificationCompat.Builder builder) {
    PendingIntent stopPendingIntent = buildPendingIntent(STOP_ALARM_ACTION);
    PendingIntent snoozePendingIntent = buildPendingIntent(SNOOZE_ALARM_ACTION);
    builder.addAction(R.drawable.ic_alarm_off_white, getString(R.string.stop),
            stopPendingIntent);
    builder.addAction(R.drawable.ic_snooze_white, getString(R.string.snooze),
            snoozePendingIntent);
  }

  /**
   * This method builds the pending intent given an action
   *
   * @param action
   *         Action to be executed
   *
   * @return The pending intent
   */
  private PendingIntent buildPendingIntent(String action) {
    Intent intent = new Intent(action);
    intent.putExtra(IAlarmsService.ALARM_ID, mCurrentAlarm.getId());
    return PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
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
    displayNotification(true);
  }

  /**
   * This method stops the playing music and the device vibrations
   */
  private void stopPlaying() {
    if (mSoundService != null) {
      mSoundService.stopSound(mCurrentAlarm.getId());
    }
    mCurrentSong = null;

    // To make sure the alarm is always up to date
    mAlarmsService.setUpAlarm(mCurrentAlarm);
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
    mAlarmsService.snoozeAlarm(mCurrentAlarm.getId(), mAlarmDay);
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
