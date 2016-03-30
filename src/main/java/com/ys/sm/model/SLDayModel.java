package com.ys.sm.model;

/**
 * 点亮的数据库model
 * Created by ys on 2015/12/15.
 */
public class SLDayModel {

    public static final int TYPE_LIGHT = 0;//total light times
    public static final int TYPE_LOCK  = 1;//user-present then screenlock times
    public static final int TYPE_OFF   = 2;//light then off whitout user-present times

    public long day;//日期，精确到天
    public int  type;
    public int  count=1;//次数，每插入一次一次记一条
    public long start;
    public long end;
    public long duation;

    @Override
    public String toString() {
        return "SLDayModel{" +
               "day=" + day +
               ", type=" + type +
               ", count=" + count +
               ", start=" + start +
               ", end=" + end +
               ", duation=" + duation +
               '}';
    }
}
