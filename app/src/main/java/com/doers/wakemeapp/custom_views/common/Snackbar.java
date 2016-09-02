package com.doers.wakemeapp.custom_views.common;

import android.graphics.Color;
import android.support.annotation.IntDef;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.TextView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author <a href="mailto:antonio-jimenez@accionplus.com">Antonio A. Jimenez N.</a>
 */
public class Snackbar {

  /** Snackbar's lengths **/
  public static final int LENGTH_SHORT = android.support.design.widget.Snackbar.LENGTH_SHORT;
  public static final int LENGTH_LONG = android.support.design.widget.Snackbar.LENGTH_LONG;
  public static final int LENGTH_INDEFINITE =
          android.support.design.widget.Snackbar.LENGTH_INDEFINITE;

  /**
   * Make a Snackbar to display a message
   * <p/>
   * <p>Snackbar will try and find a parent view to hold Snackbar's view from the value given
   * to {@code view}. Snackbar will walk up the view tree trying to find a suitable parent,
   * which is defined as a {@link CoordinatorLayout} or the window decor's content view,
   * whichever comes first.
   * <p/>
   * <p>Having a {@link CoordinatorLayout} in your view hierarchy allows Snackbar to enable
   * certain features, such as swipe-to-dismiss and automatically moving of widgets like
   * {@link FloatingActionButton}.
   *
   * @param view
   *         The view to find a parent from.
   * @param text
   *         The text to show.  Can be formatted text.
   * @param duration
   *         How long to display the message.  Either {@link #LENGTH_SHORT} or {@link #LENGTH_LONG}
   */
  @NonNull
  public static android.support.design.widget.Snackbar make(@NonNull View view,
                                                            @NonNull CharSequence text,
                                                            @Duration int duration) {
    android.support.design.widget.Snackbar snackbar =
            android.support.design.widget.Snackbar.make(view, text, duration);
    TextView tv =
            (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
    tv.setTextColor(Color.WHITE);
    return snackbar;
  }

  @NonNull
  public static android.support.design.widget.Snackbar make(@NonNull View view,
                                                            @StringRes int resId,
                                                            @Duration int duration) {
    return make(view, view.getResources().getText(resId), duration);
  }

  @IntDef({LENGTH_INDEFINITE, LENGTH_SHORT, LENGTH_LONG})
  @IntRange(from = 1)
  @Retention(RetentionPolicy.SOURCE)
  public @interface Duration {
  }

}