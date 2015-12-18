package com.ys.sm;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.*;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.ys.sm.adapter.AdapterAR;
import com.ys.sm.dao.SLDbManager;
import com.ys.sm.model.AppRecordModel;
import com.ys.sm.model.SLDayModel;
import com.ys.sm.service.MonitorService;
import com.ys.sm.utils.Tool;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements View.OnClickListener {


    static class MyHandler extends Handler {
        MainActivity activity;

        public MyHandler(SoftReference<MainActivity> sf) {
            activity = sf.get();
        }

        @Override
        public void handleMessage(Message msg) {
            if (activity == null)
                return;
            switch (msg.what) {
                case 0:
//                    activity.switchBtnStatus();
                    break;
                case 1:
//                    activity.switchBtnStatus();
                    break;
                default:
                    break;
            }
        }
    }

    private Button   btnToday;
    private Button   btnAll;
    private TextView tvTotal;
    private TextView tvPresent;
    private TextView tv_present_count_less;
    private TextView tvOff;
    private ListView mLv;
    boolean isStart;

    AdapterAR mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //        bindService(new Intent(this, MonitorService.class), serviceConnection, BIND_AUTO_CREATE);
        startService(new Intent(this, MonitorService.class));
        initView();

    }

    @Override
    protected void onResume() {
        super.onResume();
       loadToday();
    }

    private void loadToday() {
        btnToday.setBackgroundResource(R.color.colorPrimary);
        btnAll.setBackgroundResource(android.R.color.transparent);
        long today = Tool.getDay(System.currentTimeMillis());
        setViewData(today);
        setListView(today);
    }
    private void loadAll(){
        btnAll.setBackgroundResource(R.color.colorPrimary);
        btnToday.setBackgroundResource(android.R.color.transparent);
        setViewData(0);
        setListView(0);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_today:
               loadToday();
                break;
            case R.id.btn_all:
                loadAll();
                break;
        }
    }

    private void setListView(long today) {
        List<AppRecordModel> list = SLDbManager.getInstance(this).getAppRecordModes(today);
        if (list != null) {
            mAdapter.setListAndNotify(list);
        }
    }

    private void setViewData(long today) {
        //        List<SLDayModel> list = SLDbManager.getInstance(this).getLightInfos(today);
        int countlight = SLDbManager.getInstance(this).getLightCount(today);
        int countpresent = SLDbManager.getInstance(this).getUserPresentCount(today) + 1;
        int count_off = SLDbManager.getInstance(this).getLightAutoOffCOunt(today);


        List<SLDayModel> lesses = SLDbManager.getInstance(this).getPresentInfos(today);
        int count = 0;
        if (lesses != null) {
            for (int i = 0, j = lesses.size(); i < j; i++) {
                SLDayModel m = lesses.get(i);
                if (m != null) {
                    if (m.duation < 60 * 1000) {
                        count += 1;
                    }
                }
            }
        }


        String light = getString(R.string.light_times) + ":" + countlight;
        String prsent = getString(R.string.light_times_present) + ":" + countpresent;
        String present_less = getString(R.string.light_times_present_less) + ":" + count;
        String off = getString(R.string.light_times_off) + ":" + count_off;
        tvTotal.setText(light);
        tvPresent.setText(prsent);
        tv_present_count_less.setText(present_less);
        tvOff.setText(off);

    }

    private void initView() {
        btnToday = (Button) findViewById(R.id.btn_today);
        btnAll = (Button) findViewById(R.id.btn_all);
        mLv = (ListView) findViewById(R.id.lv);
        btnToday.setOnClickListener(this);
        btnAll.setOnClickListener(this);
        tvTotal = (TextView) findViewById(R.id.tv_light_count);
        tvPresent = (TextView) findViewById(R.id.tv_present_count);
        tv_present_count_less = (TextView) findViewById(R.id.tv_present_count_less);
        tvOff = (TextView) findViewById(R.id.tv_off_count);
        mAdapter = new AdapterAR(this, new ArrayList<AppRecordModel>());
        mLv.setAdapter(mAdapter);
    }

    Messenger messenger;
    Messenger mMsger = new Messenger(new MyHandler(new SoftReference<>(this)));

    ServiceConnection serviceConnection = new ServiceConnection() {


        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            messenger = new Messenger(service);

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            //            startMonitor();
        }
    };



//    private void switchBtnStatus() {
//        if (isStart) {
//            isStart = false;
//            btnToday.setText(R.string.start);
//        } else {
//            isStart = true;
//            btnToday.setText(R.string.stop);
//        }
//    }

    private void startMonitor() {
        Message msg = Message.obtain(null, 0);
        msg.replyTo = mMsger;
        try {
            messenger.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void stopMonitor() {
        Message msg = Message.obtain(null, 1);
        msg.replyTo = mMsger;
        try {
            messenger.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

}
