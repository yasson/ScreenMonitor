package com.ys.sm.service;

import android.content.Context;
import android.util.Log;
import com.ys.sm.dao.SLDbManager;
import com.ys.sm.receiver.ScreenReceiver;
import com.ys.sm.utils.Tool;
import com.ys.sm.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

/**
 * 监听栈顶应用
 * Created by ys on 2015/12/15.
 */
public class MonitorTask extends TimerTask implements ScreenReceiver.IScreenChange {

    Context context;
    String lastPackageName = "";
    List<String> packages;
    long         startTime;

    public MonitorTask(Context context) {
        this.context = context;
        packages = new ArrayList<>();
        startTime = System.currentTimeMillis();
    }

    @Override
    public void run() {
        String pName = Utils.getCurrentAppPname(context);
        if (!lastPackageName.equals(pName)) {
            startTime = insertOrUpdateApp(context);
            lastPackageName = pName==null?"":pName;
            packages.add(pName);
            Log.i("ys", "MonitorTask pname changed :" + pName);
        }
    }


    @Override
    public void onUserPresent(Context context) {
        String name = Utils.getCurrentAppPname(context);
        lastPackageName = null==name?"":name;
        startTime = System.currentTimeMillis();

    }

    @Override
    public void onScreenOff(Context context) {
        insertOrUpdateApp(context);
    }

    private long insertOrUpdateApp(Context context) {
        long time = System.currentTimeMillis();
        long day = Tool.getDay(time);
        long duation = time - startTime;
        SLDbManager.getInstance(context).insertOrUpdateApp(day, lastPackageName, duation);
        return time;
    }
}
