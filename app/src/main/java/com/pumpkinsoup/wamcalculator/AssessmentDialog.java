package com.pumpkinsoup.wamcalculator;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

/**
 *  An AppCompatDialogFragment which represents a pop-up dialog box.
 *  The pop-up box allows users to input the details of an assessment for a subject,
 *  including the name, weighting, mark, and total marks for that assessment.
 */
public class AssessmentDialog extends AppCompatDialogFragment {

    private EditText etAssessmentName;
    private EditText etValue;
    private EditText etStudentMark;
    private EditText etTotalAssessmentMarks;
    private AssessmentDialogListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_assessment_details, null);
        builder.setView(view)
                .setTitle("Add a new assessment")
                .setNegativeButton("Cancel", (dialog, which) -> {

                })
                .setPositiveButton("Add", (dialog, which) -> {
                    String assessmentName = etAssessmentName.getText().toString();
                    String value = etValue.getText().toString();
                    String studentMark = etStudentMark.getText().toString();
                    String totalAssessmentMarks = etTotalAssessmentMarks.getText().toString();
                    listener.addAssessment(assessmentName, value, studentMark, totalAssessmentMarks);
                });
        etAssessmentName = view.findViewById(R.id.et_assessment_name);
        etValue = view.findViewById(R.id.et_value);
        etStudentMark = view.findViewById(R.id.et_student_mark);
        etTotalAssessmentMarks = view.findViewById(R.id.et_total_assessment_marks);
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (AssessmentDialogListener) getTargetFragment();
        } catch (Exception e) {
            throw new ClassCastException(context.toString() +
                    "must implement AssessmentDialogListener");
        }
    }

    public interface AssessmentDialogListener {
        void addAssessment(String assessmentName, String value, String studentMark, String totalAssessmentMarks);
    }
}
