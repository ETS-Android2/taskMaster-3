package com.android.taskmaster;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public final class  TaskItem {
    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "title_task")
    private String title;

    @ColumnInfo(name = "body_task")
    private String body;

    @ColumnInfo(name = "state_task")
    private String state;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
    @Ignore
    public TaskItem(String title, String body, String state) {
        this.title = title;
        this.body = body;
        this.state=state;
    }

    public TaskItem(String title,String body) {
        this.title=title;
        this.body=body;
    }
    @Ignore
    public TaskItem() {
    }

    public String getTitle() {
        return title;
    }

// --Commented out by Inspection START (8/25/21, 3:55 PM):
//    public void setTitle(String title) {
//        this.title = title;
//    }
// --Commented out by Inspection STOP (8/25/21, 3:55 PM)

    public String getBody() {
        return body;
    }

// --Commented out by Inspection START (8/25/21, 3:55 PM):
//    public void setBody(String body) {
//        this.body = body;
//    }
// --Commented out by Inspection STOP (8/25/21, 3:55 PM)
}