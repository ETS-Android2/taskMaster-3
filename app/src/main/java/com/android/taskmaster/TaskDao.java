package com.android.taskmaster;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TaskDao  {
@Insert
    void insertOneTask(TaskItem taskItem);

// --Commented out by Inspection START (8/25/21, 3:55 PM):
//@Query("SELECT * FROM taskItem WHERE title_task LIKE :taskTitle")
//TaskItem findByName(String taskTitle);
// --Commented out by Inspection STOP (8/25/21, 3:55 PM)

// --Commented out by Inspection START (8/25/21, 3:55 PM):
//    @Query("SELECT * FROM taskitem")
//    List<TaskItem> findAll();
// --Commented out by Inspection STOP (8/25/21, 3:55 PM)

}
