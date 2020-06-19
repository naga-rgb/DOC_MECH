package com.example.doc_mech;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class login extends AppCompatActivity {

    private ImageView profile_pic;
    private Button login,forget_pass;
    private String Email,Pass;
    private int ATTEMPTS = 3;
    private ProgressDialog dialog;
    private TextView attempts;
    private TextInputLayout pass,email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setui();
        forget_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Forgot_pass.class));
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                dialog.setCanceledOnTouchOutside(false);
                dialog.setCancelable(false);
                getvalues();
                if(validate())
                {
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(Email,Pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                dialog.dismiss();
                                update_pass();
                                Toast.makeText(login.this, "YOUR ARE AUTHENTICATED", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(),Home.class));
                                finish();
                            }
                            else
                            {
                                dialog.dismiss();
                                Toast.makeText(login.this, "Authentication failed...", Toast.LENGTH_SHORT).show();
                                --ATTEMPTS;
                                if (ATTEMPTS<0)
                                {
                                    login.setEnabled(false);
                                    attempts.setText("AUTHENTICATION FAILED..");
                                }
                                else
                                {
                                    attempts.setText("REMAINING ATTEMPTS: "+ATTEMPTS);
                                }
                            }
                        }
                    });
                }
                else
                {
                    dialog.dismiss();
                }
            }
        });
    }

    private void update_pass() {
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("MECHS").child(FirebaseAuth.getInstance().getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    reference.child(snapshot.getKey()).child("password").setValue(pass.getEditText().getText().toString());
                    break;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getvalues() {
        Email = email.getEditText().getText().toString();
        Pass = pass.getEditText().getText().toString();
    }

    private void setui()
    {
        profile_pic = (ImageView) findViewById(R.id.profile_pic);
        login = (Button) findViewById(R.id.login);
        forget_pass = (Button) findViewById(R.id.forget_pass);
        email = (TextInputLayout) findViewById(R.id.email);
        pass = (TextInputLayout) findViewById(R.id.pass);
        attempts = (TextView) findViewById(R.id.attempts);
        attempts.setText("REMAINING ATTEMPTS: "+ATTEMPTS);
        dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait while you are authenticated");
    }
    private boolean validate()
    {
        if(Email.isEmpty() || Pass.isEmpty())
        {
            Toast.makeText(this, "All fields have to be filled", Toast.LENGTH_SHORT).show();
            return false;
        }
        else
        {
            return true;
        }
    }


}
