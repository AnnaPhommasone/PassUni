package com.pumpkinsoup.wamcalculator;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.pumpkinsoup.wamcalculator.AssessmentProvider.Assessment;
import com.pumpkinsoup.wamcalculator.AssessmentProvider.AssessmentValue;
import com.pumpkinsoup.wamcalculator.AssessmentProvider.AssessmentViewModel;

import java.util.ArrayList;
import java.util.List;


/**
 * A fragment for the second tab. It calculates the subject mark based on the
 * given assessments (an assessment has a name, value, and what mark the student got).
 */
public class SubjectMarkTabFragment extends Fragment implements DeleteListener, AssessmentDialog.AssessmentDialogListener,
        EditAssessmentDialog.EditAssessmentDialogListener, EditAssessmentListener {

    public static final String SUBJECT_MARK_KEY = "subjectMark";
    private int assessmentId;
    private RecyclerView recyclerView;
    private AssessmentRecyclerAdapter recyclerAdapter;
    private Button btnCalcMark;
    private TextView tvSubjectMark;
    private ArrayList<Assessment> assessments;
    private AssessmentViewModel assessmentViewModel;

    public SubjectMarkTabFragment() {}

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
        return inflater.inflate(R.layout.fragment_subject_mark, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        assessments = new ArrayList<>();
        recyclerAdapter = new AssessmentRecyclerAdapter(assessments);
        recyclerAdapter.setDeleteListener(this);
        recyclerAdapter.setEditAssessmentListener(this);
        recyclerView = view.findViewById(R.id.recycler_view_assessments);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(recyclerAdapter);

        assessmentViewModel.getAllAssessments().observe(getViewLifecycleOwner(), newData -> {
            recyclerAdapter.setData((ArrayList<Assessment>) newData);
        });

        btnCalcMark = view.findViewById(R.id.btn_calculate_mark);
        btnCalcMark.setOnClickListener(v -> new CalculateSubjectMark().execute());
        tvSubjectMark = view.findViewById(R.id.tv_unit_mark);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            tvSubjectMark.setText(savedInstanceState.getString(SUBJECT_MARK_KEY));
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences sP = this.getActivity().getSharedPreferences("Fragment2", Context.MODE_PRIVATE);
        tvSubjectMark.setText(sP.getString(SUBJECT_MARK_KEY, ""));
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SUBJECT_MARK_KEY, tvSubjectMark.getText().toString());
    }

    @Override
    public void onStop() {
        super.onStop();
        SharedPreferences sP = this.getActivity().getSharedPreferences("Fragment2", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sP.edit();
        editor.putString(SUBJECT_MARK_KEY, tvSubjectMark.getText().toString());
        editor.apply();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_subject_mark_options, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_clear_mark:
                tvSubjectMark.setText("");
                return true;
            case R.id.action_del_assess:
                assessmentViewModel.deleteAll();
                return true;
            case R.id.action_add_assess:
                openNewAssessmentDialogBox();
                return true;
            case R.id.action_info_unit:
                Intent intent = new Intent(getContext(), SubjectInfoActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClickDel(int id, String name) {
        assessmentViewModel.deleteAssessment(id);
        recyclerAdapter.notifyDataSetChanged();
        Toast.makeText(getActivity(), name + " deleted", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void addAssessment(String assessmentName, String value, String studentMark, String totalAssessmentMarks) {
        if (correctInputs(value, studentMark, totalAssessmentMarks)) {
            if (assessmentName.length() == 0) {
                assessmentName = "Assessment #" + (recyclerAdapter.getItemCount() + 1);
            }
            Assessment assessment = new Assessment(assessmentName, value, studentMark, totalAssessmentMarks);
            assessmentViewModel.insert(assessment);
            Toast.makeText(getActivity(), assessmentName + " added", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void editAssessment(int id, String assessmentName, String value, String studentMark,
                               String totalMarks, int position) {
        if (correctInputs(value, studentMark, totalMarks)) {
            if (assessmentName.length() == 0) {
                assessmentName = "Assessment #" + (position + 1);
            }
            assessmentId = id;
            Assessment assessment = new Assessment(assessmentName, value, studentMark, totalMarks);
            new UpdateAssessment().execute(assessment);
            Toast.makeText(getActivity(), assessmentName + " edited", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClickEdit(Assessment assessment, int position) {
        EditAssessmentDialog dialog = new EditAssessmentDialog();
        EditAssessmentDialog diaLogInstance = dialog.newInstance(assessment, position);
        diaLogInstance.setTargetFragment(SubjectMarkTabFragment.this, 1);
        diaLogInstance.show(getActivity().getSupportFragmentManager(), "EditAssessmentDialog");
    }

    private void openNewAssessmentDialogBox() {
        AssessmentDialog dialog = new AssessmentDialog();
        dialog.setTargetFragment(SubjectMarkTabFragment.this, 1);
        dialog.show(getActivity().getSupportFragmentManager(), "AssessmentDialog");
    }

    private boolean correctInputs(String value, String studentMark, String totalMarks) {
        if (value.length() == 0 || studentMark.length() == 0 || totalMarks.length() == 0) {
            Toast.makeText(getActivity(), "Invalid Input", Toast.LENGTH_LONG).show();
            return false;
        }
        if (Double.parseDouble(value) == 0 || Double.parseDouble(value) > 100) {
            Toast.makeText(getActivity(), "Value must be range 1-100", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private boolean valuesAddTo100() {
        double sumValues = 0.0;
        for (int i = 0; i < assessments.size(); i++) {
            double val = Double.parseDouble(assessments.get(i).getValue());
            sumValues += val;
        }
        return sumValues == 100.0;
    }

    private class CalculateSubjectMark extends AsyncTask<Void, Void, Integer> {
        @Override
        protected Integer doInBackground(Void... params) {
            Integer overallMark = 0;
            try {
                List<AssessmentValue> assessmentValues = assessmentViewModel.getAssessmentValues();
                float subjectMark = 0.0f;
                for (AssessmentValue a : assessmentValues) {
                    float value = Float.parseFloat(a.value);
                    float mark = Float.parseFloat(a.markNumerator) / Float.parseFloat(a.markDenominator);
                    subjectMark += (mark * value);
                }
                overallMark = Math.round(subjectMark);
                return overallMark;
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            return overallMark;
        }

        @Override
        protected void onPostExecute(Integer overallMark) {
            super.onPostExecute(overallMark);
            String displayedMark = "Subject Mark: " + overallMark;
            tvSubjectMark.setText(displayedMark);
        }
    }

    private class UpdateAssessment extends AsyncTask<Assessment, Assessment, Void> {
        @Override
        protected Void doInBackground(Assessment... assessments) {
            Assessment assessment = assessments[0];
            assessmentViewModel.update(assessmentId, assessment.getAssessmentName(),
                    assessment.getValue(), assessment.getMarkNumerator(), assessment.getMarkDenominator());
            return null;
        }
    }

}
