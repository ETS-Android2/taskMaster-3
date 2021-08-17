package com.android.taskmaster;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {TaskItem.class}, version = 1,exportSchema = false)
public abstract class AppDB extends RoomDatabase {
    public abstract TaskDao taskDao();
}
