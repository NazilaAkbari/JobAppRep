package com.akbari.myapplication.jobapp.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.akbari.myapplication.jobapp.model.Job;
import com.akbari.myapplication.jobapp.model.JobTime;
import com.akbari.myapplication.jobapp.model.Time;
import com.akbari.myapplication.jobapp.utils.DateUtil;
import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.PersianCalendar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @author Nazila Akbari
 * @version 1.0
 * @since 04/24/2016
 */
public class TimeDao {

    public void AddTime(Context context, Time time) throws ParseException {
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.US);
        Date timeEnter = timeFormat.parse(time.getEnterTime());
        Date timeExit = timeFormat.parse(time.getExitTime());
        float difference = (timeExit.getTime() - timeEnter.getTime()) / ((60 * 1000));
        contentValues.put(DbHelper.FeedEntry.COLUMN_NAME_ENTER, time.getEnterTime());
        contentValues.put(DbHelper.FeedEntry.COLUMN_NAME_EXIT, time.getExitTime());
        contentValues.put(DbHelper.FeedEntry.COLUMN_NAME_Date, time.getDate());
        contentValues.put(DbHelper.FeedEntry.COLUMN_NAME_JOB_ID, time.getJobId());
        contentValues.put(DbHelper.FeedEntry.COLUMN_NAME_SUM, difference);
        db.insert(DbHelper.FeedEntry.TABLE_NAME_TIME, null, contentValues);
        db.close();
    }

    public List<Time> getTimesInDateRange(Context context, JobTime jobTime) {
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + DbHelper.FeedEntry.TABLE_NAME_TIME
                + " WHERE " + DbHelper.FeedEntry.COLUMN_NAME_Date
                + " BETWEEN '" + jobTime.getDateFrom() + "' AND '"
                + jobTime.getDateTo() + "'"
                + " AND " + DbHelper.FeedEntry.COLUMN_NAME_JOB_ID
                + " = '" + jobTime.getJobId() + "'";
        List<Time> times = new ArrayList<>();
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Time time = new Time();
            time.setId(cursor.getString(0));
            time.setEnterTime(cursor.getString(2));
            time.setExitTime(cursor.getString(3));
            time.setDate(cursor.getString(4));
            times.add(time);
            cursor.moveToNext();
        }
        return times;
    }

    public Integer getThisMonthHour(Context context, Job job) {
        JobTime jobTime = new JobTime();
        jobTime.setPayDay(job.getPayDay());
        jobTime.setJobId(job.getId());
        PersianCalendar endOfThisMonth = getEndOfThisMonth(jobTime);
        jobTime.setDateTo(DateUtil.computeDateString(endOfThisMonth));
        endOfThisMonth.set(Calendar.MONTH, endOfThisMonth.get(Calendar.MONTH) - 1);
        jobTime.setDateFrom(DateUtil.computeDateString(endOfThisMonth));
        return getHourInDateRange(context, jobTime);
    }

    public Integer getThisWeekHour(Context context, Job job) {
        JobTime jobTime = new JobTime();
        jobTime.setPayDay(job.getPayDay());
        jobTime.setJobId(job.getId());
        PersianCalendar persianCalendar = new PersianCalendar();
        int dayOfWeek = persianCalendar.get(Calendar.DAY_OF_WEEK);
        persianCalendar.set(Calendar.MONTH, persianCalendar.get(Calendar.MONTH));
        jobTime.setDateTo(DateUtil.computeDateString(persianCalendar));
        persianCalendar.set(Calendar.DAY_OF_MONTH,
                persianCalendar.get(Calendar.DAY_OF_MONTH) - dayOfWeek);
        jobTime.setDateFrom(DateUtil.computeDateString(persianCalendar));
        return getHourInDateRange(context, jobTime);
    }

    public Map<String, Integer> getMonthDailyHourData(Context context, JobTime jobTime) {
        PersianCalendar startDate = getDateOfFirstTimeEntry(context, jobTime.getJobId());
        PersianCalendar today = new PersianCalendar();
        Map<String, Integer> chartAxisMap = new HashMap<>();
        while (today.compareTo(startDate) >= 0) {
            jobTime.setDateTo(DateUtil.computeDateString(today));
            Integer hourOfDay = getHourOfDay(context, jobTime);
            if (hourOfDay > 0) {
                chartAxisMap.put(DateUtil.computeDateString(today),
                        hourOfDay);
            }
            today.set(Calendar.DAY_OF_MONTH, today.get(Calendar.DAY_OF_MONTH) - 1);
        }
        return chartAxisMap;
    }

    private PersianCalendar getEndOfThisMonth(JobTime jobTime) {
        PersianCalendar calendar = new PersianCalendar();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.set(Calendar.DAY_OF_MONTH, jobTime.getPayDay());
        if (day > jobTime.getPayDay())
            calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1);
        else
            calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
        return calendar;
    }

    public Integer getHourInDateRange(Context context, JobTime jobTime) {
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT SUM ( " + DbHelper.FeedEntry.COLUMN_NAME_SUM
                + " ) FROM " + DbHelper.FeedEntry.TABLE_NAME_TIME
                + " WHERE " + DbHelper.FeedEntry.COLUMN_NAME_Date
                + " BETWEEN '" + jobTime.getDateFrom() + "' AND '"
                + jobTime.getDateTo() + "'"
                + " AND " + DbHelper.FeedEntry.COLUMN_NAME_JOB_ID
                + " = '" + jobTime.getJobId() + "'";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            return cursor.getInt(0);
        } else
            return -1;
    }

    private Integer getHourOfDay(Context context, JobTime jobTime) {
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT SUM ( " + DbHelper.FeedEntry.COLUMN_NAME_SUM
                + " ) FROM " + DbHelper.FeedEntry.TABLE_NAME_TIME
                + " WHERE " + DbHelper.FeedEntry.COLUMN_NAME_Date
                + " = '" + jobTime.getDateTo() + "'"
                + " AND " + DbHelper.FeedEntry.COLUMN_NAME_JOB_ID
                + " = '" + jobTime.getJobId() + "'";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            return cursor.getInt(0);
        } else
            return -1;
    }

    private PersianCalendar getDateOfFirstTimeEntry(Context context, String jobId) {
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT " + DbHelper.FeedEntry.COLUMN_NAME_Date
                + " FROM " + DbHelper.FeedEntry.TABLE_NAME_TIME
                + " WHERE " + DbHelper.FeedEntry.COLUMN_NAME_JOB_ID
                + " = '" + jobId + "'"
                + " ORDER BY " + DbHelper.FeedEntry.COLUMN_NAME_Date;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst())
            return DateUtil.parsePersianDate(cursor.getString(0));
        else
            return new PersianCalendar();
    }

    public void removeTime(Context context, String id) {
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        db.delete(
                DbHelper.FeedEntry.TABLE_NAME_TIME,
                DbHelper.FeedEntry._ID + "=?",
                new String[]{id}
        );
        db.close();
    }

    public Time findTimeById(Context context, String id) {
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + DbHelper.FeedEntry.TABLE_NAME_TIME
                + " WHERE " + DbHelper.FeedEntry._ID + " = '" + id + "'";
        Cursor cursor = db.rawQuery(query, null);
        Time time = new Time();
        if (cursor.moveToFirst()) {
            time.setId(cursor.getString(0));
            time.setJobId(cursor.getString(1));
            time.setEnterTime(cursor.getString(2));
            time.setExitTime(cursor.getString(3));
            time.setDate(cursor.getString(4));
        }
        cursor.close();
        db.close();
        return time;
    }

    public void edit(Context context, Time time) throws ParseException {
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.US);
        Date timeEnter = timeFormat.parse(time.getEnterTime());
        Date timeExit = timeFormat.parse(time.getExitTime());
        float difference = (timeExit.getTime() - timeEnter.getTime()) / ((60 * 1000));
        contentValues.put(DbHelper.FeedEntry.COLUMN_NAME_ENTER, time.getEnterTime());
        contentValues.put(DbHelper.FeedEntry.COLUMN_NAME_EXIT, time.getExitTime());
        contentValues.put(DbHelper.FeedEntry.COLUMN_NAME_Date, time.getDate());
        contentValues.put(DbHelper.FeedEntry.COLUMN_NAME_SUM, difference);
        db.update(DbHelper.FeedEntry.TABLE_NAME_TIME, contentValues, "_id=" + time.getId(), null);
        db.close();
    }

}
