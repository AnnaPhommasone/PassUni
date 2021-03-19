package com.pumpkinsoup.psgetdegrees;

import com.pumpkinsoup.psgetdegrees.SubjectProvider.Subject;

/**
 * An interface for listening to when users tap the edit button ('little pencil icon') on any
 * subject item in the recycler view on the first tab (WAM tab).
 */
public interface EditSubjectListener {
    void onClickEditSubject(Subject subject, int position);
}
