package com.pumpkinsoup.psgetdegrees;

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

public class AssessmentDialog extends AppCompatDialogFragment {

    private EditText etAssessmentName;
    private EditText etValue;
    private EditText etMarkNum;
    private EditText etMarkDen;
    private AssessmentDialogListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_assessment_dialog, null);
        builder.setView(view)
                .setTitle("Add a new assessment")
                .setNegativeButton("Cancel", (dialog, which) -> {

                })
                .setPositiveButton("Add", (dialog, which) -> {
                    String assessmentName = etAssessmentName.getText().toString();
                    String value = etValue.getText().toString();
                    String markNum = etMarkNum.getText().toString();
                    String markDen = etMarkDen.getText().toString();
                    listener.addAssessment(assessmentName, value, markNum, markDen);
                });
        etAssessmentName = view.findViewById(R.id.et_assessment_name);
        etValue = view.findViewById(R.id.et_value);
        etMarkNum = view.findViewById(R.id.et_mark_numerator);
        etMarkDen = view.findViewById(R.id.et_mark_denominator);
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
        void addAssessment(String assessmentName, String value, String markNum, String markDen);
    }
}
