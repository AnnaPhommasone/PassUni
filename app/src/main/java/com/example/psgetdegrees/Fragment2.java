package com.example.psgetdegrees;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import com.example.psgetdegrees.assessmentProvider.Assessment;
import com.example.psgetdegrees.assessmentProvider.AssessmentValue;
import com.example.psgetdegrees.assessmentProvider.AssessmentViewModel;
import com.example.psgetdegrees.assessmentProvider.AssessmentViewModelFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


/**
 * A fragment that for the second tab. It calculates the unit/subject mark based on the
 * given assessments (an assessment has a name, value, and what mark the student got).
 */
public class Fragment2 extends Fragment implements DeleteListener {

    private RecyclerView recyclerView;
    private AssessRecyclerAdapter recyclerAdapter;
    private EditText etAssessmentName;
    private EditText etValue;
    private EditText etMark;
    private Button btnAddAssessment;
    private Button btnCalcMark;
    private TextView tvUnitMark;
    private ArrayList<Assessment> assessments;
    private AssessmentViewModel assessmentViewModel;

    public Fragment2() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        assessmentViewModel = ViewModelProviders.of(this,
                new AssessmentViewModelFactory(getActivity().getApplication()))
                .get(AssessmentViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment2_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etAssessmentName = view.findViewById(R.id.et_assessment_name);
        etValue = view.findViewById(R.id.et_value);
        etMark = view.findViewById(R.id.et_mark);
        btnAddAssessment = view.findViewById(R.id.btn_add_assessment);
        btnAddAssessment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String assessmentName = etAssessmentName.getText().toString();
                String value = etValue.getText().toString();
                String mark = etMark.getText().toString();
                addAssessment(assessmentName, value, mark);
            }
        });
        assessments = new ArrayList<>();
        recyclerAdapter = new AssessRecyclerAdapter(assessments, assessmentViewModel);
        recyclerAdapter.setDeleteListener(this);
        recyclerView = view.findViewById(R.id.recycler_view2);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(recyclerAdapter);

        assessmentViewModel.getAllAssessments().observe(getViewLifecycleOwner(), newData -> {
            recyclerAdapter.setData((ArrayList<Assessment>) newData);
        });

        btnCalcMark = view.findViewById(R.id.btn_calculate_mark);
        btnCalcMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CalculateUnitMark().execute();
            }
        });
        tvUnitMark = view.findViewById(R.id.tv_unit_mark);
    }

    // Adds the new Assessment to the assessments database, and clears the input fields on the
    // screen.
    private void addAssessment(String assessmentName, String value, String mark) {
        if (correctInputs(assessmentName, value, mark)) {
            Assessment newAssessment = new Assessment(assessmentName, value, mark);
            assessmentViewModel.insert(newAssessment);
            resetInputFields();
            etAssessmentName.requestFocus();
            Toast.makeText(getActivity(), "Assessment Added", Toast.LENGTH_SHORT).show();
        }
    }

    // Clears the input fields on the screen.
    private void resetInputFields() {
        etAssessmentName.getText().clear();
        etValue.getText().clear();
        etMark.getText().clear();
    }

    // Checks all fields are not empty and value of an assessment is between 1-100.
    // It it possible to get a mark over 100%.
    private boolean correctInputs(String assessmentName, String value, String mark) {
        if (assessmentName.length() == 0 || value.length() == 0 || mark.length() == 0) {
            Toast.makeText(getActivity(), "All Fields Are Required", Toast.LENGTH_LONG).show();
            return false;
        }
        if (Double.parseDouble(value) == 0 || Double.parseDouble(value) > 100) {
            Toast.makeText(getActivity(), "Value Must Be 1-100", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    // Checks if sum of values equal to 100 for all assessments.
    private boolean valuesAddTo100() {
        double sumValues = 0.0;
        for (int i = 0; i < assessments.size(); i++) {
            double val = Double.parseDouble(assessments.get(i).getValue());
            sumValues += val;
        }
        return sumValues == 100.0;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.unit_options_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_del_assess) {
            assessmentViewModel.deleteAll();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Deletes the Assessment identified by the given unique ID.
    @Override
    public void onClickDel(int id) {
        assessmentViewModel.deleteAssessment(id);
        recyclerAdapter.notifyDataSetChanged();
        Toast.makeText(getActivity(), "Assessment Deleted", Toast.LENGTH_SHORT).show();
    }

    // Uses the background thread to retrieve the given assessment details,
    // and calculate the unit/subject mark.
    // After calculating the mark, this class populates the text view with the unit/subject mark.
    private class CalculateUnitMark extends AsyncTask<Void, Void, Integer> {

        // Calculates the Unit mark.
        @Override
        protected Integer doInBackground(Void... params) {
            Integer overallMark = 0;
            try {
                List<AssessmentValue> assessmentValues = assessmentViewModel.getAssessmentValues();
                float unitMark = 0.0f;
                for (AssessmentValue a : assessmentValues) {
                    float mark = Float.parseFloat(a.mark) / 100;
                    float value = Float.parseFloat(a.value);
                    unitMark += (mark * value);
                }
                overallMark = Math.round(unitMark);
                return overallMark;
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            return overallMark;
        }

        // Populates a text view with the Unit mark.
        @Override
        protected void onPostExecute(Integer overallMark) {
            super.onPostExecute(overallMark);
            String msg = "Unit Mark: " + overallMark;
            tvUnitMark.setText(msg);
        }

    }
}
