package com.payment.payneed.views.package_manager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.payment.payneed.R;
import com.payment.payneed.views.package_manager.pkgmodel.CommissionModel;

import java.util.List;

public class ComissionPopupAdapter extends RecyclerView.Adapter<ComissionPopupAdapter.MyViewHolder> {
    private Context mContext;
    private List<CommissionModel> dataList;
    private  OnCommissionClickListener listener;

    public ComissionPopupAdapter(Context mContext, List<CommissionModel> dataList, OnCommissionClickListener listener) {
        this.mContext = mContext;
        this.dataList = dataList;
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.commission_charge_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        CommissionModel model = dataList.get(position);
        holder.title.setText(model.getTitle());

        holder.mainLayout.setOnClickListener(v -> {
            listener.onCommissionItemClicked(position,model.getTitle());
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
       TextView title;
       ConstraintLayout mainLayout;

        public MyViewHolder(View view) {
            super(view);

            title = view.findViewById(R.id.title);
            mainLayout = view.findViewById(R.id.mainLayout);

        }
    }
    interface OnCommissionClickListener{
        void onCommissionItemClicked(int position,String title);
    }

}