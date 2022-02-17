package com.pumpkinsoup.wamcalculator.SubjectProvider;

import androidx.room.ColumnInfo;

/**
 * A class that represents a subset of the columns from the subjects database.
 * We use this class when we want to retrieve the subject name/code column, creditPoints column, and
 * the mark column.
 */
public class SubjectValue {
    @ColumnInfo(name = "yearLevel")
    public String yearLevel;

    @ColumnInfo(name = "creditPoints")
    public String creditPoints;

    @ColumnInfo(name = "mark")
    public String mark;

}
