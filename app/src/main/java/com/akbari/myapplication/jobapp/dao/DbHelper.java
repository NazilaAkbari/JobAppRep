package com.akbari.myapplication.jobapp.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by n.akbari on 04/19/2016.
 */
public class DbHelper extends SQLiteOpenHelper {


    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "FeedReader.db";

    public static abstract class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME_JOB = "jobDb";
        public static final String COLUMN_NAME_JOB = "jobName";
        public static final String COLUMN_NAME_PAY_DAY = "payDay";
        private static final String TEXT_TYPE = " TEXT";
        private static final String COMMA_SEP = ",";
        public static final String SQL_CREATE_JOB =
                "CREATE TABLE " + FeedEntry.TABLE_NAME_JOB + " (" +
                        FeedEntry._ID + " INTEGER PRIMARY KEY," +
                        FeedEntry.COLUMN_NAME_JOB + TEXT_TYPE + COMMA_SEP +
                        FeedEntry.COLUMN_NAME_PAY_DAY + " INTEGER" +
                        " )";

        private static final String SQL_DELETE_JOB =
                "DROP TABLE IF EXISTS " + FeedEntry.TABLE_NAME_JOB;

        public static final String TABLE_NAME_TIME = "detailDb";
        public static final String COLUMN_NAME_ENTER = "enterColumn";
        public static final String COLUMN_NAME_EXIT = "exitColumn";
        public static final String COLUMN_NAME_SUM = "sumColumn";
        public static final String COLUMN_NAME_JOB_Name = "jobName";
        public static final String COLUMN_NAME_Date = "date";
        private static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + FeedEntry.TABLE_NAME_TIME + " (" +
                        FeedEntry._ID + " INTEGER PRIMARY KEY," +
                        FeedEntry.COLUMN_NAME_JOB_Name + TEXT_TYPE + COMMA_SEP +
                        FeedEntry.COLUMN_NAME_ENTER + TEXT_TYPE + COMMA_SEP +
                        FeedEntry.COLUMN_NAME_EXIT + TEXT_TYPE + COMMA_SEP +
                        FeedEntry.COLUMN_NAME_Date + TEXT_TYPE + COMMA_SEP +
                        FeedEntry.COLUMN_NAME_SUM + " FLOAT" +
                        " )";

        private static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + FeedEntry.TABLE_NAME_TIME;
    }

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        System.out.println("create");
        db.execSQL(FeedEntry.SQL_CREATE_JOB);
        db.execSQL(FeedEntry.SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(FeedEntry.SQL_DELETE_JOB);
        onCreate(db);
    }
}
