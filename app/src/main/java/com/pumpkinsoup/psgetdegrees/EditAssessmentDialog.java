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

import com.pumpkinsoup.psgetdegrees.AssessmentProvider.Assessment;

/**
 * An AppCompatDialogFragment which represents the pop-up dialog box for editing an assessment
 * in the second tab (Subject Mark tab).
 * The pop-up box has the values of the assessment already filled in, and allows users to
 * edit the values.
 */
public class EditAssessmentDialog extends AppCompatDialogFragment {

    public static final String ASSESSMENT_ID_KEY = "assessmentId";
    public static final String ASSESSMENT_NAME_KEY = "assessmentName";
    public static final String VALUE_KEY = "value";
    public static final String MARK_NUM_KEY = "markNumerator";
    public static final String MARK_DEN_KEY = "markDenominator";
    public static final String POSITION_KEY = "position";

    private int id;
    private EditText etAssessmentName;
    private EditText etValue;
    private EditText etMarkNum;
    private EditText etMarkDen;
    private EditAssessmentDialogListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_assessment_details, null);
        builder.setView(view)
                .setTitle("Edit assessment")
                .setNegativeButton("Cancel", (dialog, which) -> {

                })
                .setPositiveButton("Edit", (dialog, which) -> {
                    String assessmentName = etAssessmentName.getText().toString();
                    String value = etValue.getText().toString();
                    String markNum = etMarkNum.getText().toString();
                    String markDen = etMarkDen.getText().toString();
                    int pos = getArguments().getInt(POSITION_KEY);
                    listener.editAssessment(id, assessmentName, value, markNum, markDen, pos);
                });

        etAssessmentName = view.findViewById(R.id.et_assessment_name);
        etValue = view.findViewById(R.id.et_value);
        etMarkNum = view.findViewById(R.id.et_mark_numerator);
        etMarkDen = view.findViewById(R.id.et_mark_denominator);

        id = getArguments().getInt(ASSESSMENT_ID_KEY);
        etAssessmentName.setText(getArguments().getString(ASSESSMENT_NAME_KEY));
        etValue.setText(getArguments().getString(VALUE_KEY));
        etMarkNum.setText(getArguments().getString(MARK_NUM_KEY));
        etMarkDen.setText(getArguments().getString(MARK_DEN_KEY));

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (EditAssessmentDialogListener) getTargetFragment();
        } catch (Exception e) {
            throw new ClassCastException(context.toString() +
                    "must implement EditAssessmentDialogListener");
        }
    }

    public EditAssessmentDialog newInstance(Assessment assessment, int position) {
        EditAssessmentDialog dialog = new EditAssessmentDialog();
        Bundle args = new Bundle();
        args.putInt(ASSESSMENT_ID_KEY, assessment.getId());
        args.putString(ASSESSMENT_NAME_KEY, assessment.getAssessmentName());
        args.putString(VALUE_KEY, assessment.getValue());
        args.putString(MARK_NUM_KEY, assessment.getMarkNumerator());
        args.putString(MARK_DEN_KEY, assessment.getMarkDenominator());
        args.putInt(POSITION_KEY, position);
        dialog.setArguments(args);
        return dialog;
    }

    public interface EditAssessmentDialogListener {
        void editAssessment(int id, String assessmentName, String value, String markNum, String markDen,
                            int position);
    }

}
