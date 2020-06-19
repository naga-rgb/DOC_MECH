package com.example.doc_mech;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class change_auth extends AppCompatActivity {

    private EditText email;
    private TextInputLayout pass;
    private Button update,update_pass;
    private String uid;
    private String Pass;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_auth);
        setUI();
        getpass();
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                final String Email = email.getText().toString();
                if(!Email.isEmpty()) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    user.updateEmail(Email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                dialog.dismiss();
                                Toast.makeText(change_auth.this, "Email updated successfully", Toast.LENGTH_SHORT).show();
                                String auth = FirebaseAuth.getInstance().getUid();
                                final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("MECHS").child(auth).child(uid);
                                reference.child("email").setValue(Email);
                            } else {
                                dialog.dismiss();
                                Toast.makeText(change_auth.this, "Email updation failure", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else
                {
                    dialog.dismiss();
                    Toast.makeText(change_auth.this, "Email is empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

        update_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                Pass = pass.getEditText().getText().toString();
                if (Pass.trim().isEmpty())
                {
                    dialog.dismiss();
                    Toast.makeText(change_auth.this, "Password Empty", Toast.LENGTH_SHORT).show();
                }
                else if(Pass.trim().length()<6)
                {
                    dialog.dismiss();
                    Toast.makeText(change_auth.this, "Password too short", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    user.updatePassword(Pass).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                dialog.dismiss();
                                Toast.makeText(change_auth.this, "Password updated successfully", Toast.LENGTH_SHORT).show();
                                String Auth = FirebaseAuth.getInstance().getUid();
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("MECHS").child(Auth).child(uid);
                                reference.child("password").setValue(Pass);
                            }
                            else
                            {
                                dialog.dismiss();
                                Toast.makeText(change_auth.this, "Password updation Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    private void getpass() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("MECHS").child(FirebaseAuth.getInstance().getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               for (DataSnapshot snapshot : dataSnapshot.getChildren())
               {
                   pass.getEditText().setText(snapshot.child("password").getValue().toString());
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
        dialog = new ProgressDialog(this);
        dialog.setMessage("updating..Please wait");
        email = (EditText)findViewById(R.id.email);
        pass = (TextInputLayout) findViewById(R.id.pass);
        update = (Button) findViewById(R.id.update);
        update_pass = (Button) findViewById(R.id.update_pass);

        uid = getIntent().getStringExtra("path");

        Log.d("auth",uid);
        email.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
    }
}
