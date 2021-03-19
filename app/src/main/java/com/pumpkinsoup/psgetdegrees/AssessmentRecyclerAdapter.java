package com.pumpkinsoup.psgetdegrees;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pumpkinsoup.psgetdegrees.AssessmentProvider.Assessment;

import java.util.ArrayList;

/**
 * A Recycler Adapter for the list of assessments (on the second tab - Subject Mark tab).
 */
public class AssessmentRecyclerAdapter extends RecyclerView.Adapter<AssessmentRecyclerAdapter.ViewHolder> {

    private ArrayList<Assessment> data;
    private DeleteListener deleteListener;
    private EditAssessmentListener editAssessmentListener;

    public AssessmentRecyclerAdapter(ArrayList<Assessment> data) {
        this.data = data;
    }

    public void setDeleteListener(DeleteListener callBack) {
        this.deleteListener = callBack;
    }

    public void setEditAssessmentListener(EditAssessmentListener callBack) {
        this.editAssessmentListener = callBack;
    }

    public void setData(ArrayList<Assessment> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AssessmentRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_assessment, parent, false);
        AssessmentRecyclerAdapter.ViewHolder viewHolder = new AssessmentRecyclerAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AssessmentRecyclerAdapter.ViewHolder holder, int position) {
        holder.tvAssessmentName.setText(data.get(position).getAssessmentName());
        holder.tvValue.setText(data.get(position).getValue());
        holder.tvMarkNum.setText(data.get(position).getMarkNumerator());
        holder.tvMarkDen.setText(data.get(position).getMarkDenominator());
        holder.btnDel.setOnClickListener(v -> deleteListener.onClickDel(data.get(position).getId(), data.get(position).getAssessmentName()));
        holder.btnEdit.setOnClickListener(v -> editAssessmentListener.onClickEdit(data.get(position), position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvAssessmentName;
        public TextView tvValue;
        public TextView tvMarkNum;
        public TextView tvMarkDen;
        public ImageButton btnDel;
        public ImageButton btnEdit;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAssessmentName = itemView.findViewById(R.id.tv_assessment_name);
            tvValue = itemView.findViewById(R.id.tv_value_val);
            tvMarkNum = itemView.findViewById(R.id.tv_student_mark);
            tvMarkDen = itemView.findViewById(R.id.tv_total_marks);
            btnDel = itemView.findViewById(R.id.btn_del);
            btnEdit = itemView.findViewById(R.id.btn_edit_assessment);
        }
    }
}
