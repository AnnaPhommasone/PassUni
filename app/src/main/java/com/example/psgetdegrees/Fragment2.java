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


/**
 * A fragment that for the second tab. It calculates the unit/subject mark based on the
 * given assessments (an assessment has a name, value, and what mark the student got).
 */
public class Fragment2 extends Fragment implements DeleteListener, AssessmentDialog.AssessmentDialogListener {

    private RecyclerView recyclerView;
    private AssessRecyclerAdapter recyclerAdapter;
    private Button btnCalcMark;
    private TextView tvUnitMark;
    private ArrayList<Assessment> assessments;
    private AssessmentViewModel assessmentViewModel;

    public Fragment2() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        assessmentViewModel = new ViewModelProvider(this).get(AssessmentViewModel.class);
//        assessmentViewModel = ViewModelProviders.of(this,
//                new AssessmentViewModelFactory(getActivity().getApplication()))
//                .get(AssessmentViewModel.class);
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
        btnCalcMark.setOnClickListener(v -> new CalculateUnitMark().execute());
        tvUnitMark = view.findViewById(R.id.tv_unit_mark);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.unit_options_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_del_assess:
                assessmentViewModel.deleteAll();
                return true;
            case R.id.action_add_assess:
                openDialog();
                return true;
            case R.id.action_info_unit:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Deletes the Assessment identified by the given unique ID.
    @Override
    public void onClickDel(int id, String name) {
        assessmentViewModel.deleteAssessment(id);
        recyclerAdapter.notifyDataSetChanged();
        Toast.makeText(getActivity(), name + " Deleted", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void addAssessment(String assessmentName, String value, String markNum, String markDen) {
        if (correctInputs(value, markNum, markDen)) {
            if (assessmentName.length() == 0) {
                assessmentName = "Assessment #" + recyclerAdapter.getItemCount() + 1;
            }
            Assessment assessment = new Assessment(assessmentName, value, markNum, markDen);
            assessmentViewModel.insert(assessment);
            Toast.makeText(getActivity(), assessmentName + " Added", Toast.LENGTH_SHORT).show();
        }
    }

//    private void addAssessment(String assessmentName, String value, String mark) {
//        if (correctInputs(value, mark)) {
//            Assessment newAssessment = new Assessment(assessmentName, value, mark);
//            assessmentViewModel.insert(newAssessment);
//            Toast.makeText(getActivity(), "Assessment Added", Toast.LENGTH_SHORT).show();
//        }
//    }

    // Opens a custom dialog box for users to enter in the details of the new assessment.
    private void openDialog() {
        AssessmentDialog dialog = new AssessmentDialog();
        dialog.setTargetFragment(Fragment2.this, 1);
        dialog.show(getActivity().getSupportFragmentManager(), "AssessmentDialog");
    }

    // Checks if value and mark fields are not empty, and value is in 0-100 range.
    // A student can score higher than the total amount of marks. e.g. 20.5/20 (as I have done b4:) )
    private boolean correctInputs(String value, String markNum, String markDen) {
        if (value.length() == 0 || markNum.length() == 0 || markDen.length() == 0) {
            Toast.makeText(getActivity(), "Invalid Input", Toast.LENGTH_LONG).show();
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
                    float value = Float.parseFloat(a.value);
                    float mark = Float.parseFloat(a.markNumerator) / Float.parseFloat(a.markDenominator);
                    unitMark += (mark * value);
                }
                overallMark = Math.round(unitMark);
                return overallMark;
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            return overallMark;
        }

        // Populates the text view with the Unit mark.
        @Override
        protected void onPostExecute(Integer overallMark) {
            super.onPostExecute(overallMark);
            String msg = "Unit Mark: " + overallMark;
            tvUnitMark.setText(msg);
        }

    }
}
