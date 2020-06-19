package com.example.doc_mech;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class MobileAuthentication extends AppCompatActivity {

    private TextInputLayout phone;
    private Button send;
    private String Phone,VerificationId,Code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_authentication);

        setUI();
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Phone = phone.getEditText().getText().toString().trim();
                if (!Phone.isEmpty())
                {
                    start_verification();
                }
                else
                {
                    Toast.makeText(MobileAuthentication.this, "Enter a valid Mobile Number", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setUI() {
        phone = (TextInputLayout) findViewById(R.id.phone);
        send = (Button) findViewById(R.id.send);
    }

    private void start_verification() {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + Phone,
                60,
                TimeUnit.SECONDS,
                this,
                mcallbacks
        );
    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mcallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            Code = phoneAuthCredential.getSmsCode();
            if(!Code.isEmpty())
            {
                verify_otp();
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(MobileAuthentication.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            VerificationId = s;
        }
    };

    private void verify_otp() {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(VerificationId,Code);
        FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful())
                {
                    Intent intent = new Intent(getApplicationContext(),signup.class);
                    intent.putExtra("mobile",Phone);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    Toast.makeText(MobileAuthentication.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
