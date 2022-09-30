package com.payment.payneed.views.package_manager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.payment.payneed.R;
import com.payment.payneed.views.package_manager.pkgmodel.ChargeCommissionModel;

import java.util.List;

public class ComissionChargeAdapter extends RecyclerView.Adapter<ComissionChargeAdapter.MyViewHolder> {
    private Context mContext;
    private List<ChargeCommissionModel> dataList;
    OnCommissionClickListener listener;

    public ComissionChargeAdapter(Context mContext, List<ChargeCommissionModel> dataList, OnCommissionClickListener listener) {
        this.mContext = mContext;
        this.dataList = dataList;
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.charge_commission_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        ChargeCommissionModel model = dataList.get(position);
        holder.titleTv.setText(model.getProvider().getName());
        holder.inputEt.setText(model.getValue());


        holder.typeRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            model.setModified(true);
            if (checkedId == R.id.percentageBtn) {
                model.setType("percent");
            } else if (checkedId == R.id.flatBtn) {
                model.setType("flat");
            }
        });

        holder.inputEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                model.setValue(s.toString());
                model.setModified(true);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView titleTv;
        EditText inputEt;
        RadioGroup typeRadioGroup;

        public MyViewHolder(View view) {
            super(view);
            titleTv = view.findViewById(R.id.titleTv);
            inputEt = view.findViewById(R.id.inputEt);
            typeRadioGroup = view.findViewById(R.id.typeRadioGroup);
        }
    }

    interface OnCommissionClickListener {
        void updateCommission(int position);
    }

}