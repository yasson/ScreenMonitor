package com.ys.sm;

import android.app.Application;
import com.ys.sm.utils.ImageLoader;

/**
 * app
 * Created by ys on 2015/12/18.
 */
public class SMApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ImageLoader.getInstance().init(this);
    }
}
