package com.example.doc_mech;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class view_photos extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<Image> list;
    private Image_adapter adapter;
    private ProgressDialog dialog;
    private String del_url,UID;
    private Image obj;
    private boolean todelete = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_photos);
        setui();
        dialog.show();
        fetch_data();
    }


    private void setui()
    {
        dialog = new ProgressDialog(this);
        dialog.setMessage("Fetching your images..");
        list = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        adapter = new Image_adapter(getApplicationContext(),list);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        new ItemTouchHelper(itemtouchhelper).attachToRecyclerView(recyclerView);

    }

    private void fetch_data()
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("IMAGES").child(FirebaseAuth.getInstance().getUid()).child("uploads");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                list.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    Image data =  new Image();
                    data = snapshot.getValue(Image.class);
                    list.add(data);
                }
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    ItemTouchHelper.SimpleCallback itemtouchhelper  = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            final int position = viewHolder.getAdapterPosition();
            obj = list.get(viewHolder.getAdapterPosition());
            UID = list.get(viewHolder.getAdapterPosition()).getUID();
            del_url = list.get(viewHolder.getAdapterPosition()).getUrl();
            list.remove(viewHolder.getAdapterPosition());
            adapter.notifyDataSetChanged();
            Snackbar.make(recyclerView,"Image deleted",Snackbar.LENGTH_LONG)
                    .setAction("UNDO", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            todelete = true;
                            list.add(position,obj);
                            adapter.notifyDataSetChanged();
                        }
                    }).show();
           new Handler().postDelayed(new Runnable() {
               @Override
               public void run() {
                   if(todelete == false)
                   {
                       delete_photo();
                   }
               }
           },2500);
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addBackgroundColor(ContextCompat.getColor(view_photos.this, R.color.colorAccent))
                    .addActionIcon(R.drawable.delete_icon)
                    .create()
                    .decorate();

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

    private void delete_photo() {
        todelete = !todelete;
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("IMAGES").child(FirebaseAuth.getInstance().getUid()).child("uploads");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    if (snapshot.child("url").getValue().toString().equals(del_url))
                    {
                        reference.child(snapshot.getKey()).setValue(null);
                        final StorageReference reference = FirebaseStorage.getInstance().getReference("IMAGES/"+FirebaseAuth.getInstance().getUid()+"/"+UID);
                        reference.delete();
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
