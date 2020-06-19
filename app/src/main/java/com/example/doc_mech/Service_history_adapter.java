package com.example.doc_mech;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Service_history_adapter extends RecyclerView.Adapter<Service_history_adapter.ViewHolder> {

    private Context context;
    private ArrayList<Old_Services_Class> list;


    public Service_history_adapter(Context context, ArrayList<Old_Services_Class> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public Service_history_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.service_history_layout,parent,false);
        return new Service_history_adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Service_history_adapter.ViewHolder holder, int position) {

        if (!list.get(position).getEngine().equals("Not Mentioned"))
        {
            holder.engine.setVisibility(View.VISIBLE);
            holder.engine.setText(list.get(position).getEngine());
        }
        if (!list.get(position).getSuspension().equals("Not Mentioned"))
        {
            holder.suspension.setVisibility(View.VISIBLE);
            holder.suspension.setText(list.get(position).getSuspension());
        }
        if (!list.get(position).getElectrical().equals("Not Mentioned"))
        {
            holder.electrical.setVisibility(View.VISIBLE);
            holder.electrical.setText(list.get(position).getElectrical());
        }
        if (!list.get(position).getOthers().equals("Not Mentioned"))
        {
            holder.others.setVisibility(View.VISIBLE);
            holder.others.setText(list.get(position).getOthers());
        }
        if (!list.get(position).getGeneral_services().equals("Not Mentioned"))
        {
            holder.general.setVisibility(View.VISIBLE);
            holder.general.setText(list.get(position).getGeneral_services());
        }
        if (!list.get(position).getPremium_services().equals("Not Mentioned"))
        {
            holder.premium.setVisibility(View.VISIBLE);
            holder.premium.setText(list.get(position).getPremium_services());
        }

        holder.bike_no.setText("Vehicle Number: "+list.get(position).getBike_no());
        holder.Owner.setText("OWner Name: "+list.get(position).getOwner());
        holder.kms.setText("Kms Driven: "+list.get(position).getKms());
        holder.address.setText("Address: "+list.get(position).getAddress());
        holder.bill.setText("Bill Amt: ₹ "+list.get(position).getBill());
        holder.date.setText("Date Booked: "+list.get(position).getDate());
        holder.date_billed.setText("Date Billed: "+list.get(position).getDate_billed());
        holder.Phone.setText("Mobile: "+list.get(position).getPhone());
        holder.email.setText("Email: "+list.get(position).getEmail());
        holder.shop_name.setText("Shop Name: "+list.get(position).getShopname());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView bike_no,Owner,Phone,email,kms,engine,suspension,electrical,others,general,premium,date,address,shop_name,bill,date_billed;
        public LinearLayout layout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            layout = (LinearLayout) itemView.findViewById(R.id.linear);
            bike_no = (TextView) itemView.findViewById(R.id.bike_no);
            Owner = (TextView) itemView.findViewById(R.id.Owner_name);
            Phone = (TextView) itemView.findViewById(R.id.Phone);
            email = (TextView) itemView.findViewById(R.id.email);
            kms = (TextView) itemView.findViewById(R.id.kms);
            engine = (TextView) itemView.findViewById(R.id.engine);
            suspension = (TextView) itemView.findViewById(R.id.suspension);
            electrical = (TextView) itemView.findViewById(R.id.electrical);
            others = (TextView) itemView.findViewById(R.id.others);
            general = (TextView) itemView.findViewById(R.id.general_service);
            premium = (TextView) itemView.findViewById(R.id.premium_service);
            date = (TextView) itemView.findViewById(R.id.date);
            address = (TextView) itemView.findViewById(R.id.address);
            shop_name = (TextView) itemView.findViewById(R.id.shop_name);
            bill = (TextView) itemView.findViewById(R.id.bill);
            date_billed = (TextView) itemView.findViewById(R.id.date_billed);
        }
    }

    public void updatelist(ArrayList<Old_Services_Class> list1)
    {
        list = new ArrayList<>();
        list.addAll(list1);
        notifyDataSetChanged();
    }
}
