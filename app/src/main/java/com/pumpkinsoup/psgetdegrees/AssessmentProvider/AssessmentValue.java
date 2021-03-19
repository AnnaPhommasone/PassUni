package com.pumpkinsoup.psgetdegrees.AssessmentProvider;

import androidx.room.ColumnInfo;

/**
 * An class that represents a subset of columns from the assessments database.
 * This class is used when we want to retrieve the value column and the mark column.
 */
public class AssessmentValue {
    @ColumnInfo(name = "value")
    public String value;

    @ColumnInfo(name = "markNumerator")
    public String markNumerator;

    @ColumnInfo(name = "markDenominator")
    public String markDenominator;
}
