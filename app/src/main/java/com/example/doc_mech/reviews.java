package com.example.doc_mech;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class reviews extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<Rating_class> list;
    private See_reviews_adapter adapter;
    private ImageView empty_image;
    private TextView empty_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);
        setUI();
        fetch_review();
    }

    private void setUI()
    {

        empty_image = (ImageView) findViewById(R.id.empty_image);
        empty_text = (TextView) findViewById(R.id.empty_text);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        list = new ArrayList<>();
        adapter = new See_reviews_adapter(getApplicationContext(),list);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void fetch_review()
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("MECHS/"+ FirebaseAuth.getInstance().getUid()+"/REVIEWS");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    Rating_class data = snapshot.getValue(Rating_class.class);
                    list.add(data);
                }
                if(list.size()>0)
                {
                    adapter.notifyDataSetChanged();
                }
                else
                {
                    recyclerView.setVisibility(View.GONE);
                    empty_image.setVisibility(View.VISIBLE);
                    empty_text.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
