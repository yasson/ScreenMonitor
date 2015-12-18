package com.ys.sm.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * screen ligth data db
 * Created by ys on 2015/12/16.
 */
public class SLDbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "lc.db";
    private static final int    VERSION = 1;

    public static final String TB_NAME_SL = "screenlight";
    public static final String C_DAY      = "day";
    public static final String C_TYPE     = "type";
    public static final String C_COUNT    = "count";
    public static final String C_START    = "start";
    public static final String C_END      = "end";
    public static final String C_DUATION  = "duation";

    public static final String TB_NAME_AR = "app_record";
    public static final String C_PNAME    = "pname";

    public SLDbHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists " + TB_NAME_SL + " (" + C_DAY + " text," + C_TYPE + " integer," + C_COUNT + " integer," +
                   C_START + " long," +
                   C_END + " long," +
                   C_DUATION + " long)");
        db.execSQL("create table if not exists " + TB_NAME_AR + " (" + C_DAY + " text," + C_PNAME + " text," + C_COUNT + " integer," +
                   C_DUATION + " long)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
