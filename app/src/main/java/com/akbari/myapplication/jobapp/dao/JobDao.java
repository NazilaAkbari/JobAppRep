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
        contentValues.put(DbHelper.FeedEntry.COLUMN_NAME_JOB, job.getJobTitle());
        db.insert(DbHelper.FeedEntry.TABLE_NAME_JOB, null, contentValues);
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
                job.setJobTitle(cursor.getString(1));
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
                new String[]{job.getJobTitle()}
        );
        db.close();
    }

}
