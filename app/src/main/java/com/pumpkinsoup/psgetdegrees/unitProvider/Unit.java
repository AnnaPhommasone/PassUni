package com.pumpkinsoup.psgetdegrees.unitProvider;

import android.provider.BaseColumns;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import static com.pumpkinsoup.psgetdegrees.unitProvider.Unit.TABLE_NAME;

@Entity(tableName = TABLE_NAME)
public class Unit {
    public static final String TABLE_NAME = "units";
    public static final String COLUMN_ID = BaseColumns._ID;

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "unitId")
    private int id;

    @ColumnInfo(name = "unitName")
    private String unitName;

    @ColumnInfo(name = "yearLevel")
    private String yearLevel;

    @ColumnInfo(name = "creditPoints")
    private String creditPoints;

    @ColumnInfo(name = "mark")
    private String mark;

    public Unit(String unitName, String yearLevel, String creditPoints, String mark) {
        this.unitName = unitName;
        this.yearLevel = yearLevel;
        this.creditPoints = creditPoints;
        this.mark = mark;
    }

    public int getId()  {
        return id;
    }

    public String getUnitName() {
        return unitName;
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
