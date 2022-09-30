package com.payment.payneed.commission;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.payment.payneed.R;
import com.payment.payneed.commission.gsonModel.CommissionModel;
import com.payment.payneed.utill.SharedPrefs;

import java.util.List;

public class CommissionDataAdapter extends BaseAdapter {

    private List<CommissionModel> dataList;
    private LayoutInflater layoutInflater = null;
    private Context context;
    private String type, title, lable, desc;

    public CommissionDataAdapter(Context context, List<CommissionModel> dataList) {
        this.context = context;
        this.dataList = dataList;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int i) {
        return dataList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 1;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        SpannableStringBuilder builder;
        final Holder holder;
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.commition_data_row, parent, false);
            holder = new Holder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        final CommissionModel model = dataList.get(position);
        String slug = SharedPrefs.getValue(context, SharedPrefs.ROLE_SLUG);
        String Record = "Provider : " + model.getProvider().getName();
        Record += "\n" + "Value : " + model.getValue();


//        Record += "\n" + "Scheme Id : " + model.getSchemeId() + "\n";
        holder.titleTv.setText(model.getProvider().getName());
        holder.value.setText( model.getValue());
        return convertView;
    }

    public void UpdateList(List<CommissionModel> item) {
        dataList = item;
        notifyDataSetChanged();
    }

    public class Holder {
        private TextView titleTv, value;
        private ImageView imgDelete;

        public Holder(View view) {
            titleTv = (TextView) view.findViewById(R.id.titleTv);
            value = (TextView) view.findViewById(R.id.value);
        }
    }

}
