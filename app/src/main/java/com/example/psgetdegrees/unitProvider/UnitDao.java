package com.example.psgetdegrees.unitProvider;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UnitDao {

    @Query("SELECT * FROM units")
    LiveData<List<Unit>> getAllUnits();

    @Insert
    void addUnit(Unit unit);

    @Query("DELETE FROM units WHERE unitId= :id")
    void deleteUnit(int id);

    @Query("DELETE FROM units")
    void deleteAllUnits();

    @Query("SELECT yearLevel, creditPoints, mark FROM units")
    List<UnitValue> getUnitValues();

}
