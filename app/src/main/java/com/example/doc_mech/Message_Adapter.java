package com.example.doc_mech;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class Message_Adapter extends RecyclerView.Adapter<Message_Adapter.ViewHolder> {

    private Context context;
    private ArrayList<Chats> list;
    private static int MSG_LEFT = 0;
    private static int MSG_RIGHT = 1;

    public Message_Adapter(Context context, ArrayList<Chats> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public Message_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == MSG_LEFT)
        {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_left,parent,false);
            return new Message_Adapter.ViewHolder(view);
        }
        else
        {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_right,parent,false);
            return new Message_Adapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull Message_Adapter.ViewHolder holder, int position) {
        holder.message.setText(list.get(position).getMessage());
        holder.time.setText(list.get(position).getTime());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView message;
        public TextView time;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            message = (TextView) itemView.findViewById(R.id.show_message);
            time = (TextView) itemView.findViewById(R.id.time);
        }
    }


    @Override
    public int getItemViewType(int position) {

        if (list.get(position).getSender().equals(FirebaseAuth.getInstance().getUid()))
        {
            return MSG_RIGHT;
        }
        else
        {
            return MSG_LEFT;
        }
    }
}
