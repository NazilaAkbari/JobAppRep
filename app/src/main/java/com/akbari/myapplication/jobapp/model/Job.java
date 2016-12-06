package com.akbari.myapplication.jobapp.model;

/**
 * @author n.akbari
 * @since 04/24/2016
 * @version 1.0
 */
public class Job {
    private String id;
    private String jobName;
    private Integer payDay;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public Integer getPayDay() {
        return payDay;
    }

    public void setPayDay(Integer payDay) {
        this.payDay = payDay;
    }
}
