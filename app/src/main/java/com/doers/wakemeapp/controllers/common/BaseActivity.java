package com.doers.wakemeapp.controllers.common;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.doers.wakemeapp.R;
import com.doers.wakemeapp.business.services.api.IFirebaseAnalyticsService;
import com.doers.wakemeapp.business.services.constants.FirebaseEvent;
import com.doers.wakemeapp.controllers.alarms.AlarmManagerActivity;
import com.doers.wakemeapp.controllers.playlists.PlaylistsManagerActivity;
import com.doers.wakemeapp.di.WakeMeAppApplication;
import com.doers.wakemeapp.di.components.DiComponent;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * This is the base activity which offers common configuration and methods for other activities
 *
 * @author <a href="mailto:aajn88@gmail.com">Antonio Jimenez</a>
 */
public abstract class BaseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

  /** Toolbar **/
  @BindView(R.id.toolbar)
  @Nullable
  protected Toolbar mToolbar;

  /** Drawer layout **/
  @BindView(R.id.drawer_layout)
  @Nullable
  protected DrawerLayout mDrawerLayout;

  /** Navigation view **/
  @BindView(R.id.navigation_view)
  @Nullable
  protected NavigationView mNavigationView;

  /** Firebase analytics service **/
  @Inject
  protected IFirebaseAnalyticsService mFirebaseAnalyticsService;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    injectComponent(((WakeMeAppApplication) getApplication()).getInjector());
  }

  @Override
  public void setContentView(@LayoutRes int layoutResID) {
    super.setContentView(layoutResID);
    ButterKnife.bind(this);
    setUpActionBar();
  }

  /**
   * This method sets up the action bar and the navigation drawer
   */
  private void setUpActionBar() {
    if (mToolbar != null) {
      setSupportActionBar(mToolbar);
    }
    if (mNavigationView == null || mDrawerLayout == null) {
      return;
    }

    initDrawerHeader();

    ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open,
            R.string.navigation_drawer_close);
    mDrawerLayout.addDrawerListener(toggle);
    toggle.syncState();

    mNavigationView.setNavigationItemSelectedListener(this);
  }

  /**
   * This method initializes the drawer header
   */
  private void initDrawerHeader() {
    if (mNavigationView == null) {
      return;
    }
    View headerView =
            getLayoutInflater().inflate(R.layout.nav_header_drawer, mNavigationView, false);
    mNavigationView.addHeaderView(headerView);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_base, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == R.id.logo_item) {
      mFirebaseAnalyticsService.logEvent(FirebaseEvent.TOOLBAR_WAKE_ME_APP_ICON_CLICKED);
    }
    return super.onOptionsItemSelected(item);
  }

  /**
   * Injection component. This should be done if there are fields to be injected
   *
   * @param diComponent
   *         Dependency injection
   */
  protected abstract void injectComponent(DiComponent diComponent);

  /**
   * This method gets the Activity (i.e. this)
   *
   * @return The current Activity
   */
  protected Activity getActivity() {
    return this;
  }

  /**
   * This method shows a dialog to the user confirming to exit the activity without saving the
   * progress
   *
   * @param context
   *         Current context
   * @param titleRes
   *         Title to be shown. Could be null
   * @param msgRes
   *         Message to be shown. Could be null
   * @param confirmRes
   *         Confirmation string for confirmation button. Cannot be null
   */
  protected void confirmExit(Context context, @StringRes Integer titleRes,
                             @StringRes Integer msgRes, @StringRes int confirmRes) {
    AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.StyledDialog);
    if (titleRes != null) {
      builder.setTitle(titleRes);
    }
    if (msgRes != null) {
      builder.setMessage(msgRes);
    }
    builder.setCancelable(true).setNegativeButton(R.string.cancel, null)
            .setPositiveButton(confirmRes, new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                finish();
              }
            });
    AlertDialog alert = builder.create();
    alert.show();
  }

  @Override
  public boolean onNavigationItemSelected(@NonNull MenuItem item) {

    switch (item.getItemId()) {
      case R.id.my_alarms_item:
        AlarmManagerActivity.startActivity(this);
        break;
      case R.id.my_playlists_item:
        PlaylistsManagerActivity.startActivity(this);
        break;
      case R.id.log_out_item:
        break;
      case R.id.about_item:
        break;
    }

    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    drawer.closeDrawer(GravityCompat.START);
    return true;
  }
}
