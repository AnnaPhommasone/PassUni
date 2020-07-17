package com.example.psgetdegrees.unitProvider;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Unit.class}, version = 1)
public abstract class UnitDatabase extends RoomDatabase {

    public static final String UNIT_DATABASE_NAME = "unit_database";
    public abstract UnitDao unitDao();
    private static volatile UnitDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static UnitDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (UnitDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            UnitDatabase.class, UNIT_DATABASE_NAME).build();
                }
            }
        }
        return INSTANCE;
    }

}
