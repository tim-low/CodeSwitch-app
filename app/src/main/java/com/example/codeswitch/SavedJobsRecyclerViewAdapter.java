package com.example.codeswitch;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SavedJobsRecyclerViewAdapter extends RecyclerView.Adapter<SavedJobsRecyclerViewAdapter.RecyclerViewHolder> implements Filterable {

    private ArrayList<JobItem> savedJobItemList;
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
            savedJobItemList.clear();
            savedJobItemList.addAll((List)results.values);
            System.out.println(results.values);
            notifyDataSetChanged();
        }
    };

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        public ImageView RecyclerImageView;
        public TextView RecyclerTitleText;
        public TextView RecyclerCompanyText;
        public TextView RecyclerDatePostedText;
        public TextView RecyclerQualifiedText;
        public TextView RecyclerAppliedTextView;
        public ImageView RecyclerAppliedImageView;
        public View RecyclerCard;

        OnJobListener onJobListener;

        public RecyclerViewHolder(View itemView, OnJobListener onJobListener){
            super(itemView);
            RecyclerImageView = itemView.findViewById(R.id.SavedJobImage);
            RecyclerTitleText = itemView.findViewById(R.id.SavedJobTitleText);
            RecyclerCompanyText = itemView.findViewById(R.id.SavedJobCompanyText);
            RecyclerDatePostedText = itemView.findViewById(R.id.SavedJobDatePostedText);
            RecyclerQualifiedText = itemView.findViewById(R.id.SavedJobQualifiedText);
            RecyclerAppliedTextView = itemView.findViewById(R.id.AppliedText);
            RecyclerAppliedImageView = itemView.findViewById(R.id.AppliedImage);
            RecyclerCard = itemView;


            this.onJobListener = onJobListener;   //listener is set to the global listener inside each individual viewholder
            itemView.setOnClickListener(this);      //onclicklistener is attached to each individual viewholder
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            try {
                onJobListener.onJobClick(getAdapterPosition());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public boolean onLongClick(View v) {
            try {
                onJobListener.onJobLongClick(getAdapterPosition());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }
    }

    public SavedJobsRecyclerViewAdapter(ArrayList<JobItem> savedJobItemList, OnJobListener onJobListener) {
        this.savedJobItemList = savedJobItemList;
        fullJobItemList = new ArrayList<>(savedJobItemList);   //make copy of list

        this.mOnJobListener = onJobListener;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.saved_jobs_view, parent, false);
        RecyclerViewHolder rvh = new RecyclerViewHolder(v, mOnJobListener);        //global listener is passed to viewholder when constructed
        return rvh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        JobItem currentItem = savedJobItemList.get(position);

        holder.RecyclerImageView.setImageResource(currentItem.getJobImageResource());
        holder.RecyclerTitleText.setText(currentItem.getJobTitleText());
        holder.RecyclerCompanyText.setText(currentItem.getJobCompanyText());
        holder.RecyclerDatePostedText.setText(currentItem.getJobDatePostedText());
        String qualified = currentItem.isQualified()? "Qualified" : "Not Qualified";
        holder.RecyclerQualifiedText.setText(qualified);
        String appliedText = currentItem.getAppliedStatus()? "Applied" : "Not Applied";
        holder.RecyclerAppliedTextView.setText(appliedText);
        int appliedImage = currentItem.getAppliedStatus()? R.drawable.applied_image : R.drawable.not_applied_image;
        holder.RecyclerAppliedImageView.setImageResource(appliedImage);
        if (currentItem.isQualified()){
            holder.RecyclerCard.setBackgroundColor(Color.parseColor("#EEFFEF"));
        } else{
            holder.RecyclerCard.setBackgroundColor(Color.parseColor("#FFFFEB"));
        }

    }

    @Override
    public int getItemCount() {
        return savedJobItemList.size();
    }

    public interface OnJobListener {
        void onJobClick(int position) throws IOException;
        void onJobLongClick(int position) throws IOException;
    }
}
