package com.pumpkinsoup.psgetdegrees.SubjectProvider;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Subject.class}, version = 1)
public abstract class SubjectDatabase extends RoomDatabase {

    public static final String SUBJECT_DATABASE_NAME = "subject_database";
    public abstract SubjectDao unitDao();
    private static volatile SubjectDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static SubjectDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (SubjectDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            SubjectDatabase.class, SUBJECT_DATABASE_NAME).build();
                }
            }
        }
        return INSTANCE;
    }

}
