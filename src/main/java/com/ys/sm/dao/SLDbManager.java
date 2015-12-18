package com.ys.sm.dao;

import android.content.Context;
import android.text.TextUtils;
import com.ys.sm.model.AppRecordModel;
import com.ys.sm.model.SLDayModel;
import com.ys.sm.utils.Tool;

import java.util.List;

import static com.ys.sm.model.SLDayModel.*;

/**
 * 监控数据的正删改查
 * Created by ys on 2015/12/16.
 */
public class SLDbManager {

    static private SLDbManager instance;

    private SLDao     mSLDao;
    private AppRecDao mARDao;

    private SLDbManager(Context context) {
        mSLDao = new SLDao(context);
        mARDao = new AppRecDao(context);
    }

    public static SLDbManager getInstance(Context context) {
        if (instance == null) {
            instance = new SLDbManager(context);
        }
        return instance;
    }


    public List<AppRecordModel> getAppRecordModes(long day) {
        if (day==0){
            return mARDao.getAppRecordsOfAll();
        }
        return mARDao.getAppRecordsOfDay(day);
    }

    public void insertLight(int type) {
        long time = System.currentTimeMillis();
        SLDayModel model = new SLDayModel();
        model.day = Tool.getDay(time);
        model.type = type;
        if (type == SLDayModel.TYPE_LIGHT) {
            model.start = time;
        } else {
            model.start = mSLDao.getLastLightTime();
            model.end = time;
            model.duation = time - model.start;
            mSLDao.updateLightEndTime(time);
        }
        mSLDao.insert(model);
    }
    public void insertOrUpdateApp(long day, String pName, long duationInc) {
        if (TextUtils.isEmpty(pName)){
            return;
        }
        if (mARDao.hasAppInDay(day, pName)) {
            mARDao.updateAppRecords(day, pName, duationInc);
        } else {
            mARDao.insertApp(day, pName, duationInc);
        }
    }

    public List<SLDayModel> getLightInfos(long day) {
        if (day==0){
            return mSLDao.getInfoOfAll(TYPE_LIGHT);
        }
        return mSLDao.getInfoOfDay(TYPE_LIGHT, day);

    }

    public List<SLDayModel> getPresentInfos(long day) {
        if (day==0){
            return mSLDao.getInfoOfAll(TYPE_LOCK);
        }
        return mSLDao.getInfoOfDay(TYPE_LOCK, day);

    }

    public List<SLDayModel> getOffInfos(long day) {
        if (day==0){
            return mSLDao.getInfoOfAll(TYPE_OFF);
        }
        return mSLDao.getInfoOfDay(TYPE_OFF, day);
    }


    public int getLightTime(long day) {
        if (day==0){
            return   mSLDao.getRemainTimeOfAll(TYPE_LIGHT) / 1000;
        }
        return mSLDao.getRemainTimeOfDay(TYPE_LIGHT, day) / 1000;
    }

    public int getPresentTime(long day) {
        if (day==0){
            return   mSLDao.getRemainTimeOfAll(TYPE_LOCK) / 1000;
        }

        return mSLDao.getRemainTimeOfDay(TYPE_LOCK, day) / 1000;
    }

    public int getOffTime(long day) {
        if (day==0){
            return   mSLDao.getRemainTimeOfAll(TYPE_OFF) / 1000;
        }
        return mSLDao.getRemainTimeOfDay(TYPE_OFF, day) / 1000;
    }


    public int getLightCount(long day) {
        if (day==0){
            return mSLDao.getLightCountByTypeAndAll(TYPE_LIGHT);
        }
        return mSLDao.getLightCountByTypeAndDay(TYPE_LIGHT, day);
    }

    public int getUserPresentCount(long day) {
        if (day==0){
            return mSLDao.getLightCountByTypeAndAll(TYPE_LOCK);
        }
        return mSLDao.getLightCountByTypeAndDay(TYPE_LOCK, day);
    }

    public int getLightAutoOffCOunt(long day) {
        if (day==0){
            return mSLDao.getLightCountByTypeAndAll(TYPE_OFF);
        }
        return mSLDao.getLightCountByTypeAndDay(TYPE_OFF, day);
    }


}

