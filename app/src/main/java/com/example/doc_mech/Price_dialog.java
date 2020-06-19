package com.example.doc_mech;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Price_dialog extends AppCompatDialogFragment {
    private EditText general,premium;
    private String General,Premium;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.price_list,null);
        general = (EditText) view.findViewById(R.id.general_service);
        premium = (EditText) view.findViewById(R.id.premium_service);
        builder
                .setView(view)
                .setTitle("Your Price List");
        get_prices();
        builder.setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                General = general.getText().toString().trim();
                Premium = premium.getText().toString().trim();
                if (General.isEmpty() || Premium.isEmpty())
                {
                    Toast.makeText(getActivity(), "Please enter the fields", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if (Integer.parseInt(General.substring(1))<=0 || Integer.parseInt(Premium.substring(1))<=0)
                    {
                        Toast.makeText(getActivity(), "Please enter a valid price", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        update_price();
                    }
                }
            }
        }).setNeutralButton("OK",null);
        return builder.create();
    }

    private void update_price() {
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("PRICE LIST").child(FirebaseAuth.getInstance().getUid());
        if (Character.isDigit(General.charAt(0)) && Character.isDigit(Premium.charAt(0)))
        {
            reference.child("general").setValue(General);
            reference.child("premium").setValue(Premium);
        }
        else
        {
            reference.child("general").setValue(General.substring(1));
            reference.child("premium").setValue(Premium.substring(1));
        }
        Toast.makeText(getActivity(), "Price Updated Successfully", Toast.LENGTH_SHORT).show();
    }

    private void get_prices() {
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("PRICE LIST").child(FirebaseAuth.getInstance().getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount()==2) {
                    general.setText("₹"+dataSnapshot.child("general").getValue().toString());
                    premium.setText("₹"+dataSnapshot.child("premium").getValue().toString());
                }
                if (dataSnapshot.getChildrenCount() == 0)
                {
                    Toast.makeText(getActivity(), "You have'nt added the price list please add it..", Toast.LENGTH_SHORT).show();
                }
                }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
