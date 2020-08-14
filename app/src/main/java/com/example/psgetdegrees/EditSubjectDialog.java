package com.example.psgetdegrees;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.psgetdegrees.unitProvider.Unit;

public class EditSubjectDialog extends AppCompatDialogFragment {

    public static final String SUBJECT_ID = "subjectId";
    public static final String SUBJECT_NAME_KEY = "subjectName";
    public static final String YEAR_LEVEL_KEY = "yearLevel";
    public static final String CREDIT_POINTS_KEY = "creditPoints";
    public static final String MARK_KEY = "mark";

    private int id;
    private EditText etSubjectName;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private EditText etCreditPoints;
    private EditText etMark;
    private EditSubjectDialogListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_unit_dialog, null);
        builder.setView(view)
                .setTitle("Edit unit")
                .setNegativeButton("Cancel", (dialog, which) -> {

                })
                .setPositiveButton("Edit", (dialog, which) -> {
                    // find which radio btn was clicked
                    int radioId = radioGroup.getCheckedRadioButtonId();
                    radioButton = view.findViewById(radioId);

                    String subjectName = etSubjectName.getText().toString();
                    String yearLevel = radioButton.getText().toString();
                    String creditPoints = etCreditPoints.getText().toString();
                    String mark = etMark.getText().toString();
                    listener.editSubject(id, subjectName, yearLevel, creditPoints, mark);
                });

        etSubjectName = view.findViewById(R.id.et_unit_name);
        radioGroup = view.findViewById(R.id.radio_group);
        etCreditPoints = view.findViewById(R.id.et_credit_pts);
        etMark = view.findViewById(R.id.et_mark);

        id = getArguments().getInt(SUBJECT_ID);
        etSubjectName.setText("Test subject name");
        etSubjectName.setText(getArguments().getString(SUBJECT_NAME_KEY));
        String yearLevel = getArguments().getString(YEAR_LEVEL_KEY);
        if (yearLevel.equals("1")) {
            radioGroup.check(R.id.radio_one);
        } else if (yearLevel.equals("2+")) {
            radioGroup.check(R.id.radio_two);
        }
        etCreditPoints.setText(getArguments().getString(CREDIT_POINTS_KEY));
        etMark.setText(getArguments().getString(MARK_KEY));

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (EditSubjectDialogListener) getTargetFragment();
        } catch (Exception e) {
            throw new ClassCastException(context.toString() +
                    "must implement EditSubjectDialogListener");
        }
    }

    public EditSubjectDialog newInstance(Unit unit) {
        EditSubjectDialog dialog = new EditSubjectDialog();
        Bundle args = new Bundle();
        args.putInt(SUBJECT_ID, unit.getId());
        args.putString(SUBJECT_NAME_KEY, unit.getUnitName());
        args.putString(YEAR_LEVEL_KEY, unit.getYearLevel());
        args.putString(CREDIT_POINTS_KEY, unit.getCreditPoints());
        args.putString(MARK_KEY, unit.getMark());
        dialog.setArguments(args);
        return dialog;
    }

    public interface EditSubjectDialogListener {
        void editSubject(int id, String subjectName, String yearLevel, String creditPoints, String mark);
    }
}
