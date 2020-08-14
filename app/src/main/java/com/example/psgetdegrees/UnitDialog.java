package com.example.psgetdegrees;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class UnitDialog extends AppCompatDialogFragment {

    private EditText etUnitName;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private EditText etCreditPoints;
    private EditText etMark;
    private UnitDialogListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_unit_dialog, null);
        builder.setView(view)
                .setTitle("Add a new subject")
                .setNegativeButton("Cancel", (dialog, which) -> {

                })
                .setPositiveButton("Add", (dialog, which) -> {
                    // find which radio btn was clicked
                    int radioId = radioGroup.getCheckedRadioButtonId();
                    radioButton = view.findViewById(radioId);

                    String unitName = etUnitName.getText().toString();
                    String yearLevel = radioButton.getText().toString();
                    String creditPts = etCreditPoints.getText().toString();
                    String mark = etMark.getText().toString();
                    listener.addUnit(unitName, yearLevel, creditPts, mark);
                });

        etUnitName = view.findViewById(R.id.et_unit_name);
        radioGroup = view.findViewById(R.id.radio_group);
        etCreditPoints = view.findViewById(R.id.et_credit_pts);
        etMark = view.findViewById(R.id.et_mark);

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (UnitDialogListener) getTargetFragment();
        } catch (Exception e) {
            throw new ClassCastException(context.toString() +
                    "must implement UnitDialogListener");
        }
    }

    public interface UnitDialogListener {
        void addUnit(String unitName, String yearLevel, String creditPoints, String mark);
    }

}
