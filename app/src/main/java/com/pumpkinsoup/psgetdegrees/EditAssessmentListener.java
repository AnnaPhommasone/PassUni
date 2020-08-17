package com.pumpkinsoup.psgetdegrees;

import com.pumpkinsoup.psgetdegrees.assessmentProvider.Assessment;

public interface EditAssessmentListener {
    void onClickEdit(Assessment assessment, int position);
}
