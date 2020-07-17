package com.example.psgetdegrees;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.psgetdegrees.unitProvider.Unit;
import com.example.psgetdegrees.unitProvider.UnitViewModel;

import java.util.ArrayList;
import java.util.List;

public class UnitRecyclerAdapter extends RecyclerView.Adapter<UnitRecyclerAdapter.ViewHolder>{

    private ArrayList<Unit> data;
    private UnitViewModel unitViewModel;
    private DeleteListener deleteListener;

    public UnitRecyclerAdapter(List<Unit> data, UnitViewModel unitViewModel) {
        this.data = (ArrayList<Unit>) data;
        this.unitViewModel = unitViewModel;
    }

    public void setDeleteListener(DeleteListener callBack) {
        this.deleteListener = callBack;
    }

    public void setData(ArrayList<Unit> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UnitRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.unit_card_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull UnitRecyclerAdapter.ViewHolder holder, int position) {
        holder.tvUnitCode.setText(data.get(position).getUnitCode());
        holder.tvCreditPoints.setText(data.get(position).getCreditPoints());
        holder.tvMark.setText(data.get(position).getMark());
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteListener.onClickDel(data.get(position).getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvUnitCode;
        public TextView tvCreditPoints;
        public TextView tvMark;
        public ImageButton btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUnitCode = itemView.findViewById(R.id.et_unit_code);
            tvCreditPoints = itemView.findViewById(R.id.et_credit_points);
            tvMark = itemView.findViewById(R.id.et_mark);
            btnDelete = itemView.findViewById(R.id.btn_del);
        }
    }
}
