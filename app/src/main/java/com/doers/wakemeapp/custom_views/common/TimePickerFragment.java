package com.doers.wakemeapp.custom_views.common;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;

import java.util.Calendar;

/**
 * Time picker fragment
 *
 * @author <a href="mailto:aajn88@gmail.com">Antonio Jimenez</a>
 */
public class TimePickerFragment extends DialogFragment {

    /** Hour argument **/
    private static final String HOUR_ARG = "hour";

    /** Minute argument **/
    private static final String MINUTE_ARG = "hour";

    /** Time listener **/
    private TimePickerDialog.OnTimeSetListener mListener;

    /**
     * This method creates an instance of the fragment with the given default hour and minute
     *
     * @param hour
     *         Default hour
     * @param minute
     *         Default minute
     *
     * @return {@link TimePickerFragment} instance
     */
    public static TimePickerFragment getInstance(int hour, int minute,
                                                 TimePickerDialog.OnTimeSetListener listener) {
        TimePickerFragment instance = new TimePickerFragment();
        instance.mListener = listener;
        Bundle extras = new Bundle();
        extras.putInt(HOUR_ARG, hour);
        extras.putInt(MINUTE_ARG, minute);
        instance.setArguments(extras);
        return instance;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof TimePickerDialog.OnTimeSetListener) {
            mListener = (TimePickerDialog.OnTimeSetListener) context;
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        Bundle extras = getArguments();
        if (extras != null) {
            hour = extras.getInt(HOUR_ARG);
            minute = extras.getInt(MINUTE_ARG);
        }

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), mListener, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

}
