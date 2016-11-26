package com.akbari.myapplication.jobapp.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.akbari.myapplication.jobapp.model.QueryModel;
import com.akbari.myapplication.jobapp.model.Time;
import com.akbari.myapplication.jobapp.utils.DateUtil;
import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.PersianCalendar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
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
        long difference = (timeExit.getTime() - timeEnter.getTime()) / ((60 * 60 * 1000));
        contentValues.put(DbHelper.FeedEntry.COLUMN_NAME_ENTER, time.getEnterTime());
        contentValues.put(DbHelper.FeedEntry.COLUMN_NAME_EXIT, time.getExitTime());
        contentValues.put(DbHelper.FeedEntry.COLUMN_NAME_Date, time.getDate());
        contentValues.put(DbHelper.FeedEntry.COLUMN_NAME_JOB_Name, time.getJobName());
        contentValues.put(DbHelper.FeedEntry.COLUMN_NAME_SUM, difference);
        db.insert(DbHelper.FeedEntry.TABLE_NAME_TIME, null, contentValues);
        db.close();
    }


    public Integer getThisMonthTime(Context context, String payDay, String jobName) {
        QueryModel queryModel = new QueryModel();
        queryModel.setPayDay(Integer.valueOf(payDay));
        queryModel.setJobName(jobName);
        PersianCalendar endOfThisMonth = getEndOfThisMonth(queryModel);
        queryModel.setDateTo(DateUtil.computeDateString(endOfThisMonth));
        endOfThisMonth.set(Calendar.MONTH, endOfThisMonth.get(Calendar.MONTH) - 1);
        queryModel.setDateFrom(DateUtil.computeDateString(endOfThisMonth));
        return getTimeInMonthDateRange(context, queryModel);
    }

    public Map<Integer, Integer> getChartData(Context context, QueryModel queryModel) {
        PersianCalendar endOfThisMonth = getEndOfThisMonth(queryModel);
        PersianCalendar startDate = getDateOfFirstTimeEntry(context, queryModel.getJobName());
        Map<Integer, Integer> chartAxisMap = new HashMap<>();
        while (endOfThisMonth.compareTo(startDate) > 0) {
            QueryModel newQueryModel = new QueryModel();
            newQueryModel.setDateTo(DateUtil.computeDateString(endOfThisMonth));
            endOfThisMonth.set(Calendar.MONTH, endOfThisMonth.get(Calendar.MONTH) - 1);
            newQueryModel.setDateFrom(DateUtil.computeDateString(endOfThisMonth));
            newQueryModel.setJobName(queryModel.getJobName());
            if (queryModel.getPayDay() > 15)
                chartAxisMap.put(endOfThisMonth.get(Calendar.MONTH) + 1,
                        getTimeInMonthDateRange(context, newQueryModel));
            else
                chartAxisMap.put(endOfThisMonth.get(Calendar.MONTH),
                        getTimeInMonthDateRange(context, newQueryModel));
        }
        return chartAxisMap;
    }

    private PersianCalendar getEndOfThisMonth(QueryModel queryModel) {
        PersianCalendar calendar = new PersianCalendar();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.set(Calendar.DAY_OF_MONTH, queryModel.getPayDay());
        if (day > queryModel.getPayDay())
            calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 2);
        else
            calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1);
        return calendar;
    }

    private Integer getTimeInMonthDateRange(Context context, QueryModel queryModel) {
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT SUM ( " + DbHelper.FeedEntry.COLUMN_NAME_SUM
                + " ) FROM " + DbHelper.FeedEntry.TABLE_NAME_TIME
                + " WHERE " + DbHelper.FeedEntry.COLUMN_NAME_Date
                + " BETWEEN '" + queryModel.getDateFrom() + "' AND '"
                + queryModel.getDateTo() + "'"
                + " AND " + DbHelper.FeedEntry.COLUMN_NAME_JOB_Name
                + " = '" + queryModel.getJobName() + "'";
        System.out.println(query + "!!!!!!Q");
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            return cursor.getInt(0);
        } else
            return -1;
    }

    private PersianCalendar getDateOfFirstTimeEntry(Context context, String jobName) {
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT " + DbHelper.FeedEntry.COLUMN_NAME_Date
                + " FROM " + DbHelper.FeedEntry.TABLE_NAME_TIME
                + " WHERE " + DbHelper.FeedEntry.COLUMN_NAME_JOB_Name
                + " = '" + jobName + "'"
                + " ORDER BY " + DbHelper.FeedEntry.COLUMN_NAME_Date;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            return DateUtil.parsePersianDate(cursor.getString(0));
        } else {
            PersianCalendar calendar = new PersianCalendar();
            calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1);
            return new PersianCalendar();
        }
    }

}
