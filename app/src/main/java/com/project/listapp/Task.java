package com.project.listapp;

import org.bson.types.ObjectId;

import io.realm.RealmModel;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Task   extends RealmObject {



    @PrimaryKey
    private ObjectId _id;
    private String title;
    private String description;
    private String lastUpdatedDate;
    private int priority;
    private boolean finished;
    // RealmObject subclasses must provide an empty constructor
    public Task() {
    }
    public ObjectId get_id() {
        return _id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }

    public Task(String title, String description, String lastUpdatedDate, int priority, boolean finished) {
        this.title = title;
        this.description = description;
        this.lastUpdatedDate = lastUpdatedDate;
        this.priority = priority;
        this.finished = finished;
    }

    public Task(String new_task) {
        this.title=new_task;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(String lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }
}
