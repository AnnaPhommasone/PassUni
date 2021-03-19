package com.pumpkinsoup.psgetdegrees.SubjectProvider;

import android.provider.BaseColumns;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import static com.pumpkinsoup.psgetdegrees.SubjectProvider.Subject.TABLE_NAME;

@Entity(tableName = TABLE_NAME)
public class Subject {
    public static final String TABLE_NAME = "subjects";
    public static final String COLUMN_ID = BaseColumns._ID;

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "subjectId")
    private int id;

    @ColumnInfo(name = "subjectName")
    private String subjectName;

    @ColumnInfo(name = "yearLevel")
    private String yearLevel;

    @ColumnInfo(name = "creditPoints")
    private String creditPoints;

    @ColumnInfo(name = "mark")
    private String mark;

    public Subject(String subjectName, String yearLevel, String creditPoints, String mark) {
        this.subjectName = subjectName;
        this.yearLevel = yearLevel;
        this.creditPoints = creditPoints;
        this.mark = mark;
    }

    public int getId()  {
        return id;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public String getYearLevel() {
        return yearLevel;
    }

    public String getCreditPoints() {
        return creditPoints;
    }

    public String getMark() {
        return mark;
    }

    public void setId(@NonNull int id) {
        this.id = id;
    }

}
