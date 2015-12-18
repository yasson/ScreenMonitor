package com.ys.sm.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.ys.sm.dao.SLDbManager;
import com.ys.sm.service.MonitorService;

import static com.ys.sm.model.SLDayModel.*;

/**
 * 监控屏幕变化
 * 1.亮
 * 2.灭
 * 3.解锁
 * <p>
 * Created by ys on 2015/12/15.
 */
public class ScreenReceiver extends BroadcastReceiver {

    static final int STATUS_ON      = 0;
    static final int STATUS_PRESENT = 1;
    static final int STATUS_OFF     = 2;

    long onTime;
    long status = STATUS_OFF;

    private IScreenChange mISC;

    public void setCallback(IScreenChange screenChange) {
        this.mISC = screenChange;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null)
            return;
        Log.i("ys", "onReceive:" + intent.getAction());
        switch (intent.getAction()) {

            case Intent.ACTION_SCREEN_ON:
                if (status == STATUS_OFF) {
                    status = STATUS_ON;
                }
                SLDbManager.getInstance(context).insertLight(TYPE_LIGHT);
                break;
            case Intent.ACTION_USER_PRESENT:
                if (null != mISC) {
                    mISC.onUserPresent(context);
                }
                status = STATUS_PRESENT;
                break;
            case Intent.ACTION_SCREEN_OFF:
            case Intent.ACTION_SHUTDOWN:
            case Intent.ACTION_REBOOT:
                if (status == STATUS_ON) {
                    SLDbManager.getInstance(context).insertLight(TYPE_OFF);
                } else if (status == STATUS_PRESENT) {
                    SLDbManager.getInstance(context).insertLight(TYPE_LOCK);
                }
                status = STATUS_OFF;
                if (null != mISC) {
                    mISC.onScreenOff(context);
                }
                break;
            case Intent.ACTION_BOOT_COMPLETED:
                context.startService(new Intent(context, MonitorService.class));
                break;
        }
    }

    public interface IScreenChange {
        void onUserPresent(Context context);

        void onScreenOff(Context context);
    }
}
