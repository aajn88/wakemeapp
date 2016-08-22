package com.doers.wakemeapp.controllers.alarms;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.doers.wakemeapp.R;
import com.doers.wakemeapp.business.services.api.IAlarmsService;
import com.doers.wakemeapp.business.services.api.IPlaylistsService;
import com.doers.wakemeapp.common.model.alarms.Alarm;
import com.doers.wakemeapp.common.model.audio.Playlist;
import com.doers.wakemeapp.common.utils.DateUtils;
import com.doers.wakemeapp.custom_views.common.TimePickerFragment;
import com.doers.wakemeapp.custom_views.font.RobotoTextView;
import com.doers.wakemeapp.di.WakeMeAppApplication;
import com.doers.wakemeapp.utils.ViewUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

/**
 * Adapter where all stored alarms will be displayed
 *
 * @author <a href="mailto:aajn88@gmail.com">Antonio Jimenez</a>
 */
public class AlarmsAdapter extends RecyclerView.Adapter {

  /** Weekdays' IDs **/
  private static final int[] WEEKDAYS_IDS = {R.id.monday_rtv, R.id.tuesday_rtv,
          R.id.wednesday_rtv, R.id.thursday_rtv, R.id.friday_rtv, R.id.saturday_rtv,
          R.id.sunday_rtv};

  /** Layout Inflater **/
  private final LayoutInflater mInflater;

  /** Current context **/
  private final Context mContext;

  /** Alarms **/
  private final List<Alarm> mAlarms;

  /** Alarms service **/
  @Inject
  IAlarmsService mAlarmsService;

  /** Playlist Service **/
  @Inject
  IPlaylistsService mPlaylistsService;

  /** List of available playlists **/
  private List<Playlist> mPlaylists;

  /**
   * Constructor
   *
   * @param activity
   *         Current Activity
   */
  public AlarmsAdapter(Activity activity, List<Alarm> alarms) {
    mContext = activity;
    mInflater = LayoutInflater.from(activity);
    mAlarms = new ArrayList<>(alarms);
    ((WakeMeAppApplication) activity.getApplication()).getComponent().inject(this);
  }

