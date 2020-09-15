package com.example.codeswitch;

import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JobRecyclerViewAdapter extends RecyclerView.Adapter<JobRecyclerViewAdapter.RecyclerViewHolder> implements Filterable {

    private ArrayList<JobItem> jobItemList;
    private ArrayList<JobItem> fullJobItemList;

    private OnJobListener mOnJobListener; //global listener

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<JobItem> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(fullJobItemList);
            }
            else{
                String filterPattern = constraint.toString().toLowerCase().trim(); //lowercase and removes spaces

                for (JobItem item : fullJobItemList){
                    if (item.getJobTitleText().toLowerCase().contains(filterPattern)){
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            jobItemList.clear();
            jobItemList.addAll((List)results.values);
            System.out.println(results.values);
            notifyDataSetChanged();
        }
    };

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView RecyclerImageView;
        public TextView RecyclerTitleText;
        public TextView RecyclerCompanyText;
        public TextView RecyclerDatePostedText;
        public TextView RecyclerQualifiedText;
        public View RecyclerCard;

        OnJobListener onJobListener;

        public RecyclerViewHolder(View itemView, OnJobListener onJobListener){
            super(itemView);
            RecyclerImageView = itemView.findViewById(R.id.JobImage);
            RecyclerTitleText = itemView.findViewById(R.id.JobTitleText);
            RecyclerCompanyText = itemView.findViewById(R.id.CompanyText);
            RecyclerDatePostedText = itemView.findViewById(R.id.DatePostedText);
            RecyclerQualifiedText = itemView.findViewById(R.id.QualifiedText);
            RecyclerCard = itemView;

            this.onJobListener = onJobListener;   //listener is set to the global listener inside each individual viewholder
            itemView.setOnClickListener(this);      //onclicklistener is attached to each individual viewholder

        }

        @Override
        public void onClick(View view) {
            try {
                onJobListener.onJobClick(getAdapterPosition());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public JobRecyclerViewAdapter(ArrayList<JobItem> jobItemList, OnJobListener onJobListener) {
        this.jobItemList = jobItemList;
        fullJobItemList = new ArrayList<>(jobItemList);   //make copy of list

        this.mOnJobListener = onJobListener;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.job_view, parent, false);
        RecyclerViewHolder rvh = new RecyclerViewHolder(v, mOnJobListener);        //global listener is passed to viewholder when constructed
        return rvh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        JobItem currentItem = jobItemList.get(position);

        holder.RecyclerImageView.setImageResource(currentItem.getJobImageResource());
        holder.RecyclerTitleText.setText(currentItem.getJobTitleText());
        holder.RecyclerCompanyText.setText(currentItem.getJobCompanyText());
        holder.RecyclerDatePostedText.setText(currentItem.getJobDatePostedText());
        String qualified = currentItem.isQualified()? "Qualified" : "Not Qualified";
        holder.RecyclerQualifiedText.setText(qualified);
        if (currentItem.isQualified()){
            holder.RecyclerCard.setBackgroundColor(Color.parseColor("#EEFFEF"));
        } else{
            holder.RecyclerCard.setBackgroundColor(Color.parseColor("#FFFFEB"));
        }

    }

    @Override
    public int getItemCount() {
        return jobItemList.size();
    }

    public interface OnJobListener {
        void onJobClick(int position) throws IOException;
    }
}
