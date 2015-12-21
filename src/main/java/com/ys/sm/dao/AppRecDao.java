package com.ys.sm.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.ys.sm.db.SLDbHelper;
import com.ys.sm.model.AppRecordModel;

import java.util.ArrayList;
import java.util.List;

import static com.ys.sm.db.SLDbHelper.*;

/**
 * app 行为数据库
 * Created by ys on 2015/12/17.
 */
public class AppRecDao implements IAppRecDao {


    private SLDbHelper     mDbHelper;
    private SQLiteDatabase mDb;


    public AppRecDao(Context context) {
        mDbHelper = new SLDbHelper(context);

    }

    @Override
    public void insertApp(long day, String pName, long duation) {
        mDb = mDbHelper.getWritableDatabase();
        mDb.execSQL("insert into " + TB_NAME_AR + "(" + C_DAY + "," + C_PNAME + "," + C_COUNT + "," + C_DUATION + ") values (" + day + ",\"" + pName +
                    "\"," +
                    "1," +
                    duation + ")");
        mDb.close();
    }

    @Override
    public boolean hasAppInDay(long day, String pName) {
        mDb = mDbHelper.getReadableDatabase();
        Cursor c = mDb.rawQuery("select * from " + TB_NAME_AR + " where " + C_DAY + "=" + day + " and " + C_PNAME + "=\"" + pName + "\"", null);
        boolean res = false;
        if (null != c && c.moveToFirst()) {
            c.close();
            res = true;
        }
        mDb.close();
        return res;
    }

    @Override
    public void updateAppRecords(long day, String pName, long duationInc) {
        mDb = mDbHelper.getWritableDatabase();
        mDb.execSQL("update " + TB_NAME_AR + " set " + C_COUNT + "=" + C_COUNT + "+1," + C_DUATION + "=" + C_DUATION + "+" + duationInc + " where " +
                    C_DAY +
                    "=" + day + " and " + C_PNAME + "=\"" + pName + "\"");
        mDb.close();
    }

    @Override
    public List<AppRecordModel> getAppRecordsOfDay(long day) {
        mDb = mDbHelper.getReadableDatabase();
        List<AppRecordModel> list = null;
        Cursor c = mDb.rawQuery("select * from " + TB_NAME_AR + " where " + C_DAY + "=" + day + " order by " + C_COUNT + " desc", null);
        if (null != c && c.moveToFirst()) {
            list = new ArrayList<>();
            do {
                AppRecordModel model = new AppRecordModel();
                model.day = c.getLong(c.getColumnIndex(C_DAY));
                model.pName = c.getString(c.getColumnIndex(C_PNAME));
                model.count = c.getInt(c.getColumnIndex(C_COUNT));
                model.duation = c.getLong(c.getColumnIndex(C_DUATION));
                list.add(model);
            } while (c.moveToNext());
            c.close();
        }
        mDb.close();
        return list;
    }

    @Override
    public List<AppRecordModel> getAppRecordsOfAll() {
        mDb = mDbHelper.getReadableDatabase();
        List<AppRecordModel> list = null;
        Cursor c = mDb.rawQuery(
                "select " + C_PNAME + "," + "sum(" + C_COUNT + "),sum(" + C_DUATION + ") from " + TB_NAME_AR + " group by " + C_PNAME +
                " order by sum(" + C_COUNT + ") desc", null);
        if (null != c && c.moveToFirst()) {
            list = new ArrayList<>();
            do {
                AppRecordModel model = new AppRecordModel();
                model.pName = c.getString(c.getColumnIndex(C_PNAME));
                model.count = c.getInt(c.getColumnIndex("sum(" + C_COUNT + ")"));
                model.duation = c.getLong(c.getColumnIndex("sum(" + C_DUATION + ")"));
                list.add(model);
            } while (c.moveToNext());
            c.close();
        }
        mDb.close();
        return list;
    }
}
