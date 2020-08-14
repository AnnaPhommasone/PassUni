package com.example.psgetdegrees.unitProvider;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class UnitRepository {

    private UnitDao unitDao;
    private LiveData<List<Unit>> units;

    public UnitRepository(Application application) {
        UnitDatabase db = UnitDatabase.getDatabase(application);
        unitDao = db.unitDao();
        units = unitDao.getAllUnits();
    }

    LiveData<List<Unit>> getAllUnits() {
        return units;
    }

    void insert(Unit unit) {
        UnitDatabase.databaseWriteExecutor.execute(()-> unitDao.addUnit(unit));
    }

    void deleteAll() {
        UnitDatabase.databaseWriteExecutor.execute(()-> unitDao.deleteAllUnits());
    }

    void deleteById(int id) {
        UnitDatabase.databaseWriteExecutor.execute(()-> unitDao.deleteUnit(id));
    }

    List<UnitValue> getUnitValues() {
        return unitDao.getUnitValues();
    }

    void update(int id, String unitName, String yearLevel, String creditPoints, String mark) {
        unitDao.update(id, unitName, yearLevel, creditPoints, mark);
    }

}
