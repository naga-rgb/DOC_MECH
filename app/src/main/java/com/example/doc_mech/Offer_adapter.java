package com.example.doc_mech;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Offer_adapter extends RecyclerView.Adapter<Offer_adapter.ViewHolder> {

    private Context context;
    private ArrayList<Add_offer_class> list;

    public Offer_adapter(Context context, ArrayList<Add_offer_class> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public Offer_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.offers_layout,parent,false);
        return new Offer_adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Offer_adapter.ViewHolder holder, int position) {

        holder.offer_name.setText(list.get(position).getOffer_name());
        holder.offer_desc.setText(list.get(position).getOffer_description());
        holder.shop_name.setText(list.get(position).getShop_name());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView offer_name,offer_desc,shop_name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            offer_name = (TextView) itemView.findViewById(R.id.offer_name);
            offer_desc = (TextView) itemView.findViewById(R.id.offer_desc);
            shop_name = (TextView) itemView.findViewById(R.id.shop_name);
        }
    }
}
