package com.example.psgetdegrees.unitProvider;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class UnitViewModel extends AndroidViewModel {

    private UnitRepository repository;
    private LiveData<List<Unit>> allUnits;

    public UnitViewModel(@NonNull Application application) {
        super(application);
        repository = new UnitRepository(application);
        allUnits = repository.getAllUnits();
    }

    public LiveData<List<Unit>> getAllUnits() {
        return allUnits;
    }

    public void insert(Unit unit) {
        repository.insert(unit);
    }

    public void deleteAll() {
        repository.deleteAll();
    }

    public void deleteById(int id) {
        repository.deleteById(id);
    }

    public List<UnitValue> getUnitValues() {
        return repository.getUnitValues();
    }

    public void update(int id, String unitName, String yearLevel, String creditPoints, String mark) {
        repository.update(id, unitName, yearLevel, creditPoints, mark);
    }

}
