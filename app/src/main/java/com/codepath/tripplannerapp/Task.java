package com.codepath.tripplannerapp;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import org.parceler.Parcel;

import java.util.Date;

@Parcel(analyze = Task.class)
@ParseClassName("Task")
public class Task extends ParseObject {

    public static final String KEY_TASK_NAME = "taskName";
    public static final String KEY_TASK_DATE = "taskDate";
    public static final String KEY_TASK_CATEGORY = "taskCategory";
    public static final String KEY_USER = "user";

    public String getTaskName() { return getString(KEY_TASK_NAME); }
    public void setTaskName(String description) { put(KEY_TASK_NAME, description);}

    public Date getTaskDate() {return getDate(KEY_TASK_DATE);}
    public void setTaskDate(Date taskDate) { put(KEY_TASK_DATE, taskDate); }

    public String getTaskCategory() { return getString(KEY_TASK_CATEGORY); }
    public void setTaskCategory(String taskCategory) { put(KEY_TASK_NAME, taskCategory);}
}
