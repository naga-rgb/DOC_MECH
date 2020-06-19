package com.example.doc_mech;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Image_adapter  extends RecyclerView.Adapter<Image_adapter.ViewHolder> {

    private Context context;
    private ArrayList<Image> list;

    public Image_adapter(Context context, ArrayList<Image> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public Image_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_view,parent,false);
        return new Image_adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Image_adapter.ViewHolder holder, int position) {
        Picasso.get().load(list.get(position).getUrl()).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public ImageView image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
        }
    }
}
