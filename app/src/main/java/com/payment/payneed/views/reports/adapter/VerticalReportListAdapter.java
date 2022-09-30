package com.payment.payneed.views.reports.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.payment.payneed.R;
import com.payment.payneed.model.ActivityListModel;

import java.util.List;

public class VerticalReportListAdapter extends RecyclerView.Adapter<VerticalReportListAdapter.ViewHolder> {

    private Context context;
    private List<ActivityListModel> activityListModels;

    public VerticalReportListAdapter(Context context, List<ActivityListModel> activityListModels) {
        this.context = context;
        this.activityListModels = activityListModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_report_list_row, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.name.setText(activityListModels.get(position).getTxt1());
        holder.image.setImageResource(activityListModels.get(position).getImage1());
    }

    @Override
    public int getItemCount() {
        return activityListModels.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.title);
            image = itemView.findViewById(R.id.image);
        }
    }
}
