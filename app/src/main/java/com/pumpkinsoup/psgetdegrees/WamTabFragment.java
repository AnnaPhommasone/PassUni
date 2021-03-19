package com.pumpkinsoup.psgetdegrees;

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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.pumpkinsoup.psgetdegrees.SubjectProvider.Subject;
import com.pumpkinsoup.psgetdegrees.SubjectProvider.SubjectValue;
import com.pumpkinsoup.psgetdegrees.SubjectProvider.SubjectViewModel;

import java.util.ArrayList;
import java.util.List;


/**
 * A fragment for the first tab. It calculates a WAM (weighted average mark) based on the
 * given units from the user.
 */
public class WamTabFragment extends Fragment implements DeleteListener, SubjectDialog.SubjectDialogListener,
        EditSubjectListener, EditSubjectDialog.EditSubjectDialogListener {

    public static final String WAM_KEY = "wam";
    private int currentId;
    private RecyclerView recyclerView;
    private SubjectRecyclerAdapter recyclerAdapter;
    private Button btnCalculateWam;
    private TextView tvWam;
    private ArrayList<Subject> subjects;
    private SubjectViewModel subjectViewModel;

    public WamTabFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        subjectViewModel = new ViewModelProvider(this).get(SubjectViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_wam, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        subjects = new ArrayList<>();
        recyclerAdapter = new SubjectRecyclerAdapter(subjects, subjectViewModel);
        recyclerAdapter.setDeleteListener(this);
        recyclerAdapter.setEditUnitListener(this);
        recyclerView = view.findViewById(R.id.recycler_view_subjects);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(recyclerAdapter);

        subjectViewModel.getAllSubjects().observe(getViewLifecycleOwner(), newData -> {
            recyclerAdapter.setData((ArrayList<Subject>) newData);
        });

        btnCalculateWam = view.findViewById(R.id.btn_calculate_wam);
        btnCalculateWam.setOnClickListener(v -> new CalculateWam().execute());
        tvWam = view.findViewById(R.id.tv_wam);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            tvWam.setText(savedInstanceState.getString(WAM_KEY));
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences sP = this.getActivity().getSharedPreferences("Fragment1", Context.MODE_PRIVATE);
        tvWam.setText(sP.getString(WAM_KEY, ""));
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(WAM_KEY, tvWam.getText().toString());
    }

    @Override
    public void onStop() {
        super.onStop();
        SharedPreferences sP = this.getActivity().getSharedPreferences("Fragment1", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sP.edit();
        editor.putString(WAM_KEY, tvWam.getText().toString());
        editor.apply();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_wam_options, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_clear_wam:
                tvWam.setText("");
                return true;
            case R.id.action_del_units:
                subjectViewModel.deleteAll();
                return true;
            case R.id.action_add_unit:
                openNewSubjectDialogBox();
                return true;
            case R.id.action_info_wam:
                Intent intent = new Intent(getContext(), WamInfoActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void addSubject(String unitName, String yearLevel, String creditPoints, String mark) {
        if (isSubjectDetailsCorrect(creditPoints, mark)) {
            if (unitName.length() == 0) {
                unitName = "Subject #" + (recyclerAdapter.getItemCount() + 1);
            }
            Subject unit = new Subject(unitName, yearLevel, creditPoints, mark);
            subjectViewModel.insert(unit);
            Toast.makeText(getActivity(), unitName + " Added", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClickEditSubject(Subject subject, int position) {
        EditSubjectDialog dialog = new EditSubjectDialog();
        EditSubjectDialog dialogInstance = dialog.newInstance(subject, position);
        dialogInstance.setTargetFragment(WamTabFragment.this, 1);
        dialogInstance.show(getActivity().getSupportFragmentManager(), "EditSubjectDialog");
    }

    @Override
    public void editSubject(int id, String subjectName, String yearLevel, String creditPoints, String mark,
                            int position) {
        if (isSubjectDetailsCorrect(creditPoints, mark)) {
            if (subjectName.length() == 0) {
                subjectName = "Subject #" + (position + 1);
            }
            currentId = id;
            Subject unit = new Subject(subjectName, yearLevel, creditPoints, mark);
            new UpdateSubject().execute(unit);
            Toast.makeText(getActivity(), subjectName + " edited", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClickDel(int id, String name) {
        subjectViewModel.deleteById(id);
        recyclerAdapter.notifyDataSetChanged();
        Toast.makeText(getActivity(), name + " deleted", Toast.LENGTH_SHORT).show();
    }

    private boolean isSubjectDetailsCorrect(String creditPoints, String mark) {
        if (creditPoints.length() == 0 || mark.length() == 0) {
            Toast.makeText(getActivity(), "Invalid Input", Toast.LENGTH_LONG).show();
            return false;
        }
        if (Integer.parseInt(mark) > 100) {
            Toast.makeText(getActivity(), "Invalid Mark", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void openNewSubjectDialogBox() {
        SubjectDialog subjectDialog = new SubjectDialog();
        subjectDialog.setTargetFragment(WamTabFragment.this, 1);
        subjectDialog.show(getActivity().getSupportFragmentManager(), "SubjectDialog");
    }

    private class CalculateWam extends AsyncTask<Void, Void, Float> {
        @Override
        protected Float doInBackground(Void... params) {
            float wam = 0.0f;
            try {
                List<SubjectValue> rows = subjectViewModel.getSubjectValues();
                if (rows.size() > 0) {
                    float sumWeightedMarks = 0.0f;
                    float sumWeightedCreditPoints = 0.0f;
                    for (SubjectValue row : rows) {
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

        @Override
        protected void onPostExecute(Float wam) {
            super.onPostExecute(wam);
            String msg = "WAM: " + String.format("%.3f", wam);
            tvWam.setText(msg);
        }
    }

    private class UpdateSubject extends AsyncTask<Subject, Subject, Void> {
        @Override
        protected Void doInBackground(Subject... subjects) {
            Subject subject = subjects[0];
            subjectViewModel.update(currentId, subject.getSubjectName(), subject.getYearLevel(),
                    subject.getCreditPoints(), subject.getMark());
            return null;
        }
    }

}
