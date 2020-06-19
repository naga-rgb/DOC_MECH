package com.example.doc_mech;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class ex1 extends AppCompatActivity {

    private Button next;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ex1);

        if (FirebaseAuth.getInstance().getCurrentUser()!=null)
        {
            startActivity(new Intent(getApplicationContext(),Home.class));
            finish();
        }
        next = (Button) findViewById(R.id.next);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),login_signup.class));
                finish();
            }
        });
    }
}
