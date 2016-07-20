package com.doers.wakemeapp.controllers.alarms;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Spinner;

import com.doers.wakemeapp.R;
import com.doers.wakemeapp.common.model.alarms.Alarm;
import com.doers.wakemeapp.custom_views.font.RobotoTextView;

import java.util.ArrayList;
import java.util.List;

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

    /** Alarms **/
    private final List<Alarm> mAlarms;

    /**
     * Constructor
     *
     * @param context
     *         Application context
     */
    public AlarmsAdapter(Context context, List<Alarm> alarms) {
        mInflater = LayoutInflater.from(context);
        mAlarms = new ArrayList<>(alarms);
    }

    /**
     * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent an
     * item.
     * <p/>
     * This new ViewHolder should be constructed with a new View that can represent the items of the
     * given type. You can either create a new View manually or inflate it from an XML layout file.
     * <p/>
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
     * <p/>
     * Note that unlike {@link ListView}, RecyclerView will not call this method again if the
     * position of the item changes in the data set unless the item itself is invalidated or the new
     * position cannot be determined. For this reason, you should only use the <code>position</code>
     * parameter while acquiring the related data item inside this method and should not keep a copy
     * of it. If you need the position of an item later on (e.g. in a click listener), use {@link
     * ViewHolder#getAdapterPosition()} which will have the updated adapter position.
     * <p/>
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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

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
