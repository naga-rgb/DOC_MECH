package com.example.doc_mech;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class service_vehicles extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<Retrieve_BikeData> list;
    private service_vehicles_adapter adapter;
    private String shopname;
    private DatabaseReference reference;
    private Toolbar toolbar;
    private SearchView searchView;
    private ProgressDialog loading_dialog;
    private ImageView empty_image;
    private TextView empty_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_vehicles);

        loading_dialog = new ProgressDialog(this);
        loading_dialog.setMessage("Fetching your Bending Services...!");
        loading_dialog.show();
        setui();
        fetch_data();
    }

    private void setui()
    {
        empty_image = (ImageView) findViewById(R.id.empty_image);
        empty_text = (TextView) findViewById(R.id.empty_text);

        reference = FirebaseDatabase.getInstance().getReference("MECHS").child(FirebaseAuth.getInstance().getUid()).child("bending services");
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        list = new ArrayList<>();
        adapter = new service_vehicles_adapter(getApplicationContext(),list);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void fetch_data()
    {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                        for (DataSnapshot snapshot1 : snapshot.getChildren())
                        {
                            Retrieve_BikeData data = snapshot1.getValue(Retrieve_BikeData.class);
                            list.add(data);
                        }
                }
                if(list.size()>0)
                {
                    adapter.notifyDataSetChanged();
                    loading_dialog.dismiss();
                }
                else
                {
                    recyclerView.setVisibility(View.GONE);
                    empty_image.setVisibility(View.VISIBLE);
                    empty_text.setVisibility(View.VISIBLE);
                    loading_dialog.dismiss();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu,menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setQueryHint(getString(R.string.search));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                newText = newText.toLowerCase();
                ArrayList<Retrieve_BikeData> mylist = new ArrayList<>();
                for (Retrieve_BikeData data : list)
                {
                    String bike_no = data.getBike_no().toLowerCase();
                    if(bike_no.contains(newText))
                    {
                        mylist.add(data);
                    }
                }
                adapter.updatelist(mylist);
                return false;
            }
        });
        return true;
    }
}


