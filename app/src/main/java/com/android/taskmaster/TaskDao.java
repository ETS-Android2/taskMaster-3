package com.android.taskmaster;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TaskDao  {
@Insert
    void insertOneTask(TaskItem taskItem);

@Query("SELECT * FROM taskItem WHERE title_task LIKE :taskTitle")
TaskItem findByName(String taskTitle);

    @Query("SELECT * FROM taskitem")
    List<TaskItem> findAll();

}
