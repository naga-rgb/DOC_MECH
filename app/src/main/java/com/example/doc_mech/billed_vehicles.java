package com.example.doc_mech;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class billed_vehicles extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<Old_Services_Class> list;
    private Service_history_adapter adapter;
    private Toolbar toolbar;
    private SearchView searchView;
    private ImageView empty_image;
    private TextView empty_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billed_vehicles);

        setui();
        fetch_data();
    }

    private void setui()
    {
        empty_image = (ImageView) findViewById(R.id.empty_image);
        empty_text = (TextView) findViewById(R.id.empty_text);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        list = new ArrayList<>();

        adapter =  new Service_history_adapter(getApplicationContext(),list);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    private void fetch_data()
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("MECHS").child(FirebaseAuth.getInstance().getUid()).child("billed vehicles");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    Old_Services_Class data = snapshot.getValue(Old_Services_Class.class);
                    list.add(data);
                }
                if(list.size() >0)
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.my_menu,menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setQueryHint(getString(R.string.search));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                ArrayList<Old_Services_Class> mylist = new ArrayList<>();
                newText = newText.toLowerCase();
                for (Old_Services_Class data : list)
                {
                    if (data.getBike_no().toLowerCase().contains(newText))
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
