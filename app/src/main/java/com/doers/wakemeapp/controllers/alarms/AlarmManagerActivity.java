package com.doers.wakemeapp.controllers.alarms;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.doers.wakemeapp.R;
import com.doers.wakemeapp.common.model.alarms.Alarm;
import com.doers.wakemeapp.custom_views.decorations.InitialSpaceItemDecoration;

import java.util.ArrayList;
import java.util.List;

import roboguice.activity.RoboActionBarActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

/**
 * Alarm manager activity where all alarms are listed
 *
 * @author <a href="mailto:aajn88@gmail.com">Antonio A. Jimenez N.</a>
 */
@ContentView(R.layout.activity_alarm_manager)
public class AlarmManagerActivity extends RoboActionBarActivity {

    /** Alarms RecyclerView **/
    @InjectView(R.id.alarms_rv)
    private RecyclerView mAlarmsRv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAlarmsRv.setLayoutManager(new LinearLayoutManager(this));
        mAlarmsRv.addItemDecoration(new InitialSpaceItemDecoration(
                (int) getResources().getDimension(R.dimen.condensedVerticalMargin)));

        List<Alarm> alarms = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            alarms.add(new Alarm());
        }
        AlarmsAdapter adapter = new AlarmsAdapter(this, alarms);
        mAlarmsRv.setAdapter(adapter);
    }
}
