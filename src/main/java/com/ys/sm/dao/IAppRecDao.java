package com.ys.sm.dao;

import com.ys.sm.model.AppRecordModel;

import java.util.List;

/**
 * app 打开次数与时间的统计
 * Created by ys on 2015/12/17.
 */
public interface IAppRecDao {

    void insertApp(long day, String pName, long duation);

    boolean hasAppInDay(long day, String pName);

    void updateAppRecords(long day, String pName, long duationInc);

    List<AppRecordModel> getAppRecordsOfDay(long day);

    List<AppRecordModel> getAppRecordsOfAll();
}
