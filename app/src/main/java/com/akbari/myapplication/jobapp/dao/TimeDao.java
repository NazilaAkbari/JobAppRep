package com.akbari.myapplication.jobapp.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.akbari.myapplication.jobapp.model.Time;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Nazila Akbari on 04/24/2016.
 */
public class TimeDao {

    public void AddTime(Context context, Time time) throws ParseException {
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        Date date1 = format.parse(time.getEnterTime());
        Date date2 = format.parse(time.getExitTime());
        long difference = (date2.getTime() - date1.getTime()) / ((60 * 60 * 1000));
        contentValues.put(DbHelper.FeedEntry.COLUMN_NAME_ENTER, time.getEnterTime());
        contentValues.put(DbHelper.FeedEntry.COLUMN_NAME_EXIT, time.getExitTime());
        contentValues.put(DbHelper.FeedEntry.COLUMN_NAME_Date, time.getDate());
        contentValues.put(DbHelper.FeedEntry.COLUMN_NAME_JOB_Name, time.getJobName());
        contentValues.put(DbHelper.FeedEntry.COLUMN_NAME_SUM, difference);
        db.insert(DbHelper.FeedEntry.TABLE_NAME_TIME, null, contentValues);
        db.close();
    }


    public Integer getMonthTime(Context context, String payDay, String jobName) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        Calendar queryCal = getQueryCal(Integer.valueOf(payDay));
        String dateFrom = dateFormat.format(queryCal.getTime());
        String dateTo = dateFormat.format(Calendar.getInstance().getTime());
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT SUM ( " + DbHelper.FeedEntry.COLUMN_NAME_SUM
                + " ) FROM " + DbHelper.FeedEntry.TABLE_NAME_TIME
                + " WHERE " + DbHelper.FeedEntry.COLUMN_NAME_Date
                + " BETWEEN '" + dateFrom + "' AND '" + dateTo + "'" +
                " AND " + DbHelper.FeedEntry.COLUMN_NAME_JOB_Name + " = '" + jobName + "'";
        System.out.println("query:"+query);
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst())
            return cursor.getInt(0);
        else
            return -1;
    }

    private Calendar getQueryCal(int payDay) {
        Calendar cal = Calendar.getInstance();
        Calendar queryCal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_MONTH);
        queryCal.set(Calendar.DAY_OF_MONTH, payDay);
        if (day > payDay) {
            queryCal.set(Calendar.MONTH, cal.get(Calendar.MONTH));

        } else {
            queryCal.set(Calendar.MONTH, cal.get(Calendar.MONTH) - 1);
        }
        return queryCal;
    }
}
