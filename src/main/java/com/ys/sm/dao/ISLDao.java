package com.ys.sm.dao;

import com.ys.sm.model.SLDayModel;

import java.util.List;

/**
 * 屏幕点亮数据库操作
 * Created by ys on 2015/12/16.
 */
public interface ISLDao {

    /**
     * insert one
     * @param model
     */
    void insert(SLDayModel model);

    /**
     * update light end time
     * @param endTime
     */
    void updateLightEndTime(long endTime);

    /**
     * get last light time
     */
    long getLastLightTime();

    /**
     * 某一天的信息
     * @param day 这一天的起始时间
     * @return
     */
    List<SLDayModel> getInfoOfDay(long day);

    /**
     * 获取某一天某一类型的信息
     * @param type
     * @param day
     * @return
     */
    List<SLDayModel> getInfoOfDay(int type,long day);
    List<SLDayModel> getInfoOfAll(int type);
    /**
     * 所有点击信息
     * @return
     */
    List<SLDayModel> getAll();

    int getLightCountByTypeAndDay(int type, long day);
    int getLightCountByTypeAndAll(int type);

    /**
     * 获取某一天某一type的总亮屏事件
     * @param type
     * @param day
     * @return
     */
    int getRemainTimeOfDay(int type, long day);
    int getRemainTimeOfAll(int type);
}
