package com.example.doc_mech;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class job_carded_vehicles_adapter extends RecyclerView.Adapter<job_carded_vehicles_adapter.ViewHolder> {

    private Context context;
    private ArrayList<addjobcardclass> list;

    public job_carded_vehicles_adapter(Context context, ArrayList<addjobcardclass> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public job_carded_vehicles_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.job_carded_vehicles_layout,parent,false);
        return new job_carded_vehicles_adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull job_carded_vehicles_adapter.ViewHolder holder, final int position) {
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

        holder.bike_no.setText(list.get(position).getBike_no());
        holder.owner_name.setText(list.get(position).getOwner());
        holder.phone.setText(list.get(position).getPhone());
        holder.email.setText(list.get(position).getEmail());
        holder.kms.setText(list.get(position).getKms());
        holder.date.setText(list.get(position).getDate());
        holder.address.setText(list.get(position).getAddress());
        holder.shopname.setText(list.get(position).getShopname());
        holder.Generate_Bill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,generate_bill.class);
                intent.putExtra("orderid",list.get(position).getOrderid());
                intent.putExtra("Bike_no",list.get(position).getBike_no());
                intent.putExtra("Owner",list.get(position).getOwner());
                intent.putExtra("Phone",list.get(position).getPhone());
                intent.putExtra("Email",list.get(position).getEmail());
                intent.putExtra("Kms",list.get(position).getKms());
                intent.putExtra("General",list.get(position).getGeneral_services());
                intent.putExtra("Premium",list.get(position).getPremium_services());
                intent.putExtra("Engine",list.get(position).getEngine());
                intent.putExtra("Suspension",list.get(position).getSuspension());
                intent.putExtra("Electrical",list.get(position).getElectrical());
                intent.putExtra("Others",list.get(position).getOthers());
                intent.putExtra("Date",list.get(position).getDate());
                intent.putExtra("Address",list.get(position).getAddress());
                intent.putExtra("User",list.get(position).getUser());
                intent.putExtra("shopname",list.get(position).getShopname());
                intent.putExtra("uid",list.get(position).getUser());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);


            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView bike_no,owner_name,phone,email,kms,general,premium,engine,suspension,electrical,others,date,address,shopname;
        public LinearLayout layout;
        public Button Generate_Bill;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            layout = (LinearLayout) itemView.findViewById(R.id.linear);
            bike_no = itemView.findViewById(R.id.bike_no);
            owner_name = itemView.findViewById(R.id.owner_name);
            kms = itemView.findViewById(R.id.kms);
            phone = itemView.findViewById(R.id.Phone);
            email = itemView.findViewById(R.id.email);
            general = itemView.findViewById(R.id.general_service);
            premium = itemView.findViewById(R.id.premium_service);
            engine = itemView.findViewById(R.id.engine);
            suspension = itemView.findViewById(R.id.suspension);
            electrical = itemView.findViewById(R.id.electrical);
            others = itemView.findViewById(R.id.others);
            date = itemView.findViewById(R.id.date);
            address = itemView.findViewById(R.id.address);
            shopname = itemView.findViewById(R.id.shop_name);
            Generate_Bill = itemView.findViewById(R.id.Generate_Bill);
        }
    }

    public void updatelist(ArrayList<addjobcardclass> list1)
    {
        list = new ArrayList<>();
        list.addAll(list1);
        notifyDataSetChanged();
    }
}
