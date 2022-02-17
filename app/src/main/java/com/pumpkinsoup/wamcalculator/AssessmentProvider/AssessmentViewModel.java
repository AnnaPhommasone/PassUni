package com.pumpkinsoup.wamcalculator.AssessmentProvider;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class AssessmentViewModel extends AndroidViewModel {

    private AssessmentRepository repository;
    private LiveData<List<Assessment>> allAssessments;

    public AssessmentViewModel(@NonNull Application application) {
        super(application);
        repository = new AssessmentRepository(application);
        allAssessments = repository.getAllAssessments();
    }

    public LiveData<List<Assessment>> getAllAssessments() {
        return allAssessments;
    }

    public void insert(Assessment assessment) {
        repository.insert(assessment);
    }

    public void deleteAll() {
        repository.deleteAll();
    }

    public void deleteAssessment(int id) {
        repository.deleteAssessment(id);
    }

    public List<AssessmentValue> getAssessmentValues() {
        return repository.getAssessmentValues();
    }

    public void update(int assessmentId, String assessmentName, String value, String studentMark, String totalMarks) {
        repository.update(assessmentId, assessmentName, value, studentMark, totalMarks);
    }
}
