package com.ys.sm.utils;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import java.util.List;

/**
 * app utils
 * Created by ys on 2015/12/17.
 */
public class Utils {


    public static String getPachageName (Context context,String pName){
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo pi = pm.getPackageInfo(pName, 0);
            return pm.getApplicationLabel(pi.applicationInfo).toString();
//            return pi.applicationInfo.name;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String getActivePackagesCompat(ActivityManager am) {
        try {
            final List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            if (taskInfo != null && taskInfo.size() > 0) {
                ActivityManager.RunningTaskInfo runningTaskInfo = taskInfo.get(0);
                if (runningTaskInfo != null) {
                    final ComponentName componentName = taskInfo.get(0).topActivity;
                    if (componentName == null) {
                        return null;
                    } else {
                        return componentName.getPackageName();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String getActivePackages(ActivityManager am) {
        String activePackage = null;
        try {
            final List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : processInfos) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    if (processInfo.pkgList.length > 0) {
                        activePackage = processInfo.pkgList[0];
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return activePackage;
    }

    public static String getCurrentAppPname(Context context) {
        String activePackage = null;

        try {
            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            if (Build.VERSION.SDK_INT >= 21) {
                activePackage = getActivePackages(am);
            } else {
                activePackage = getActivePackagesCompat(am);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return activePackage;
    }
}
