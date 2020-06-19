package com.example.doc_mech;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class offers extends AppCompatActivity {

    private EditText offer_name;
    private EditText offer_desc;
    private Button add_offer;
    private RecyclerView recyclerView;
    private String Shop_name;
    private ArrayList<Add_offer_class> list;
    private Offer_adapter adapter;
    private ImageView redeem;
    private String Offer_name,Offer_desc;
    private ArrayList<String> offer_list;
    private String del_offer_name;
    private ProgressDialog dialog;
    private TextView swipe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offers);

        setUI();
        get_shop_name();
        fetch_offer_data();
        redeem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),redeem_points.class));
            }
        });

        add_offer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 Offer_name = offer_name.getText().toString();
                 Offer_desc = offer_desc.getText().toString();
                if (Offer_name.isEmpty() || Offer_desc.isEmpty()) {
                    Toast.makeText(offers.this, "Enter all fields", Toast.LENGTH_SHORT).show();
                } else {
                    check_for_redundancy();
                }
            }
        });

        fetch_data();
    }


    private void fetch_offer_data() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("MECHS").child(FirebaseAuth.getInstance().getUid()).child("OFFERS");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    offer_list.add(snapshot.child("offer_name").getValue().toString());
                }
                add_offer.setEnabled(true);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void check_for_redundancy() {
        boolean bool = true;
        if (offer_list.size()>0) {
            for (String i : offer_list) {
                if (i.equals(Offer_name)) {
                    Toast.makeText(this, "Offer aldready exists", Toast.LENGTH_SHORT).show();
                    offer_name.setText("");
                    offer_desc.setText("");
                    bool = false;
                    break;
                }
            }

            if (bool) {
                add_new_offer();
            }
        }
        else
        {
            add_new_offer();
        }
    }

    private void add_new_offer() {
        Add_offer_class obj = new Add_offer_class(Offer_name, Offer_desc, Shop_name);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("MECHS").child(FirebaseAuth.getInstance().getUid()).child("OFFERS");
        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("OFFERS").child(FirebaseAuth.getInstance().getUid());
        reference1.push().setValue(obj);
        reference.push().setValue(obj).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(offers.this, "Offer added successfully", Toast.LENGTH_SHORT).show();
                    offer_name.setText("");
                    offer_desc.setText("");
                } else {
                    Toast.makeText(offers.this, "Offer add failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void fetch_data()
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("MECHS").child(FirebaseAuth.getInstance().getUid()).child("OFFERS");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    Add_offer_class data = snapshot.getValue(Add_offer_class.class);
                    list.add(data);
                    Log.d("values",snapshot.getValue().toString());
                }
                if(list.size()>0)
                {
                    swipe.setVisibility(View.VISIBLE);
                    adapter.notifyDataSetChanged();}
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void get_shop_name()
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("MECHS").child(FirebaseAuth.getInstance().getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    Shop_name = snapshot.child("shop_name").getValue().toString();
                    break;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void setUI()
    {
        swipe = (TextView) findViewById(R.id.swipe);

        redeem = (ImageView) findViewById(R.id.redeem);

        offer_name = (EditText) findViewById(R.id.offer_name);
        offer_desc = (EditText) findViewById(R.id.offer_desc);

        add_offer = (Button) findViewById(R.id.add);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        list = new ArrayList<>();
        adapter = new Offer_adapter(getApplicationContext(),list);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        offer_list = new ArrayList<>();

        add_offer.setEnabled(false);

        new ItemTouchHelper(itemtouchhelper).attachToRecyclerView(recyclerView);
        dialog = new ProgressDialog(this);
        dialog.setMessage("OFFER DELETING..");
    }

    ItemTouchHelper.SimpleCallback itemtouchhelper = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            dialog.show();
            del_offer_name = list.get(viewHolder.getAdapterPosition()).getOffer_name();
            list.remove(viewHolder.getAdapterPosition());
            adapter.notifyDataSetChanged();
            Toast.makeText(offers.this, "Offer deleted Successfully", Toast.LENGTH_SHORT).show();
            delete_offer();
            delete_admin_offer();
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addBackgroundColor(ContextCompat.getColor(offers.this, R.color.colorAccent))
                    .addActionIcon(R.drawable.delete_icon)
                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

    private void delete_admin_offer() {
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("OFFERS").child(FirebaseAuth.getInstance().getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    if (snapshot.child("offer_name").getValue().toString().equals(del_offer_name))
                    {
                        reference.child(snapshot.getKey()).setValue(null);
                        dialog.dismiss();
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void delete_offer() {
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("MECHS").child(FirebaseAuth.getInstance().getUid()).child("OFFERS");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    if (snapshot.child("offer_name").getValue().toString().equals(del_offer_name))
                    {
                        reference.child(snapshot.getKey()).setValue(null);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
