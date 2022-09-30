package com.payment.payneed.views.invoice;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.payment.payneed.R;
import com.payment.payneed.utill.MyUtil;
import com.payment.payneed.views.invoice.model.InvoiceModel;

import java.util.List;

public class InvoiceListAdapter extends BaseAdapter {

    private List<InvoiceModel> dataMap;
    private LayoutInflater layoutInflater = null;
    private Context context;
    private String invoiceType;

    public InvoiceListAdapter(Context context, List<InvoiceModel> dataMap) {
        this.context = context;
        this.dataMap = dataMap;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public InvoiceListAdapter(Context context, List<InvoiceModel> dataMap, String invoiceType) {
        this.context = context;
        this.dataMap = dataMap;
        this.invoiceType = invoiceType;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return dataMap.size();
    }

    @Override
    public Object getItem(int i) {
        return dataMap.get(i);
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
            convertView = layoutInflater.inflate(R.layout.m_invoice_row_layout, parent, false);
            holder = new Holder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        InvoiceModel model = dataMap.get(position);
        holder.title.setText(model.getKey());
        holder.value.setText(model.getValue());
        holder.value.setTextColor(context.getResources().getColor(R.color.textBlack));
        if (MyUtil.isNN(model.getKey()) && model.getKey().equalsIgnoreCase("status"))
            if (MyUtil.isNN(invoiceType) && invoiceType.equalsIgnoreCase("dmt"))
                changeColor(model.getValue(), holder.value);
        return convertView;
    }

    private void changeColor(String status, TextView tvTxnStatus) {
        switch (status) {
            case "success":
            case "txn":
            case "TXN":
            case "approved":
                tvTxnStatus.setTextColor(context.getResources().getColor(R.color.textGreen));
                break;
            case "pending":
                tvTxnStatus.setTextColor(context.getResources().getColor(R.color.text_yellow));
                break;
            case "rejected":
            case "failed":
            case "failure":
            case "fail":
            case "Failed":
                tvTxnStatus.setTextColor(context.getResources().getColor(R.color.textRed));
                break;
            default:
                tvTxnStatus.setTextColor(context.getResources().getColor(R.color.textBlack));
        }
    }

    public static class Holder {
        TextView value, title;
        ImageView icon;

        public Holder(View rowView) {
            value = (TextView) rowView.findViewById(R.id.tvValue);
            title = (TextView) rowView.findViewById(R.id.title);
        }
    }
}
