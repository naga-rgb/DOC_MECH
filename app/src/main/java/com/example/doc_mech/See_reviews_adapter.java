package com.example.doc_mech;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class See_reviews_adapter extends RecyclerView.Adapter<See_reviews_adapter.ViewHolder> {

    public Context context;
    public ArrayList<Rating_class> list;
    public See_reviews_adapter(Context context, ArrayList<Rating_class> list)
    {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public See_reviews_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_ui,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull See_reviews_adapter.ViewHolder holder, final int position) {
        holder.username.setText(list.get(position).getUsername());
        holder.rating.setRating(Float.parseFloat(list.get(position).getRating()));
        holder.review.setText(list.get(position).getReview());
        holder.rating.setEnabled(false);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView username,review;
        public RatingBar rating;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            username = (TextView) itemView.findViewById(R.id.username);
            review = (TextView) itemView.findViewById(R.id.review);
            rating = (RatingBar) itemView.findViewById(R.id.rating);
        }
    }
}
