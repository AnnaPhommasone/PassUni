package com.pumpkinsoup.wamcalculator;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class SubjectDialog extends AppCompatDialogFragment {

    private EditText etUnitName;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private EditText etCreditPoints;
    private EditText etMark;
    private SubjectDialogListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_subject_details, null);
        builder.setView(view)
                .setTitle("Add a new subject")
                .setNegativeButton("Cancel", (dialog, which) -> {

                })
                .setPositiveButton("Add", (dialog, which) -> {
                    int radioId = radioGroup.getCheckedRadioButtonId();
                    radioButton = view.findViewById(radioId);

                    String subjectName = etUnitName.getText().toString();
                    String yearLevel = radioButton.getText().toString();
                    String creditPts = etCreditPoints.getText().toString();
                    String mark = etMark.getText().toString();
                    listener.addSubject(subjectName, yearLevel, creditPts, mark);
                });

        etUnitName = view.findViewById(R.id.et_subject_name);
        radioGroup = view.findViewById(R.id.radio_group);
        etCreditPoints = view.findViewById(R.id.et_credit_pts);
        etMark = view.findViewById(R.id.et_mark);

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (SubjectDialogListener) getTargetFragment();
        } catch (Exception e) {
            throw new ClassCastException(context.toString() +
                    "must implement UnitDialogListener");
        }
    }

    public interface SubjectDialogListener {
        void addSubject(String unitName, String yearLevel, String creditPoints, String mark);
    }

}
