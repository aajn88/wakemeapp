package com.doers.wakemeapp.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.doers.wakemeapp.R;

/**
 * Utility class that offers standard and common view methods
 *
 * @author <a href="mailto:aajn88@gmail.com">Antonio Jimenez</a>
 */
public final class ViewUtils {

  /** Private constructor to avoid instances **/
  private ViewUtils() {
  }

  /**
   * This method builds a standard dialog builder: A message and an OK button
   *
   * @param context
   *         Current context
   * @param stringRes
   *         String message to be shown
   *
   * @return Builder of the alter dialog
   */
  public static AlertDialog.Builder buildStandardDialogBuilder(Context context,
                                                               @StringRes int stringRes) {
    AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.StyledDialog);
    builder.setMessage(stringRes).setCancelable(true)
            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {

              }
            });
    return builder;
  }

  /**
   * This method gets the requested color based on the Device's OS level
   *
   * @param context
   *         App context
   * @param colorRes
   *         Color res
   *
   * @return Color int
   */
  @ColorInt
  public static int getColor(Context context, @ColorRes int colorRes) {
    int color;
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      color = context.getColor(colorRes);
    } else {
      color = context.getResources().getColor(colorRes);
    }
    return color;
  }

  /**
   * This method shows/hides the keyboard
   *
   * @param activity
   *         Current activity
   * @param show
   *         Show or hide
   */
  public static void showKeyboard(Activity activity, boolean show) {
    View view = activity.getCurrentFocus();
    if (show) {
      InputMethodManager imm =
              (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
      imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    } else {
      if (view != null) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
      }
    }
  }

  /**
   * Enables/Disables all child views in a view group.
   *
   * @param viewGroup
   *         the view group
   * @param enabled
   *         <code>true</code> to enable, <code>false</code> to disable
   *         the views.
   */
  public static void enableViewGroup(ViewGroup viewGroup, boolean enabled) {
    int childCount = viewGroup.getChildCount();
    for (int i = 0; i < childCount; i++) {
      View view = viewGroup.getChildAt(i);
      view.setEnabled(enabled);
      if (view instanceof ViewGroup) {
        enableViewGroup((ViewGroup) view, enabled);
      }
    }
  }

}
