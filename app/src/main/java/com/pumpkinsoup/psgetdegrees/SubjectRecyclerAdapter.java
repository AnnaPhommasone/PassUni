package com.pumpkinsoup.psgetdegrees;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pumpkinsoup.psgetdegrees.SubjectProvider.Subject;
import com.pumpkinsoup.psgetdegrees.SubjectProvider.SubjectViewModel;

import java.util.ArrayList;
import java.util.List;

public class SubjectRecyclerAdapter extends RecyclerView.Adapter<SubjectRecyclerAdapter.ViewHolder>{

    private ArrayList<Subject> data;
    private SubjectViewModel unitViewModel;
    private DeleteListener deleteListener;
    private EditSubjectListener editUnitListener;

    public SubjectRecyclerAdapter(List<Subject> data, SubjectViewModel unitViewModel) {
        this.data = (ArrayList<Subject>) data;
        this.unitViewModel = unitViewModel;
    }

    public void setDeleteListener(DeleteListener callBack) {
        this.deleteListener = callBack;
    }

    public void setEditUnitListener(EditSubjectListener callBack) {
        this.editUnitListener = callBack;
    }

    public void setData(ArrayList<Subject> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SubjectRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_subject, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectRecyclerAdapter.ViewHolder holder, int position) {
        holder.tvUnitName.setText(data.get(position).getSubjectName());
        holder.tvYearLevel.setText(data.get(position).getYearLevel());
        holder.tvCreditPoints.setText(data.get(position).getCreditPoints());
        holder.tvMark.setText(data.get(position).getMark());
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteListener.onClickDel(data.get(position).getId(), data.get(position).getSubjectName());
            }
        });
        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editUnitListener.onClickEditSubject(data.get(position), position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvUnitName;
        public TextView tvYearLevel;
        public TextView tvCreditPoints;
        public TextView tvMark;
        public ImageButton btnDelete;
        public ImageButton btnEdit;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUnitName = itemView.findViewById(R.id.tv_unit_name);
            tvYearLevel = itemView.findViewById(R.id.tv_year_level_val);
            tvCreditPoints = itemView.findViewById(R.id.tv_credit_val);
            tvMark = itemView.findViewById(R.id.tv_mark_val);
            btnDelete = itemView.findViewById(R.id.btn_del);
            btnEdit = itemView.findViewById(R.id.btn_edit);
        }
    }
}
