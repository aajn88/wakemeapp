package com.doers.wakemeapp.controllers.common;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.doers.wakemeapp.R;
import com.doers.wakemeapp.di.WakeMeAppApplication;
import com.doers.wakemeapp.di.components.DiComponent;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * This is the base activity which offers common configuration and methods for other activities
 *
 * @author <a href="mailto:aajn88@gmail.com">Antonio Jimenez</a>
 */
public abstract class BaseActivity extends AppCompatActivity {

    /** Toolbar **/
    @BindView(R.id.toolbar)
    @Nullable
    protected Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        injectComponent(((WakeMeAppApplication) getApplication()).getComponent());
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }
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

}
