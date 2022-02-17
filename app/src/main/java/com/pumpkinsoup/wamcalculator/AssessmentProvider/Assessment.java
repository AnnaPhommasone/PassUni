package com.pumpkinsoup.wamcalculator.AssessmentProvider;

import android.provider.BaseColumns;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import static com.pumpkinsoup.wamcalculator.AssessmentProvider.Assessment.TABLE_NAME;

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

    @ColumnInfo(name = "markNumerator")
    private String markNumerator;

    @ColumnInfo(name = "markDenominator")
    private String markDenominator;

    public Assessment(String assessmentName, String value, String markNumerator, String markDenominator) {
        this.assessmentName = assessmentName;
        this.value = value;
        this.markNumerator = markNumerator;
        this.markDenominator = markDenominator;
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

    public String getMarkNumerator() {
        return markNumerator;
    }

    public String getMarkDenominator() {
        return markDenominator;
    }

    public void setId(@NonNull int id) {
        this.id = id;
    }
}
