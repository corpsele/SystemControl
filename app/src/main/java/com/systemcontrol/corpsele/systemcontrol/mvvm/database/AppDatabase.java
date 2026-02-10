package com.systemcontrol.corpsele.systemcontrol.mvvm.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.systemcontrol.corpsele.systemcontrol.mvvm.dao.ApiModelDao;
import com.systemcontrol.corpsele.systemcontrol.mvvm.model.ApiModel;

@Database(entities = {ApiModel.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;
    public abstract ApiModelDao apiModelDao();
    public static synchronized AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "api_database")
                    .fallbackToDestructiveMigration() // 仅用于演示，生产环境请写 Migration
                    .build();
        }
        return INSTANCE;
    }
}
