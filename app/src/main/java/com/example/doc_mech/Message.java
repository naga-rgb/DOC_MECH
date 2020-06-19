package com.example.doc_mech;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Message extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<Chats> list;
    private Message_Adapter adapter;
    private String uid,Messages;
    private EditText message;
    private ImageButton send;
    private TextView username;
    private ImageView profile_image;
    private TextView lastseen;
    private String Lastseen;
    private String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);


        lastseen = (TextView) findViewById(R.id.lastseen);
        Lastseen = getIntent().getStringExtra("last_seen");
        get_image_url();
        if(!Lastseen.equals("now"))
        {
            lastseen.setVisibility(View.VISIBLE);
            lastseen.setText(getIntent().getStringExtra("last_seen"));
        }
        profile_image = (ImageView) findViewById(R.id.profile_image);
        get_image();
        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open_dialog();
            }
        });
        username = (TextView) findViewById(R.id.username);
        username.setText(getIntent().getStringExtra("uname"));
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        list = new ArrayList<>();
        adapter = new Message_Adapter(getApplicationContext(),list);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setAdapter(adapter);
        uid = getIntent().getStringExtra("uid");

        send = (ImageButton) findViewById(R.id.send);
        message = (EditText) findViewById(R.id.message);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send_message();
            }
        });
        read_messages();
    }

    private void open_dialog() {
        chat_pic_viewer viewer = new chat_pic_viewer();
        viewer.show(getSupportFragmentManager(),"Image");
    }

    private void get_image_url() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User_Profile_image").child(getIntent().getStringExtra("uid")).child("url");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                url = dataSnapshot.getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void get_image() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User_Profile_image").child(getIntent().getStringExtra("uid")).child("url");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Picasso.get().load(dataSnapshot.getValue().toString()).into(profile_image);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void send_message()
    {
        Messages = message.getText().toString();
        if (Messages.isEmpty())
        {
            Toast.makeText(this, "Cannot send Empty messages", Toast.LENGTH_SHORT).show();
        }
        else
        {
            SimpleDateFormat format = new SimpleDateFormat("HH:mm aa");
            Date date = new Date();
            Chats obj = new Chats(FirebaseAuth.getInstance().getUid(),uid,Messages,format.format(date));
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");
            reference.push().setValue(obj);
            message.setText("");
        }
    }


    private void read_messages()
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    Chats data = snapshot.getValue(Chats.class);
                    if ((data.getSender().equals(FirebaseAuth.getInstance().getUid())  && data.getReceiver().equals(uid)) ||
                            (data.getReceiver().equals(FirebaseAuth.getInstance().getUid()) && data.getSender().equals(uid)))
                    {
                        list.add(data);
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public String geturl()
    {
        return url;
    }
}
