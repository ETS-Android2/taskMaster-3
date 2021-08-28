package com.android.taskmaster;

import com.amplifyframework.datastore.generated.model.TaskItem;

import java.util.ArrayList;
import java.util.List;

public class TaskManager {
    private static TaskManager instance=null;
    private final List<TaskItem> taskLists = new ArrayList<>();


    private TaskManager() {
    }

    public static TaskManager getInstance(){
        if (instance == null) {
            instance = new TaskManager();
        }

        return instance;

    }
// --Commented out by Inspection START (8/25/21, 3:55 PM):
//    public void setData(List<TaskItem> data) {
//        taskLists = data;
//    }
// --Commented out by Inspection STOP (8/25/21, 3:55 PM)

    public List<TaskItem> getData() {
        return taskLists;
    }
}
