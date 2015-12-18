package com.ys.sm.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.ys.sm.db.SLDbHelper;
import com.ys.sm.model.SLDayModel;

import java.util.ArrayList;
import java.util.List;

import static com.ys.sm.db.SLDbHelper.*;

/**
 * db operate
 * Created by ys on 2015/12/16.
 */
public class SLDao implements ISLDao {


    private SLDbHelper     mDbHelper;
    private SQLiteDatabase mDb;

    public SLDao(Context c) {
        mDbHelper = new SLDbHelper(c);
    }

    @Override
    public void insert(SLDayModel model) {
        mDb = mDbHelper.getWritableDatabase();
        mDb.execSQL("insert into " + TB_NAME_SL + "(" + C_DAY + "," + C_TYPE + "," + C_COUNT + "," + C_START + "," + C_END + "," + C_DUATION + ") " +
                    "values" +
                    "(" + model.day + "," + model.type + "," + model.count + "," + model.start + "," + model.end + "," + model.duation + ")");
        mDb.close();
    }

    @Override
    public void updateLightEndTime(long endTime) {
        mDb = mDbHelper.getWritableDatabase();
        long startTime = getLastLightTime();
        long duation = endTime - startTime;
        mDb.execSQL(
                "update " + TB_NAME_SL + " set " + C_END + "=" + endTime + "," + C_DUATION + "=" + duation + " where " + C_START + "=" + startTime);
        mDb.close();
    }

    @Override
    public long getLastLightTime() {
        mDb = mDbHelper.getReadableDatabase();
        Cursor cursor = mDb.rawQuery("select " + " max(" + C_START + ")" + " from " + TB_NAME_SL, null);
        long time = 0;
        if (null != cursor && cursor.moveToFirst()) {
            time = cursor.getLong(0);
            cursor.close();
        }
        return time;
    }

    @Override
    public List<SLDayModel> getInfoOfDay(long day) {
        mDb = mDbHelper.getReadableDatabase();
        Cursor c = mDb.rawQuery("select * from " + TB_NAME_SL + " where " + C_START + ">=" + day, null);
        if (null != c && c.moveToFirst()) {
            List<SLDayModel> list = new ArrayList<>();
            do {
                SLDayModel model = new SLDayModel();
                model.day = day;
                model.type = c.getInt(c.getColumnIndex(C_TYPE));
                model.count = c.getInt(c.getColumnIndex(C_COUNT));
                model.start = c.getInt(c.getColumnIndex(C_START));
                model.end = c.getInt(c.getColumnIndex(C_END));
                model.duation = c.getInt(c.getColumnIndex(C_DUATION));
                list.add(model);
            } while (c.moveToNext());
            c.close();
            mDb.close();
            return list;
        }
        mDb.close();
        return null;
    }

    @Override
    public List<SLDayModel> getInfoOfDay(int type, long day) {
        mDb = mDbHelper.getReadableDatabase();
        Cursor c = mDb.rawQuery("select * from " + TB_NAME_SL + " where " + C_DAY + "=" + day + " and " + C_TYPE + "=" + type, null);
        if (null != c && c.moveToFirst()) {
            List<SLDayModel> list = new ArrayList<>();
            do {
                SLDayModel model = new SLDayModel();
                model.day = day;
                model.type = c.getInt(c.getColumnIndex(C_TYPE));
                model.count = c.getInt(c.getColumnIndex(C_COUNT));
                model.start = c.getInt(c.getColumnIndex(C_START));
                model.end = c.getInt(c.getColumnIndex(C_END));
                model.duation = c.getInt(c.getColumnIndex(C_DUATION));
                list.add(model);
            } while (c.moveToNext());
            c.close();
            mDb.close();
            return list;
        }
        mDb.close();
        return null;
    }

    @Override
    public List<SLDayModel> getInfoOfAll(int type) {
        mDb = mDbHelper.getReadableDatabase();
        Cursor c = mDb.rawQuery("select * from " + TB_NAME_SL + " where " + C_TYPE + "=" + type, null);
        if (null != c && c.moveToFirst()) {
            List<SLDayModel> list = new ArrayList<>();
            do {
                SLDayModel model = new SLDayModel();
                model.day = c.getLong(c.getColumnIndex(C_DAY));
                model.type = c.getInt(c.getColumnIndex(C_TYPE));
                model.count = c.getInt(c.getColumnIndex(C_COUNT));
                model.start = c.getInt(c.getColumnIndex(C_START));
                model.end = c.getInt(c.getColumnIndex(C_END));
                model.duation = c.getInt(c.getColumnIndex(C_DUATION));
                list.add(model);
            } while (c.moveToNext());
            c.close();
            mDb.close();
            return list;
        }
        mDb.close();
        return null;
    }

    @Override
    public List<SLDayModel> getAll() {
        return null;
    }

    @Override
    public int getLightCountByTypeAndDay(int type, long day) {
        mDb = mDbHelper.getReadableDatabase();
        int count = 0;
        Cursor c =
                mDb.rawQuery("select sum(" + C_COUNT + ")from " + TB_NAME_SL + " where " + C_DAY + "=" + day + " and " + C_TYPE + "=" + type, null);
        if (null != c && c.moveToFirst()) {
            count = c.getInt(0);
            c.close();
        }
        mDb.close();
        return count;
    }

    @Override
    public int getLightCountByTypeAndAll(int type) {
        mDb = mDbHelper.getReadableDatabase();
        int count = 0;
        Cursor c = mDb.rawQuery("select sum(" + C_COUNT + ")from " + TB_NAME_SL + " where " + C_TYPE + "=" + type, null);
        if (null != c && c.moveToFirst()) {
            count = c.getInt(0);
            c.close();
        }
        mDb.close();
        return count;
    }

    public int getRemainTimeOfDay(int type, long day) {
        mDb = mDbHelper.getReadableDatabase();
        int sum = 0;
        Cursor c =
                mDb.rawQuery("select sum(" + C_DUATION + ")from " + TB_NAME_SL + " where " + C_TYPE + "=" + type + " and " + C_DAY + "=" + day, null);
        if (null != c && c.moveToFirst()) {
            sum = c.getInt(0);
            c.close();
        }
        mDb.close();
        return sum;


    }

    @Override
    public int getRemainTimeOfAll(int type) {
        mDb = mDbHelper.getReadableDatabase();
        int sum = 0;
        Cursor c = mDb.rawQuery("select sum(" + C_DUATION + ")from " + TB_NAME_SL + " where " + C_TYPE + "=" + type, null);
        if (null != c && c.moveToFirst()) {
            sum = c.getInt(0);
            c.close();
        }
        mDb.close();
        return sum;
    }

}
