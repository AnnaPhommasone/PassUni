package com.example.psgetdegrees;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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

    @Override
    public void addUnit(String unitName, String yearLevel, String creditPoints, String mark) {
        if (unitDetailsCorrect(creditPoints, mark)) {
            if (unitName.length() == 0) {
                unitName = "Unit #" + recyclerAdapter.getItemCount() + 1;
            }
            Unit unit = new Unit(unitName, yearLevel, creditPoints, mark);
            unitViewModel.insert(unit);
            Toast.makeText(getActivity(), unitName + " Added", Toast.LENGTH_SHORT).show();
        }
    }

    // Checks if the given unit details satisfy conditions.
    private boolean unitDetailsCorrect(String creditPoints, String mark) {
        if (creditPoints.length() == 0 && mark.length() == 0) {
            Toast.makeText(getActivity(), "Input required", Toast.LENGTH_LONG).show();
            return false;
        }
        if (creditPoints.length() == 0) {
            Toast.makeText(getActivity(), "Credit Points Required", Toast.LENGTH_LONG).show();
            return false;
        }
        if (mark.length() == 0) {
            Toast.makeText(getActivity(), "Mark Required", Toast.LENGTH_LONG).show();
            return false;
        }
        if (Integer.parseInt(mark) > 100) {
            Toast.makeText(getActivity(), "Invalid Mark", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    // Deletes the Unit identified by the given unique ID
    public void onClickDel(int id, String name) {
        unitViewModel.deleteById(id);
        recyclerAdapter.notifyDataSetChanged();
        Toast.makeText(getActivity(), name + " Deleted", Toast.LENGTH_SHORT).show();
    }

    // Opens a custom dialog for users to enter in the new unit's details.
    private void openDialog() {
        UnitDialog unitDialog = new UnitDialog();
        unitDialog.setTargetFragment(Fragment1.this, 1);
        unitDialog.show(getActivity().getSupportFragmentManager(), "UnitDialog");
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
                        String yearLevel = row.yearLevel;
                        int mark = Integer.parseInt(row.mark);
                        int creditPoints = Integer.parseInt(row.creditPoints);
                        double weight;
                        if (yearLevel.equals("1")) {
                            weight = 0.5;
                        } else {
                            weight = 1.0;
                        }
                        sumWeightedMarks += (mark * creditPoints * weight);
                        sumWeightedCreditPoints += (creditPoints * weight);
                    }
                    wam = sumWeightedMarks / sumWeightedCreditPoints;
                }
                return wam;
            } catch (Exception e) {
                Log.i("CalculateWam", "Error" + e.toString());
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
