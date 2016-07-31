package com.doers.wakemeapp.utils;

import android.content.Context;
import android.content.DialogInterface;
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

}
