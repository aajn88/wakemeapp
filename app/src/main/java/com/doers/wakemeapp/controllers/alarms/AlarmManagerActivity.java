package com.doers.wakemeapp.controllers.alarms;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.doers.wakemeapp.R;
import com.doers.wakemeapp.business.services.api.IAlarmsService;
import com.doers.wakemeapp.common.model.alarms.Alarm;
import com.doers.wakemeapp.controllers.common.BaseActivity;
import com.doers.wakemeapp.controllers.playlists.PlaylistsManagerActivity;
import com.doers.wakemeapp.custom_views.decorations.InitialSpaceItemDecoration;
import com.doers.wakemeapp.di.components.DiComponent;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Alarm manager activity where all alarms are listed
 *
 * @author <a href="mailto:aajn88@gmail.com">Antonio A. Jimenez N.</a>
 */
public class AlarmManagerActivity extends BaseActivity implements View.OnClickListener {

    /** Alarms RecyclerView **/
    @BindView(R.id.alarms_rv)
    RecyclerView mAlarmsRv;

    /** Add playlist FAB **/
    @BindView(R.id.add_playlist_fab)
    FloatingActionButton mAddPlaylistFab;

    /** Add alarm FAB **/
    @BindView(R.id.add_alarm_fab)
    FloatingActionButton mAddAlarmFab;

    /** Floating Action Menu **/
    @BindView(R.id.alarms_fam)
    FloatingActionMenu mAlarmsFam;

    /** No alarms TextView **/
    @BindView(R.id.no_alarms_tv)
    TextView mNoAlarmsTv;

    /** Alarms Service **/
    @Inject
    IAlarmsService mAlarmsService;

    /** Alarms adapter **/
    private AlarmsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_manager);

        setTitle(R.string.app_name);

        mAddPlaylistFab.setOnClickListener(this);
        mAddAlarmFab.setOnClickListener(this);

        mAlarmsRv.setLayoutManager(new LinearLayoutManager(this));
        mAlarmsRv.addItemDecoration(new InitialSpaceItemDecoration(
                (int) getResources().getDimension(R.dimen.condensedVerticalMargin)));

        List<Alarm> alarms = mAlarmsService.getAllAlarms();
        if (alarms.isEmpty()) {
            mNoAlarmsTv.setVisibility(View.VISIBLE);
        }
        mAdapter = new AlarmsAdapter(this, alarms);
        mAlarmsRv.setAdapter(mAdapter);
    }

    /**
     * Injection component. This should be done if there are fields to be injected
     *
     * @param diComponent
     *         Dependency injection
     */
    @Override
    protected void injectComponent(DiComponent diComponent) {
        diComponent.inject(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_playlist_fab:
                PlaylistsManagerActivity.startActivity(this);
                break;
            case R.id.add_alarm_fab:
                mAdapter.addAlarm();
                mNoAlarmsTv.setVisibility(View.GONE);
                break;
        }
    }
}