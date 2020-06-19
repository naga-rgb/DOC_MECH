package com.example.doc_mech;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Forgot_pass extends AppCompatActivity {

    private EditText email;
    private Button send;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);

        setUI();

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email.getText().toString().trim().isEmpty())
                {
                    Toast.makeText(Forgot_pass.this, "Please enter your Email", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    send_mail();
                }
            }
        });
    }

    private void send_mail() {
        FirebaseAuth.getInstance().sendPasswordResetEmail(email.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {
                    Toast.makeText(Forgot_pass.this, "Password reset Email has been sent to your Email", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),login.class));
                    finish();
                }
                else
                {
                    Toast.makeText(Forgot_pass.this, "Email Not Send", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setUI() {
        email = (EditText) findViewById(R.id.email);
        send = (Button) findViewById(R.id.send);
    }
}
