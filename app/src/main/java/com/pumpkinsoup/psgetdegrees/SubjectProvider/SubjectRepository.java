package com.pumpkinsoup.psgetdegrees.SubjectProvider;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class SubjectRepository {

    private SubjectDao subjectDao;
    private LiveData<List<Subject>> subjects;

    public SubjectRepository(Application application) {
        SubjectDatabase db = SubjectDatabase.getDatabase(application);
        subjectDao = db.unitDao();
        subjects = subjectDao.getAllSubjects();
    }

    LiveData<List<Subject>> getAllSubjects() {
        return subjects;
    }

    void insert(Subject unit) {
        SubjectDatabase.databaseWriteExecutor.execute(()-> subjectDao.addSubject(unit));
    }

    void deleteAll() {
        SubjectDatabase.databaseWriteExecutor.execute(()-> subjectDao.deleteAllSubjects());
    }

    void deleteById(int id) {
        SubjectDatabase.databaseWriteExecutor.execute(()-> subjectDao.deleteSubject(id));
    }

    List<SubjectValue> getSubjectValues() {
        return subjectDao.getSubjectValues();
    }

    void update(int id, String subjectName, String yearLevel, String creditPoints, String mark) {
        subjectDao.update(id, subjectName, yearLevel, creditPoints, mark);
    }

}
