package com.pumpkinsoup.wamcalculator.SubjectProvider;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface SubjectDao {

    @Query("SELECT * FROM subjects")
    LiveData<List<Subject>> getAllSubjects();

    @Insert
    void addSubject(Subject subject);

    @Query("DELETE FROM subjects WHERE subjectId= :id")
    void deleteSubject(int id);

    @Query("DELETE FROM subjects")
    void deleteAllSubjects();

    @Query("SELECT yearLevel, creditPoints, mark FROM subjects")
    List<SubjectValue> getSubjectValues();

    @Query("UPDATE subjects SET subjectName = :subjectName, yearLevel = :yearLevel, creditPoints = :creditPoints, mark = :mark WHERE subjectId = :id")
    void update(int id, String subjectName, String yearLevel, String creditPoints, String mark);

}
