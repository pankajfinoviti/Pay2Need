package com.payment.payneed.views.package_manager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.payment.payneed.R;
import com.payment.payneed.views.package_manager.pkgmodel.PackageComListModel;

import java.util.List;

public class PackageManagerAdapter extends RecyclerView.Adapter<PackageManagerAdapter.MyViewHolder> {
    private Context mContext;
    private List<PackageComListModel> dataList;
    private OnPackageListClickListener listener;

    public PackageManagerAdapter(Context mContext, List<PackageComListModel> dataList, OnPackageListClickListener listener) {
        this.mContext = mContext;
        this.dataList = dataList;
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.package_manager_list_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        PackageComListModel model = dataList.get(position);

        holder.tvSchemeId.setText(model.getScheme());
        holder.tvName.setText(model.getName());
        if (model.getStatus().equalsIgnoreCase("1")){
            holder.tvStatus.setText("Active");
        }else {
            holder.tvStatus.setText("Inactive");
        }

        holder.editBtn.setOnClickListener(v-> {
            listener.onEditBtnClicked(position,model);
        });

        holder.commissionBtn.setOnClickListener(v-> {
            listener.onViewCommissionClicked(position,model.getScheme());
        });

        holder.chargeBtn.setOnClickListener(v-> {
            listener.onChargeBtnClicked(position,model);
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
       TextView tvSchemeId,tvName,tvStatus;
       Button editBtn,commissionBtn,chargeBtn;

        public MyViewHolder(View view) {
            super(view);

            tvSchemeId = view.findViewById(R.id.tvSchemeId);
            tvName = view.findViewById(R.id.tvName);
            tvStatus = view.findViewById(R.id.tvStatus);
            editBtn = view.findViewById(R.id.editBtn);
            commissionBtn = view.findViewById(R.id.commissionBtn);
            chargeBtn = view.findViewById(R.id.chargeBtn);
        }
    }

    interface OnPackageListClickListener{
        void onEditBtnClicked(int position,PackageComListModel model);
        void onViewCommissionClicked(int position,String id);
        void onChargeBtnClicked(int position,PackageComListModel model);
    }

}