package com.akbari.myapplication.jobapp.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.akbari.myapplication.jobapp.model.QueryModel;
import com.akbari.myapplication.jobapp.model.Time;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
        SimpleDateFormat dateFormatFrom = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        SimpleDateFormat dateFormatTo = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        String newDate = dateFormatTo.format(dateFormatFrom.parse(time.getDate()));
        Date timeEnter = timeFormat.parse(time.getEnterTime());
        Date timeExit = timeFormat.parse(time.getExitTime());
        long difference = (timeExit.getTime() - timeEnter.getTime()) / ((60 * 60 * 1000));
        contentValues.put(DbHelper.FeedEntry.COLUMN_NAME_ENTER, time.getEnterTime());
        contentValues.put(DbHelper.FeedEntry.COLUMN_NAME_EXIT, time.getExitTime());
        contentValues.put(DbHelper.FeedEntry.COLUMN_NAME_Date, newDate);
        contentValues.put(DbHelper.FeedEntry.COLUMN_NAME_JOB_Name, time.getJobName());
        contentValues.put(DbHelper.FeedEntry.COLUMN_NAME_SUM, difference);
        db.insert(DbHelper.FeedEntry.TABLE_NAME_TIME, null, contentValues);
        db.close();
    }


    public Integer getMonthTime(Context context, String payDay, String jobName) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
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
        System.out.println("query:" + query);
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
        if (day > payDay)
            queryCal.set(Calendar.MONTH, cal.get(Calendar.MONTH));
        else
            queryCal.set(Calendar.MONTH, cal.get(Calendar.MONTH) - 1);
        return queryCal;
    }

    public Map<Integer, Integer> getChartData(Context context, QueryModel queryModel) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Calendar endOfThisMonth = getEndOfThisMonth(queryModel);
        Calendar startDate = getDateOfFirstTimeEntry(context, queryModel.getJobName());
        Map<Integer, Integer> chartAxisMap = new HashMap<>();
        while (endOfThisMonth.compareTo(startDate) > 0) {
            QueryModel newQueryModel = new QueryModel();
            newQueryModel.setDateTo(dateFormat.format(endOfThisMonth.getTime()));
            endOfThisMonth.set(Calendar.MONTH, endOfThisMonth.get(Calendar.MONTH) - 1);
            newQueryModel.setDateFrom(dateFormat.format(endOfThisMonth.getTime()));
            newQueryModel.setJobName(queryModel.getJobName());
            if (queryModel.getPayDay() > 15)
                chartAxisMap.put(endOfThisMonth.get(Calendar.MONTH) + 2,
                        getTimeInMonthDateRange(context, newQueryModel));
            else
                chartAxisMap.put(endOfThisMonth.get(Calendar.MONTH) + 1,
                        getTimeInMonthDateRange(context, newQueryModel));
        }
        return chartAxisMap;
    }

    private Calendar getEndOfThisMonth(QueryModel queryModel) {
        String pattern = "dd-MM-yyyy";
        Date date = new Date();
        try {
            date = new SimpleDateFormat(pattern).parse(queryModel.getDateTo());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        Calendar endDate = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        endDate.set(Calendar.DAY_OF_MONTH, queryModel.getPayDay());
        if (day > queryModel.getPayDay())
            endDate.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1);
        else
            endDate.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
        return endDate;
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

    private Calendar getDateOfFirstTimeEntry(Context context, String jobName) {
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT " + DbHelper.FeedEntry.COLUMN_NAME_Date
                + " FROM " + DbHelper.FeedEntry.TABLE_NAME_TIME
                + " WHERE " + DbHelper.FeedEntry.COLUMN_NAME_JOB_Name
                + " = '" + jobName + "'"
                + " ORDER BY " + DbHelper.FeedEntry.COLUMN_NAME_Date
                + " ASC LIMIT 1";
        System.out.println(query + "!!!!!!Q");
        Cursor cursor = db.rawQuery(query, null);
        Calendar startDate = Calendar.getInstance();
        if (cursor.moveToFirst()) {
            String pattern = "20yy-MM-dd";
            Date date = new Date();
            try {
                date = new SimpleDateFormat(pattern).parse(cursor.getString(0));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            startDate.setTime(date);
            return startDate;
        } else
            return startDate;
    }

}