  /**
   * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent an
   * item.
   * <p>
   * This new ViewHolder should be constructed with a new View that can represent the items of the
   * given type. You can either create a new View manually or inflate it from an XML layout file.
   * <p>
   * The new ViewHolder will be used to display items of the adapter using {@link
   * #onBindViewHolder(ViewHolder, int, List)}. Since it will be re-used to display different
   * items in the data set, it is a good idea to cache references to sub views of the View to
   * avoid unnecessary {@link View#findViewById(int)} calls.
   *
   * @param parent
   *         The ViewGroup into which the new View will be added after it is bound to an adapter
   *         position.
   * @param viewType
   *         The view type of the new View.
   *
   * @return A new ViewHolder that holds a View of the given view type.
   *
   * @see #getItemViewType(int)
   * @see #onBindViewHolder(ViewHolder, int)
   */
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new AlarmHolder(mInflater.inflate(R.layout.alarm_layout, parent, false));
  }

  /**
   * Called by RecyclerView to display the data at the specified position. This method should
   * update the contents of the {@link ViewHolder#itemView} to reflect the item at the given
   * position.
   * <p>
   * Note that unlike {@link ListView}, RecyclerView will not call this method again if the
   * position of the item changes in the data set unless the item itself is invalidated or the new
   * position cannot be determined. For this reason, you should only use the <code>position</code>
   * parameter while acquiring the related data item inside this method and should not keep a copy
   * of it. If you need the position of an item later on (e.g. in a click listener), use {@link
   * ViewHolder#getAdapterPosition()} which will have the updated adapter position.
   * <p>
   * Override {@link #onBindViewHolder(ViewHolder, int, List)} instead if Adapter can handle
   * effcient partial bind.
   *
   * @param holder
   *         The ViewHolder which should be updated to represent the contents of the item at the
   *         given position in the data set.
   * @param position
   *         The position of the item within the adapter's data set.
   */
  @Override
  public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
    final AlarmHolder vh = (AlarmHolder) holder;
    final Alarm alarm = mAlarms.get(position);

    final boolean[] scheduledDays = alarm.getScheduledDays();
    for (int i = 0; i < scheduledDays.length; i++) {
      updateDay(scheduledDays[i], vh.mDays[i]);
      final int finalI = i;
      vh.mDays[i].setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          scheduledDays[finalI] = !scheduledDays[finalI];
          updateDay(scheduledDays[finalI], vh.mDays[finalI]);
          updateAlarm(holder.getAdapterPosition());
        }
      });
    }
    updateTime(vh, alarm);
    vh.mTime.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        TimePickerFragment timePicker = TimePickerFragment
                .getInstance(alarm.getHour(), alarm.getMinute(),
                        new TimePickerDialog.OnTimeSetListener() {
                          @Override
                          public void onTimeSet(TimePicker timePicker, int hour,
                                                int minute) {
                            alarm.setHour(hour);
                            alarm.setMinute(minute);
                            updateTime(vh, alarm);
                          }
                        });
        timePicker.show(((FragmentActivity) mContext).getSupportFragmentManager(),
                "timePicker");
      }
    });
    setUpPlaylists(vh, alarm);
  }

  private void updateTime(AlarmHolder vh, Alarm alarm) {
    Calendar c = Calendar.getInstance();
    c.set(Calendar.HOUR_OF_DAY, alarm.getHour());
    c.set(Calendar.MINUTE, alarm.getMinute());
    vh.mTime.setText(DateUtils.format(c.getTime(), DateUtils.TIME_FORMAT));
  }

  /**
   * This method sets up the playlists for the current view holder and alarm
   *
   * @param vh
   *         View Holder
   * @param alarm
   *         Alarm
   */
  private void setUpPlaylists(final AlarmHolder vh, final Alarm alarm) {
    List<Playlist> playlists = getPlaylists();
    vh.mPlaylists.setAdapter(
            new ArrayAdapter<Playlist>(mContext, R.layout.list_item_simple, R.id.text_rtv,
                    playlists));
    int foundIndex = -1;
    vh.mPlaylists.setOnItemSelectedListener(null);
    for (int i = 0; i < playlists.size() && foundIndex == -1; i++) {
      Playlist playlist = playlists.get(i);
      if (alarm.getPlaylist() == null ||
              playlist.getId().equals(alarm.getPlaylist().getId())) {
        storePlaylist(alarm, playlist);
        foundIndex = i;
      }
    }
    vh.mPlaylists.setSelection(foundIndex);
    vh.mPlaylists.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        storePlaylist(alarm, (Playlist) vh.mPlaylists.getItemAtPosition(i));
      }

      @Override
      public void onNothingSelected(AdapterView<?> adapterView) {
      }
    });
  }

  /**
   * This method stores the playlist to the given alarm. The playlist is attached to the alarm
   *
   * @param alarm
   *         Alarm owner of the playlist
   * @param playlist
   *         Playlist to be stored and attached to the alarm
   */
  private void storePlaylist(Alarm alarm, Playlist playlist) {
    alarm.setPlaylist(playlist);
    mAlarmsService.createOrUpdateAlarm(alarm);
  }

  private void updateDay(boolean scheduledDay, TextView tv) {
    int colorRes = scheduledDay ? R.color.colorAccent : R.color.black_38_percent;
    tv.setTextColor(ViewUtils.getColor(mContext, colorRes));
  }

  /**
   * This method adds an alarm
   */
  public void addAlarm() {
    mAlarms.add(mAlarmsService.getDefaultAlarm());
    int lastItem = mAlarms.size() - 1;
    notifyItemInserted(lastItem);
    updateAlarm(lastItem);
  }

  /**
   * This method updates the alarm given its index
   *
   * @param i
   *         Alarm index to be updated
   */
  private void updateAlarm(int i) {
    mAlarmsService.createOrUpdateAlarm(mAlarms.get(i));
  }

  /**
   * Returns the total number of items in the data set hold by the adapter.
   *
   * @return The total number of items in this adapter.
   */
  @Override
  public int getItemCount() {
    return mAlarms.size();
  }

  /**
   * This method gets the available playlists
   *
   * @return List of available playlists
   */
  public List<Playlist> getPlaylists() {
    if (mPlaylists == null) {
      mPlaylists = mPlaylistsService.getAllPlaylists();
    }
    return mPlaylists;
  }

  /**
   * Alarm ViewHolder
   */
  private class AlarmHolder extends ViewHolder {

    /** Alarm title **/
    RobotoTextView mTitle;

    /** Alarm time **/
    RobotoTextView mTime;

    /** Days **/
    RobotoTextView[] mDays;

    /** Alarm playlist spinner **/
    Spinner mPlaylists;

    /** Alarm enable **/
    SwitchCompat mEnable;

    /**
     * ViewHolder
     *
     * @param itemView
     *         View item
     */
    public AlarmHolder(View itemView) {
      super(itemView);

      mTitle = (RobotoTextView) itemView.findViewById(R.id.title_rtv);
      mTime = (RobotoTextView) itemView.findViewById(R.id.time_rtv);
      loadWeekdays(itemView);
      mPlaylists = (Spinner) itemView.findViewById(R.id.playlists_sp);
      mEnable = (SwitchCompat) itemView.findViewById(R.id.enable_sw);
    }

    /**
     * This method loads the days views
     *
     * @param itemView
     *         Item view
     */
    private void loadWeekdays(View itemView) {
      mDays = new RobotoTextView[WEEKDAYS_IDS.length];
      for (int i = 0; i < WEEKDAYS_IDS.length; i++) {
        mDays[i] = (RobotoTextView) itemView.findViewById(WEEKDAYS_IDS[i]);
      }
    }
  }

}
