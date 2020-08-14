package com.example.psgetdegrees.assessmentProvider;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class AssessmentRepository {

    private AssessmentDao assessmentDao;
    private LiveData<List<Assessment>> assessments;

    public AssessmentRepository(Application application) {
        AssessmentDatabase db = AssessmentDatabase.getDatabase(application);
        assessmentDao = db.assessmentDao();
        assessments = assessmentDao.getAllAssessments();
    }

    LiveData<List<Assessment>> getAllAssessments() {
        return assessments;
    }

    void insert(Assessment assessment) {
        AssessmentDatabase.databaseWriteExecutor.execute(() -> assessmentDao.
                addAssessment(assessment));
    }

    void deleteAll() {
        AssessmentDatabase.databaseWriteExecutor.execute(() -> assessmentDao.deleteAllAssessments());
    }

    void deleteAssessment(int id) {
        AssessmentDatabase.databaseWriteExecutor.execute(() -> assessmentDao.deleteAssessment(id));
    }

    List<AssessmentValue> getAssessmentValues() {
        return assessmentDao.getAssessmentValues();
    }

    void update(int assessmentId, String assessmentName, String value, String markNumerator, String markDenominator) {
        assessmentDao.update(assessmentId, assessmentName, value, markNumerator, markDenominator);
    }
}
