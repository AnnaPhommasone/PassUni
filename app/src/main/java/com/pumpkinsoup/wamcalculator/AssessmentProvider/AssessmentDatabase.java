package com.pumpkinsoup.wamcalculator.AssessmentProvider;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Assessment.class}, version = 1)
public abstract class AssessmentDatabase extends RoomDatabase {

    public static final String ASSESSMENT_DATABASE_NAME = "assessment_database";
    public abstract AssessmentDao assessmentDao();
    private static volatile AssessmentDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static AssessmentDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AssessmentDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AssessmentDatabase.class, ASSESSMENT_DATABASE_NAME)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}
