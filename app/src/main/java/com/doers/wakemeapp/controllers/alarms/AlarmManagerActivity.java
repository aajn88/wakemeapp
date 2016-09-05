package com.doers.wakemeapp.controllers.alarms;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.TextView;

import com.doers.wakemeapp.R;
import com.doers.wakemeapp.business.services.api.IAlarmsService;
import com.doers.wakemeapp.business.services.constants.FirebaseEvent;
import com.doers.wakemeapp.common.model.alarms.Alarm;
import com.doers.wakemeapp.controllers.common.BaseActivity;
import com.doers.wakemeapp.controllers.playlists.PlaylistsManagerActivity;
import com.doers.wakemeapp.custom_views.common.Snackbar;
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

  /**
   * Callback for Snackbar undo
   */
  private final android.support.design.widget.Snackbar.Callback mCallback =
          new android.support.design.widget.Snackbar.Callback() {
            @Override
            public void onDismissed(android.support.design.widget.Snackbar snackbar, int event) {
              mAdapter.confirmDeletion();
              checkAlarmsCount();
            }

            @Override
            public void onShown(android.support.design.widget.Snackbar snackbar) {
            }
          };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_alarm_manager);

    setTitle(R.string.app_name);

    mAddPlaylistFab.setOnClickListener(this);
    mAddAlarmFab.setOnClickListener(this);
    mNoAlarmsTv.setOnClickListener(this);

    setUpRecyclerView();
  }

  @Override
  protected void onResume() {
    super.onResume();
    mAdapter.refreshAlarms();
    checkAlarmsCount();
  }

  /**
   * This method checks the alarms count. If there are no alarms, then a message is displayed.
   * Otherwise this message will kept hidden
   */
  private void checkAlarmsCount() {
    if (mAdapter == null) {
      return;
    }

    mNoAlarmsTv.setVisibility(mAdapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
  }

  /**
   * This method sets up the recycler view
   */
  private void setUpRecyclerView() {
    mAlarmsRv.setLayoutManager(new LinearLayoutManager(this));
    mAlarmsRv.addItemDecoration(new InitialSpaceItemDecoration(
            (int) getResources().getDimension(R.dimen.condensedVerticalMargin)));

    List<Alarm> alarms = mAlarmsService.getAllAlarms();
    if (alarms.isEmpty()) {
      mNoAlarmsTv.setVisibility(View.VISIBLE);
    }
    mAdapter = new AlarmsAdapter(this, alarms);
    mAlarmsRv.setAdapter(mAdapter);

    ItemTouchHelper.SimpleCallback callback =
            new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
              @Override
              public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                    RecyclerView.ViewHolder target) {
                return false;
              }

              @Override
              public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                mAdapter.partiallyDeleteAlarm(viewHolder.getAdapterPosition());
                Snackbar.make(mAlarmsRv, R.string.alarm_deleted, Snackbar.LENGTH_LONG)
                        .setAction(R.string.undo, AlarmManagerActivity.this)
                        .setCallback(mCallback)
                        .show();
                checkAlarmsCount();
              }
            };

    ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
    itemTouchHelper.attachToRecyclerView(mAlarmsRv);
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
        mAlarmsFam.close(true);
        break;
      case R.id.add_alarm_fab:
        addAlarm();
        mFirebaseAnalyticsService.logEvent(FirebaseEvent.CREATE_ALARM_CLICKING_FAB);
        break;
      case R.id.no_alarms_tv:
        addAlarm();
        mFirebaseAnalyticsService.logEvent(FirebaseEvent.CREATE_ALARM_CLICKING_TEXT);
        break;
      case R.id.snackbar_action:
        mAdapter.cancelDeletion();
        break;
    }
    checkAlarmsCount();
  }

  /**
   * This method adds an alarm
   */
  private void addAlarm() {
    mAdapter.addAlarm();
    mNoAlarmsTv.setVisibility(View.GONE);
    mAlarmsRv.scrollToPosition(mAdapter.getItemCount() - 1);
    mAlarmsFam.close(true);
  }

  @Override
  public void onBackPressed() {
    if (mAlarmsFam.isOpened()) {
      mAlarmsFam.close(true);
    } else {
      finishAffinity();
    }
  }
}
