package com.akbari.myapplication.jobapp.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.akbari.myapplication.jobapp.model.QueryModel;
import com.akbari.myapplication.jobapp.model.Time;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
        if (day > payDay) {
            queryCal.set(Calendar.MONTH, cal.get(Calendar.MONTH));

        } else {
            queryCal.set(Calendar.MONTH, cal.get(Calendar.MONTH) - 1);
        }
        return queryCal;
    }

    public List<Integer> getChartYAxisList(Context context, QueryModel queryModel) {
        Map<String, String> datesMap = getDatesMap(queryModel);
        List<Integer> chartYAxisList = new ArrayList<>();
        for (Map.Entry<String, String> entry : datesMap.entrySet()) {
            QueryModel newQueryModel = new QueryModel();
            newQueryModel.setDateFrom(entry.getKey());
            newQueryModel.setDateTo(entry.getValue());
            newQueryModel.setJobName(queryModel.getJobName());
            chartYAxisList.add(getTimeInMonthDateRange(context, queryModel));
        }
        return chartYAxisList;
    }

    private Map<String, String> getDatesMap(QueryModel queryModel) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        Calendar firsDateBeforeDateTo = getDateFrom(queryModel);
        Calendar firstDateAfterDateFrom = getDateTo(queryModel);
        Calendar middleDate = firsDateBeforeDateTo;
        Map<String, String> dateMap = new HashMap<>();
        dateMap.put(dateFormat.format(firsDateBeforeDateTo.getTime()), queryModel.getDateTo());
        dateMap.put(queryModel.getDateFrom(), dateFormat.format(firstDateAfterDateFrom.getTime()));
        while (firsDateBeforeDateTo.compareTo(firstDateAfterDateFrom) > 0) {
            middleDate.set(Calendar.MONTH, middleDate.get(Calendar.MONTH) - 1);
            dateMap.put(dateFormat.format(middleDate.getTime()),
                    dateFormat.format(firsDateBeforeDateTo.getTime()));
            firsDateBeforeDateTo = middleDate;
        }
        return dateMap;
    }

    private Calendar getDateFrom(QueryModel queryModel) {
        String pattern = "dd-MM-yyyy";
        Date date = new Date();
        try {
            date = new SimpleDateFormat(pattern).parse(queryModel.getDateTo());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        Calendar dateToCal = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        dateToCal.set(Calendar.DAY_OF_MONTH, queryModel.getPayDay());
        if (day > queryModel.getPayDay()) {
            dateToCal.set(Calendar.MONTH, calendar.get(Calendar.MONTH));

        } else {
            dateToCal.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1);
        }
        return dateToCal;
    }

    private Calendar getDateTo(QueryModel queryModel) {
        String pattern = "dd-MM-yyyy";
        Date date = new Date();
        try {
            date = new SimpleDateFormat(pattern).parse(queryModel.getDateFrom());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        Calendar dateFromCal = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        dateFromCal.set(Calendar.DAY_OF_MONTH, queryModel.getPayDay());
        if (day >= queryModel.getPayDay()) {
            dateFromCal.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1);

        } else {
            dateFromCal.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
        }
        return dateFromCal;
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
        System.out.println(query+"!!!!!!Q");
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst())
            return cursor.getInt(0);
        else
            return -1;
    }

}
