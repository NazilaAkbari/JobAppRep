package com.akbari.myapplication.jobapp.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.akbari.myapplication.jobapp.model.Job;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by n.akbari on 04/24/2016.
 */
public class JobDao {

    public void addJob(Context context, Job job) {
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbHelper.FeedEntry.COLUMN_NAME_PAY_DAY, job.getPayDay());
        contentValues.put(DbHelper.FeedEntry.COLUMN_NAME_JOB, job.getJobName());
        db.insert(DbHelper.FeedEntry.TABLE_NAME_JOB, null, contentValues);
        db.close();
    }

    public void editJob(Context context, Job job) {
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbHelper.FeedEntry.COLUMN_NAME_PAY_DAY, job.getPayDay());
        contentValues.put(DbHelper.FeedEntry.COLUMN_NAME_JOB, job.getJobName());
        db.update(DbHelper.FeedEntry.TABLE_NAME_JOB, contentValues, "_id=" + job.getId(), null);
        db.close();
    }

    public void editJobNameInTimeDb(Context context, Job job, String oldName) {
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DbHelper.FeedEntry.COLUMN_NAME_JOB_Name, job.getJobName());
        db.update(DbHelper.FeedEntry.TABLE_NAME_TIME, cv, "jobName= '" + oldName+"'", null);
        db.close();
    }

    public Job findJobIdByTitleAndPayDay(Context context, String title, String payDay) {
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + DbHelper.FeedEntry.TABLE_NAME_JOB
                + " WHERE " + DbHelper.FeedEntry.COLUMN_NAME_JOB + " = '" + title
                + "' AND " + DbHelper.FeedEntry.COLUMN_NAME_PAY_DAY + " = '" + payDay + "'";
        Cursor cursor = db.rawQuery(query, null);
        Job job = new Job();
        if (cursor.moveToFirst()) {
            System.out.println(cursor.getString(0) + "!!!!");
            job.setId(cursor.getString(0));
            job.setJobName(cursor.getString(1));
            job.setPayDay(Integer.valueOf(cursor.getString(2)));
        }
        cursor.close();
        db.close();
        return job;
    }

    public List<Job> getAllJobs(Context context) {
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DbHelper.FeedEntry.TABLE_NAME_JOB, null);
        List<Job> jobs = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                Job job = new Job();
                job.setId(cursor.getString(0));
                job.setJobName(cursor.getString(1));
                job.setPayDay(Integer.valueOf(cursor.getString(2)));
                jobs.add(job);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return jobs;
    }

    public void delete(Context context, Job job) {
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        db.delete(
                DbHelper.FeedEntry.TABLE_NAME_JOB,
                DbHelper.FeedEntry.COLUMN_NAME_JOB_Name + "=?",
                new String[]{job.getJobName()}
        );
        db.close();
    }

}
