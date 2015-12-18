package com.ys.sm.service;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.*;
import com.ys.sm.MainActivity;
import com.ys.sm.R;
import com.ys.sm.receiver.ScreenReceiver;

import java.lang.ref.SoftReference;
import java.util.Timer;

/**
 * 监听手机屏幕变化 栈顶app变化
 * Created by ys on 2015/12/15.
 */
public class MonitorService extends Service {

    static class MyHandler extends Handler {
        MonitorService ms;

        public MyHandler(SoftReference<MonitorService> sf) {
            ms = sf.get();
        }

        @Override
        public void handleMessage(Message msg) {
            if (null != ms) {
                switch (msg.what) {
                    case 0:
                        ms.startMonitor();
                        try {
                            msg.replyTo.send(Message.obtain(null, 0));
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        break;
                    case 1:
                        ms.stopMonitor();
                        try {
                            msg.replyTo.send(Message.obtain(null, 1));
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        break;
                    default:
                        break;
                }
            }
        }
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onCreate() {
        super.onCreate();
        setForeService();
        startMonitor();
    }

    private void setForeService() {
        PendingIntent pi = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        Drawable d = getResources().getDrawable(R.drawable.ic_monitor_b);
        Notification n = new Notification();
        n.icon = R.drawable.ic_monitor_s;
        n.largeIcon = d != null ? ((BitmapDrawable) d).getBitmap() : null;
        n.tickerText = getString(R.string.monitor_start);
        n.setLatestEventInfo(this, getString(R.string.app_name), getString(R.string.dont_stop), pi);
        startForeground(11111, n);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    private void startMonitor() {
        startScreenMonitor();
        startAppMonitor();
    }

    private void startAppMonitor() {
        mt = new MonitorTask(this);
        mSr.setCallback(mt);
        timer.schedule(mt, 0, 500);
    }

    private void stopMonitor() {
        unregisterReceiver(mSr);
        mt.cancel();
    }

    MonitorTask    mt;
    ScreenReceiver mSr;
    Timer timer = new Timer();

    private void startScreenMonitor() {
        mSr = new ScreenReceiver();
        IntentFilter inf = new IntentFilter();
        inf.addAction(Intent.ACTION_SCREEN_ON);
        inf.addAction(Intent.ACTION_USER_PRESENT);
        inf.addAction(Intent.ACTION_SCREEN_OFF);
        inf.addAction(Intent.ACTION_SHUTDOWN);
        inf.addAction(Intent.ACTION_REBOOT);
        registerReceiver(mSr, inf);
    }

    MyHandler mHandler = new MyHandler(new SoftReference<>(this));

    @Override
    public IBinder onBind(Intent intent) {
        return new Messenger(mHandler).getBinder();
    }

}
