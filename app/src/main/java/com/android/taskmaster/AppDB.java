package com.android.taskmaster;

import androidx.room.Database;
import androidx.room.RoomDatabase;
//@room.schemaLocation
@Database(entities = {TaskItem.class}, version = 1)
public abstract class AppDB extends RoomDatabase {
    public abstract TaskDao taskDao();
}
