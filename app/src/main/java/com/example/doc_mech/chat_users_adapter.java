package com.example.doc_mech;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class chat_users_adapter extends RecyclerView.Adapter<chat_users_adapter.ViewHolder> {

   private Context context;
   private ArrayList<Chat_users_class> list;

    public chat_users_adapter(Context context, ArrayList<Chat_users_class> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public chat_users_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item,parent,false);
        return new chat_users_adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final chat_users_adapter.ViewHolder holder, final int position) {
        StorageReference imagereference = FirebaseStorage.getInstance().getReference().child(list.get(position).getUserid()).child("Images").child("Profile_image");
        imagereference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).fit().centerCrop().into(holder.profile_image);
            }
        });

        if (list.get(position).getStatus().equals("online"))
        {
            holder.status.setImageResource(R.drawable.ic_online);
            holder.status.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.status.setImageResource(R.drawable.ic_offline);
            holder.status.setVisibility(View.VISIBLE);
        }
        holder.usernmame.setText(list.get(position).getUsername());
        holder.usernmame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,Message.class);
                intent.putExtra("uid",list.get(position).getUserid());
                intent.putExtra("uname",list.get(position).getUsername());
                intent.putExtra("last_seen",list.get(position).getLastseen());
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
        public TextView usernmame;
        public ImageView profile_image,status;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            usernmame = (TextView) itemView.findViewById(R.id.username);
            profile_image = (ImageView) itemView.findViewById(R.id.profile_image);
            status = (ImageView) itemView.findViewById(R.id.status);
        }
    }


    public void updatelist(ArrayList<Chat_users_class> list1)
    {
        list = new ArrayList<>();
        list.addAll(list1);
        notifyDataSetChanged();
    }
}
