package com.pumpkinsoup.psgetdegrees.SubjectProvider;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class SubjectViewModel extends AndroidViewModel {

    private SubjectRepository repository;
    private LiveData<List<Subject>> allSubjects;

    public SubjectViewModel(@NonNull Application application) {
        super(application);
        repository = new SubjectRepository(application);
        allSubjects = repository.getAllSubjects();
    }

    public LiveData<List<Subject>> getAllSubjects() {
        return allSubjects;
    }

    public void insert(Subject subject) {
        repository.insert(subject);
    }

    public void deleteAll() {
        repository.deleteAll();
    }

    public void deleteById(int id) {
        repository.deleteById(id);
    }

    public List<SubjectValue> getSubjectValues() {
        return repository.getSubjectValues();
    }

    public void update(int id, String subjectName, String yearLevel, String creditPoints, String mark) {
        repository.update(id, subjectName, yearLevel, creditPoints, mark);
    }

}
