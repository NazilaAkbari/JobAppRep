package com.akbari.myapplication.jobapp.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.akbari.myapplication.jobapp.model.Job;

import java.util.ArrayList;
import java.util.List;

/**
 * @author n.akbari
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
        cv.put(DbHelper.FeedEntry.COLUMN_NAME_JOB_ID, job.getId());
        db.update(DbHelper.FeedEntry.TABLE_NAME_TIME, cv, "jobName= '" + oldName + "'", null);
        db.close();
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
                DbHelper.FeedEntry.COLUMN_NAME_JOB_ID + "=?",
                new String[]{job.getId()}
        );
        db.close();
    }

    public Job findJobById(Context context, String jobId) {
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + DbHelper.FeedEntry.TABLE_NAME_JOB
                + " WHERE " + DbHelper.FeedEntry._ID + " = '" + jobId+"'";
        Cursor cursor = db.rawQuery(query, null);
        Job job = new Job();
        if (cursor.moveToFirst()) {
            job.setId(cursor.getString(0));
            job.setJobName(cursor.getString(1));
            job.setPayDay(Integer.valueOf(cursor.getString(2)));
        }
        cursor.close();
        db.close();
        return job;
    }
}
