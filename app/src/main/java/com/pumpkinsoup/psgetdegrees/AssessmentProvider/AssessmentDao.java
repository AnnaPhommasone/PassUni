package com.pumpkinsoup.psgetdegrees.AssessmentProvider;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface AssessmentDao {

    @Query("SELECT * FROM assessments")
    LiveData<List<Assessment>> getAllAssessments();

    @Insert
    void addAssessment(Assessment assessment);

    @Query("DELETE FROM assessments WHERE assessmentId = :id")
    void deleteAssessment(int id);

    @Query("DELETE FROM assessments")
    void deleteAllAssessments();

    @Query("SELECT value, markNumerator, markDenominator FROM assessments")
    List<AssessmentValue> getAssessmentValues();

    @Query("UPDATE assessments SET assessmentName = :assessmentName, value = :value, markNumerator = :markNumerator, markDenominator = :markDenominator WHERE assessmentId = :assessmentId")
    void update(int assessmentId, String assessmentName, String value, String markNumerator, String markDenominator);
}
