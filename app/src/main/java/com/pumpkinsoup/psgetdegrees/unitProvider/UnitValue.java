package com.pumpkinsoup.psgetdegrees.unitProvider;

import androidx.room.ColumnInfo;

/**
 * A class that represents a subset of the columns from the assessments database.
 * We use this class when we want to retrieve the unitCode column, creditPoints column, and
 * the mark column.
 */
public class UnitValue {
    @ColumnInfo(name = "yearLevel")
    public String yearLevel;

    @ColumnInfo(name = "creditPoints")
    public String creditPoints;

    @ColumnInfo(name = "mark")
    public String mark;

}
