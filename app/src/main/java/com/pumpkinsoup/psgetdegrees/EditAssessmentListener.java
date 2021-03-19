package com.pumpkinsoup.psgetdegrees;

import com.pumpkinsoup.psgetdegrees.AssessmentProvider.Assessment;

/**
 * An interface for listening to when the edit button ('little pencil icon') is clicked on any
 * assessment item in the recycler view on the second tab (Subject Mark tab).
 */
public interface EditAssessmentListener {
    void onClickEdit(Assessment assessment, int position);
}
