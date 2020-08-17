package com.pumpkinsoup.psgetdegrees.assessmentProvider;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class AssessmentViewModelFactory implements ViewModelProvider.Factory {

    private final Application application;

    public AssessmentViewModelFactory(@NonNull Application application) {
        this.application = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass == AssessmentViewModel.class) {
            return (T) new AssessmentViewModel(application);
        }
        return null;
    }
}
