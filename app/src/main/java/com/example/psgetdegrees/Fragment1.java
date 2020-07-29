package com.example.psgetdegrees;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.psgetdegrees.unitProvider.Unit;
import com.example.psgetdegrees.unitProvider.UnitValue;
import com.example.psgetdegrees.unitProvider.UnitViewModel;

import java.util.ArrayList;
import java.util.List;


/**
 * A fragment for the first tab. It calculates a WAM (weighted average mark) based on the
 * given units from the user.
 */
public class Fragment1 extends Fragment implements DeleteListener, UnitDialog.UnitDialogListener {

    private RecyclerView recyclerView;
    private UnitRecyclerAdapter recyclerAdapter;
    private Button btnAddUnit;
    private EditText etUnitCode;
    private EditText etCreditPoints;
    private EditText etMark;
    private Button btnCalcWam;
    private TextView tvWam;
    private ArrayList<Unit> units;
    private UnitViewModel unitViewModel;

    public Fragment1() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        unitViewModel = new ViewModelProvider(this).get(UnitViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment1_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etUnitCode = view.findViewById(R.id.et_unit_code);
        etCreditPoints = view.findViewById(R.id.et_credit_points);
        etMark = view.findViewById(R.id.et_mark);
        btnAddUnit = view.findViewById(R.id.btn_add_unit);
        btnAddUnit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String unitCode = etUnitCode.getText().toString();
                String creditPoints = etCreditPoints.getText().toString();
                String mark = etMark.getText().toString();
                addUnit(unitCode, creditPoints, mark);
                resetInputFields();
                etUnitCode.requestFocus();
            }
        });
        units = new ArrayList<>();
        recyclerAdapter = new UnitRecyclerAdapter(units, unitViewModel);
        recyclerAdapter.setDeleteListener(this);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(recyclerAdapter);

        unitViewModel.getAllUnits().observe(getViewLifecycleOwner(), newData -> {
            recyclerAdapter.setData((ArrayList<Unit>) newData);
        });

        btnCalcWam = view.findViewById(R.id.btn_calculate_wam);
        btnCalcWam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CalculateWam().execute();
            }
        });
        tvWam = view.findViewById(R.id.tv_wam);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.wam_options_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_del_units:
                unitViewModel.deleteAll();
                return true;
            case R.id.action_add_unit:
                openDialog();
                return true;
            case R.id.action_info_wam:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Adds the new Unit to the units database.
    private void addUnit(String unitCode, String creditPoints, String mark) {
        if (unitDetailsCorrect(unitCode, creditPoints, mark)) {
            // Converts the faculty code of unit code to uppercase for better presentation
            String facultyCode = unitCode.substring(0, 3).toUpperCase();
            String code = facultyCode + unitCode.substring(3, 7);
            Unit newUnit = new Unit(code, creditPoints, mark);
            unitViewModel.insert(newUnit);
            Toast.makeText(getActivity(), "Unit Added", Toast.LENGTH_SHORT).show();
        }
    }

    // Clears the input text fields on the screen. This method is invoked after the user presses
    // the "Add Unit" button.
    private void resetInputFields() {
        etUnitCode.getText().clear();
        etCreditPoints.getText().clear();
        etMark.getText().clear();
    }

    // Checks if the given unit details (unit code, credit points, and mark)
    // satisfy conditions.
    private boolean unitDetailsCorrect(String unitCode, String creditPoints, String mark) {
        // Check that credit points and mark fields not empty
        if (unitCode.length() == 0 || creditPoints.length() == 0 || mark.length() == 0) {
            Toast.makeText(getActivity(), "All Fields Are Required", Toast.LENGTH_LONG).show();
            return false;
        }
        // Check if unit code has correct form (e.g. ABC1234)
        if (unitCode.length() != 7) {
            Toast.makeText(getActivity(), "Unit Code Is Not 7 Characters", Toast.LENGTH_LONG).show();
            return false;
        }
        String facultyCode = unitCode.substring(0, 3);
        String numCode = unitCode.substring(3, 7);
        if (!isAlpha(facultyCode) && !isInteger(numCode)) {
            Toast.makeText(getActivity(), "Unit Code Has Incorrect Form", Toast.LENGTH_LONG).show();
            return false;
        }
        // Check that mark is not over 100
        if (Integer.parseInt(mark) > 100) {
            Toast.makeText(getActivity(), "Mark Must be Between 0-100", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    // Checks if given string only contains letters
    private boolean isAlpha(String s) {
        char[] chars = s.toCharArray();
        for (char c : chars) {
            if (!Character.isLetter(c)) {
                return false;
            }
        }
        return true;
    }

    // Checks if given string is an integer
    private boolean isInteger(String s) {
        boolean isValidInt = false;
        try {
            Integer.parseInt(s);
            isValidInt = true;
        } catch (NumberFormatException e) {
            // s is not an integer
        }
        return isValidInt;
    }

    // Deletes the Unit identified by the given unique ID
    public void onClickDel(int id) {
        unitViewModel.deleteById(id);
        recyclerAdapter.notifyDataSetChanged();
        Toast.makeText(getActivity(), "Unit Deleted", Toast.LENGTH_SHORT).show();
    }

    private void openDialog() {
        UnitDialog unitDialog = new UnitDialog();
        unitDialog.setTargetFragment(Fragment1.this, 1);
        unitDialog.show(getActivity().getSupportFragmentManager(), "UnitDialog");
    }

    @Override
    public void addUnit(String unitName, String yearLevel, String creditPoints, String mark) {
        if (unitName.length() == 0) {
            unitName = "Unit " + recyclerAdapter.getItemCount() + 1;
        }
        Toast.makeText(getActivity(), unitName + " Added", Toast.LENGTH_SHORT).show();
    }

    // Uses the background thread to retrieve the Unit details from the units database,
    // and calculates the WAM based on the Units that have been added to the database.
    // Populates the text view with the calculated WAM.
    private class CalculateWam extends AsyncTask<Void, Void, Float> {

        // Calculates the WAM.
        @Override
        protected Float doInBackground(Void... params) {
            float wam = 0.0f;
            try {
                List<UnitValue> rows = unitViewModel.getUnitValues();
                if (rows.size() > 0) {
                    float sumWeightedMarks = 0.0f;
                    float sumWeightedCreditPoints = 0.0f;
                    for (UnitValue row : rows) {
                        int mark = Integer.parseInt(row.mark);
                        int creditPoints = Integer.parseInt(row.creditPoints);
                        int yearLevel = Integer.parseInt(row.unitCode.substring(3, 4));
                        double weight;
                        if (yearLevel >= 2) {
                            weight = 1.0;
                        } else {
                            weight = 0.5;
                        }
                        sumWeightedMarks += (mark * creditPoints * weight);
                        sumWeightedCreditPoints += (creditPoints * weight);
                    }
                    wam = sumWeightedMarks / sumWeightedCreditPoints;
                }
                return wam;
            } catch (Exception e) {
                Log.i("WAM", "Error" + e.toString());
            }
            return wam;
        }

        // Populates the text view with the calculated, rounded WAM.
        @Override
        protected void onPostExecute(Float wam) {
            super.onPostExecute(wam);
            String msg = "WAM: " + String.format("%.3f", wam);
            tvWam.setText(msg);
        }
    }
}
