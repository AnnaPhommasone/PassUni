package com.example.psgetdegrees.unitProvider;

import android.provider.BaseColumns;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import static com.example.psgetdegrees.unitProvider.Unit.TABLE_NAME;

@Entity(tableName = TABLE_NAME)
public class Unit {
    public static final String TABLE_NAME = "units";
    public static final String COLUMN_ID = BaseColumns._ID;

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "unitId")
    private int id;

    @ColumnInfo(name = "unitCode")
    private String unitCode;

    @ColumnInfo(name = "creditPoints")
    private String creditPoints;

    @ColumnInfo(name = "mark")
    private String mark;

    public Unit(String unitCode, String creditPoints, String mark) {
        this.unitCode = unitCode;
        this.creditPoints = creditPoints;
        this.mark = mark;
    }

    public int getId()  {
        return id;
    }

    public String getUnitCode() {
        return unitCode;
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
