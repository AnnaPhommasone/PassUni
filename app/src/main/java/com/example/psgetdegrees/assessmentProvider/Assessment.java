package com.example.psgetdegrees.assessmentProvider;

import android.provider.BaseColumns;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import static com.example.psgetdegrees.assessmentProvider.Assessment.TABLE_NAME;

@Entity(tableName = TABLE_NAME)
public class Assessment {
    public static final String TABLE_NAME = "assessments";
    public static final String COLUMN_ID = BaseColumns._ID;

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "assessmentId")
    private int id;

    @ColumnInfo(name = "assessmentName")
    private String assessmentName;

    @ColumnInfo(name = "value")
    private String value;

    @ColumnInfo(name = "mark")
    private String mark;

    public Assessment(String assessmentName, String value, String mark) {
        this.assessmentName = assessmentName;
        this.value = value;
        this.mark = mark;
    }

    public int getId() {
        return id;
    }

    public String getAssessmentName() {
        return assessmentName;
    }

    public String getValue() {
        return value;
    }

    public String getMark() {
        return mark;
    }

    public void setId(@NonNull int id) {
        this.id = id;
    }
}
