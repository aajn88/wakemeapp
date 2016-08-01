package com.doers.wakemeapp.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;

import com.doers.wakemeapp.R;

/**
 * Utility class that offers standard and common view methods
 *
 * @author <a href="mailto:aajn88@gmail.com">Antonio Jimenez</a>
 */
public final class ViewUtils {

    /** Private constructor to avoid instances **/
    private ViewUtils() {}

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

}
