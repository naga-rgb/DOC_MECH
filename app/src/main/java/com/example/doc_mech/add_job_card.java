package com.example.doc_mech;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class add_job_card extends AppCompatActivity {

    private TextView bike_no, owner, phone, email,kms,general,premium,engine,electrical,suspension,others,date,address,Shopname;
    private String Bike_no, Owner, Phone, Email,Kms,General_service="Not Mentioned",Premium_service="Not Mentioned",Engine="Not Mentioned",Electrical="Not Mentioned",Suspension="Not Mentioned",Others="Not Mentioned";
    private Button send;
    private String Date,Address,User,shopname,uid;
    private DatabaseReference reference,reference2;
    private ArrayList<String> list;
    private String orderid;
    private TextView title0,title1;
    private String Message,Message1="ffede";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_job_card);
        setUI();
        getvalues();
        setenabled();
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate())
                {
                   send_message();
                   send_a_chat();
                }
            }
        });
    }

    private void send_a_chat() {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm aa");
        Date date = new Date();
        Chats Obj = new Chats(FirebaseAuth.getInstance().getUid(),uid,Message,format.format(date));
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.push().setValue(Obj);
    }


    private void send_message() {
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.SEND_SMS)== PackageManager.PERMISSION_GRANTED)
        {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(Phone,null,Message,null,null);
                add_job_card_vehicle();
        }
        else
        {
            ActivityCompat.requestPermissions(this,
                    new String[]
                            {
                                    Manifest.permission.SEND_SMS
                            },123);
            send_message();
        }
    }

    private void add_job_card_vehicle() {

        Retrieve_BikeData obj = new Retrieve_BikeData(orderid,Bike_no,Owner,Kms,Address,Email,Phone,General_service,Premium_service,Engine,Suspension,Electrical,Others,Date,User,shopname);
        reference2.child(orderid).push().setValue(obj);
        reference.child(orderid).push().setValue(obj).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {
                    Toast.makeText(add_job_card.this, "Added to job card successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),service_vehicles.class));
                    finish();
                }
                else
                {
                    Toast.makeText(add_job_card.this, "Added to job card failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setUI()
    {
        title0 = (TextView) findViewById(R.id.title0);
        title1 = (TextView) findViewById(R.id.title1);

        address = (TextView) findViewById(R.id.address);
        list = new ArrayList<String>();
        date = (TextView) findViewById(R.id.date);
        bike_no = (TextView) findViewById(R.id.bike_no);
        owner = (TextView) findViewById(R.id.Owner_Name);
        phone = (TextView) findViewById(R.id.phone);
        email = (TextView) findViewById(R.id.email);
        send = (Button) findViewById(R.id.Send);
        kms = (TextView) findViewById(R.id.kms);
        general = (TextView) findViewById(R.id.general_service);
        premium = (TextView) findViewById(R.id.premium_service);
        engine = (TextView) findViewById(R.id.engine);
        suspension = (TextView) findViewById(R.id.suspension);
        electrical = (TextView) findViewById(R.id.electrical);
        others = (TextView) findViewById(R.id.others);
        reference = FirebaseDatabase.getInstance().getReference("job carded vehicles");
        reference2 = FirebaseDatabase.getInstance().getReference("MECHS").child(FirebaseAuth.getInstance().getUid()).child("job carded vehicles");
        uid = getIntent().getStringExtra("uid");
        orderid = getIntent().getStringExtra("orderid");
        Bike_no = getIntent().getStringExtra("Bike_no");
        Owner = getIntent().getStringExtra("Owner");
        Email = getIntent().getStringExtra("Email");
        Phone = getIntent().getStringExtra("Phone");
        Address = getIntent().getStringExtra("Address");
        Kms = getIntent().getStringExtra("Kms");
        General_service = getIntent().getStringExtra("General");
        Premium_service = getIntent().getStringExtra("Premium");
        Engine = getIntent().getStringExtra("Engine");
        Suspension = getIntent().getStringExtra("Suspension");
        Electrical = getIntent().getStringExtra("Electrical");
        Others = getIntent().getStringExtra("Others");
        Date = getIntent().getStringExtra("Date");
        User = getIntent().getStringExtra("User");
        shopname = getIntent().getStringExtra("shopname");
        Shopname = (TextView) findViewById(R.id.shop_name);
        Shopname.setText("Shop Name: "+shopname);
        bike_no.setText("Vehicle Number: "+Bike_no);
        owner.setText("Owner Name: "+Owner);
        phone.setText("Mobile: "+Phone);
        email.setText("Email: "+Email);
        kms.setText("Kms Driven: "+Kms);
        general.setText(General_service);
        premium.setText(Premium_service);
        engine.setText(Engine);
        suspension.setText(Suspension);
        electrical.setText(Electrical);
        others.setText(Others);
        date.setText("Date: "+Date);
        address.setText("Address: "+"\n"+Address);
        Message = "DOC MECH Services"+"\n"+"Your vehicle have been taken service by "+shopname+" " +
                "and your order id is "+orderid +"\n"+"Happy Servicing";
        Log.d("Length",String.valueOf(Message.length()));

    }

    private void setenabled()
    {
        bike_no.setEnabled(false);
        owner.setEnabled(false);
        kms.setEnabled(false);
        email.setEnabled(false);
        phone.setEnabled(false);
        if (!General_service.equals("Not Mentioned")){
            general.setVisibility(View.VISIBLE);
            title0.setVisibility(View.VISIBLE);
            general.setEnabled(false);}

        if (!Premium_service.equals("Not Mentioned")){
            premium.setVisibility(View.VISIBLE);
            title0.setVisibility(View.VISIBLE);
            premium.setEnabled(false);}

        if (!Engine.equals("Not Mentioned")){
            engine.setVisibility(View.VISIBLE);
            title1.setVisibility(View.VISIBLE);
            engine.setEnabled(false);}

        if (!Suspension.equals("Not Mentioned")){
            general.setVisibility(View.VISIBLE);
            title1.setVisibility(View.VISIBLE);
            general.setEnabled(false);}

        if (!Electrical.equals("Not Mentioned")){
            electrical.setVisibility(View.VISIBLE);
            title1.setVisibility(View.VISIBLE);
            electrical.setEnabled(false);}

        if (!Others.equals("Not Mentioned")){
            others.setVisibility(View.VISIBLE);
            title1.setVisibility(View.VISIBLE);
            others.setEnabled(false);}

        date.setEnabled(false);
        address.setEnabled(false);
        Shopname.setEnabled(false);
    }

    private void getvalues()
    {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    for (DataSnapshot snapshot1 : snapshot.getChildren())
                    {
                        list.add(snapshot1.child("bike_no").getValue().toString());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private boolean validate()
    {
        if(list.size()>0)
        {
            for (String i  : list)
            {
                if(i.equals(Bike_no))
                {
                    Toast.makeText(this, "ALDREADY ADDED TO JOB CARD", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        }
        else
        {
            return true;
        }
        return true;
    }
}
