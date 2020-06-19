package com.example.doc_mech;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class redeem_points extends AppCompatActivity {

    private EditText email,new_coins,old_coins,pass;
    private Button view,redeem;
    private String Old_coins,New_coins,Email,mEmail,Pass,Uid;
    private int old_coin,new_coin;
    private FirebaseUser user;
    private String mPass;
    private ProgressDialog dialog,dialog2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redeem_points);
        user = FirebaseAuth.getInstance().getCurrentUser();
        mEmail = user.getEmail();
        setUI();
        get_pass();
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Email = email.getText().toString().trim();
                Pass = pass.getText().toString().trim();
                dialog.show();
                if(!Email.isEmpty() && !Pass.isEmpty())
                {
                    authenticate();
                }
                else
                {
                    dialog.dismiss();
                    Toast.makeText(redeem_points.this, "Enter your Email and Password", Toast.LENGTH_SHORT).show();
                }
            }
        });


        redeem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog2.show();
                New_coins = new_coins.getText().toString().trim();
                if(New_coins.isEmpty())
                {
                    dialog2.dismiss();
                    Toast.makeText(redeem_points.this, "Enter the New points", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    old_coin = Integer.parseInt(Old_coins);
                    new_coin = Integer.parseInt(New_coins);
                    if (new_coin<0 || new_coin>old_coin)
                    {
                        dialog2.dismiss();
                        Toast.makeText(redeem_points.this, "Please enter a valid torque coins", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        redeem_coins();
                    }
                }
            }
        });
    }

    private void get_pass() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("MECHS").child(FirebaseAuth.getInstance().getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    mPass = snapshot.child("password").getValue().toString();
                    break;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    private void redeem_coins() {
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User").child(Uid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    reference.child(snapshot.getKey()).child("torque_coins").setValue(New_coins).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                            {
                                dialog2.dismiss();
                                Toast.makeText(redeem_points.this, "Coins redeemed Successfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(),Home.class));
                                finish();
                            }
                            else
                            {
                                Toast.makeText(redeem_points.this, "Process Failed", Toast.LENGTH_SHORT).show();
                                dialog2.dismiss();
                            }
                        }
                    });
                    break;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void authenticate()
    {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(Email,Pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful())
                {
                    email.setVisibility(View.INVISIBLE);
                    pass.setVisibility(View.INVISIBLE);
                   Uid = FirebaseAuth.getInstance().getUid();
                   FirebaseAuth.getInstance().signOut();
                   FirebaseAuth.getInstance().signInWithEmailAndPassword(mEmail,mPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                       @Override
                       public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful())
                            {
                                view_coins();
                            }
                       }
                   });


                }
                else
                {
                    dialog.dismiss();
                    email.setText("");
                    pass.setText("");
                    Toast.makeText(redeem_points.this, "User Authentication Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void view_coins()
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User").child(Uid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    Old_coins = snapshot.child("torque_coins").getValue().toString();
                    old_coins.setText(snapshot.child("torque_coins").getValue().toString());
                    old_coins.setVisibility(View.VISIBLE);
                    new_coins.setVisibility(View.VISIBLE);
                    redeem.setVisibility(View.VISIBLE);
                    dialog.dismiss();
                    break;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void setUI() {

        dialog = new ProgressDialog(this);
        dialog2 = new ProgressDialog(this);
        dialog.setMessage("Please Wait Fetching details...");
        dialog2.setMessage("Processing...");

        email = (EditText) findViewById(R.id.email);
        pass = (EditText) findViewById(R.id.pass);
        new_coins = (EditText)findViewById(R.id.new_coins);
        old_coins = (EditText) findViewById(R.id.old_coins);

        old_coins.setEnabled(false);

        view = (Button) findViewById(R.id.view);
        redeem = (Button) findViewById(R.id.redeem);
    }
}
