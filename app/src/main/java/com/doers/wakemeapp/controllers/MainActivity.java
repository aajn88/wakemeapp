package com.doers.wakemeapp.controllers;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.doers.wakemeapp.R;
import com.doers.wakemeapp.controllers.alarms.AlarmManagerActivity;
import com.doers.wakemeapp.controllers.common.BaseActivity;
import com.doers.wakemeapp.di.components.DiComponent;
import com.doers.wakemeapp.utils.AnimationUtils;

import butterknife.BindView;

/**
 * Main activity where splash is loaded
 *
 * @author <a href="mailto:aajn88@gmail.com">Antonio A. Jimenez N.</a>
 */
public class MainActivity extends BaseActivity {

    /** Tag for logs **/
    private static final String TAG = MainActivity.class.getName();

    /** Constant for animation delay **/
    private static final int ANIM_DELAY = 500;

    /** Constant for animations duration **/
    private static final int ANIM_DURATION = 500;

    /** Delay for the splash view **/
    private static final int SPLASH_DELAY = 2500;

    /** Logo Image View **/
    @BindView(R.id.logo_iv)
    ImageView mLogoIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

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

    /**
     * Initializes the application
     */
    private void init() {
        mLogoIv.setAlpha(0.0f);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                animateLogo();
            }
        }, ANIM_DELAY);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                redirectActivity();
            }
        }, SPLASH_DELAY);
    }

    /**
     * This method redirects to the right Activity depending if the session is active or not
     */
    private void redirectActivity() {
        Intent alarmManagerIntent = new Intent(this, AlarmManagerActivity.class);
        startActivity(alarmManagerIntent);
    }

    /**
     * This method animate the logo. Fades and resizes the Logo view
     */
    private void animateLogo() {
        ObjectAnimator scaleXAnimation = AnimationUtils
                .createObjectAnimator(mLogoIv, "scaleX", 0.0F, 1.0F, ANIM_DURATION);
        ObjectAnimator scaleYAnimation = AnimationUtils
                .createObjectAnimator(mLogoIv, "scaleY", 0.0F, 1.0F, ANIM_DURATION);
        ObjectAnimator alphaAnimation = AnimationUtils
                .createObjectAnimator(mLogoIv, "alpha", 0.0F, 1.0F, ANIM_DURATION);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(scaleXAnimation).with(scaleYAnimation).with(alphaAnimation);
        animatorSet.start();
    }

}
