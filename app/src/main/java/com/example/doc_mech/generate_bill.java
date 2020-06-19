package com.example.doc_mech;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class generate_bill extends AppCompatActivity {

    private TextView bike_no, owner, phone, email,kms,general,premium,engine,electrical,suspension,others,date,address,Shopname,title0,title1;
    private EditText bill_amt;
    private String Date,Bike_no, Owner, Phone, Email,Kms,General_service="Not Mentioned",Premium_service="Not Mentioned",Engine="Not Mentioned",Electrical="Not Mentioned",Suspension="Not Mentioned",Others="Not Mentioned",Message,Bill;
    private Button send;
    private DatabaseReference reference,reference2,delref1,delref2,reference3,delref3,delre4;
    private String Address,User,Date_Billed,shopname;
    private static int REQUEST_CODE = 123;
    private String rating = "NULL";
    private String orderid,uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_bill);
        setUI();
        setEnabled();

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bill = bill_amt.getText().toString();
                open_dialog();
            }
        });
    }

    private void open_dialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmation")
                .setTitle("Send Confirmation Message to the Customer?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        send_confirmation_message();
                    }
                }).setNegativeButton("NO",null);
        builder.create().show();
    }


    private void send_confirmation_message() {
        if(Bill.trim().isEmpty())
        {
            Toast.makeText(generate_bill.this, "Please enter the bill amount", Toast.LENGTH_SHORT).show();
        }
        else
        {
            send.setEnabled(false);
            send_sms();
            send_a_chat();
            Old_Services_Class obj = new Old_Services_Class(orderid,Bike_no,Owner,Kms,Email,Phone,Engine,Suspension,Electrical,Others,General_service,Premium_service,Date,Date_Billed,Address,User,Bill,shopname,rating);
            reference.child(orderid).push().setValue(obj);
            reference2.child(orderid).push().setValue(obj);
            reference3.push().setValue(obj);
            del_data1();
            del_data2();
            del_data3();
            del_data4();
        }
    }

    private void send_a_chat() {
        Message = "Doc Mech Services " + "\n" + " Service Completed For Your Vehicle " + Bike_no +" Bill Amt ₹ " + Bill+" Delivery in Progress \n Happy Servicing";
        SimpleDateFormat format = new SimpleDateFormat("HH:mm aa");
        Date date = new Date();
        Chats Obj = new Chats(FirebaseAuth.getInstance().getUid(),uid,Message,format.format(date));
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.push().setValue(Obj);
    }

    private void send_sms() {
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED)
        {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(Phone,null,"Doc Toque Services!!! \n Your Vehicle " + Bike_no + " Service Completed and the Bill Amount is "+ Bill + " Delivery In Progress \n Happy Servicing!!!",null,null);
                Toast.makeText(this, "Send confirmation Message Successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),Home.class));
                finish();
        }
        else
        {
            check_permission();
        }
    }

    private void check_permission() {
        if (ContextCompat.checkSelfPermission(generate_bill.this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED)
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.SEND_SMS))
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Please allow the Permission")
                        .setMessage("DOC MECH needs to send SMS to the users so please allow permission to send SMS")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(generate_bill.this,new String[]
                                        {
                                                Manifest.permission.SEND_SMS
                                        },REQUEST_CODE);
                            }
                        }).setNegativeButton("Calcel",null);
                builder.create().show();
            }
            else
            {
                ActivityCompat.requestPermissions(generate_bill.this,new String[]
                        {
                                Manifest.permission.SEND_SMS
                        },REQUEST_CODE);

                send_sms();
            }
        }
    }

    private void setUI()
    {
        title0 = (TextView) findViewById(R.id.title0);
        title1 = (TextView) findViewById(R.id.title1);
        address = (TextView) findViewById(R.id.address);
        date = (TextView) findViewById(R.id.date);
        send = (Button) findViewById(R.id.send);
        bill_amt = (EditText) findViewById(R.id.bill_amt);
        bike_no = (TextView) findViewById(R.id.bike_no);
        owner = (TextView) findViewById(R.id.owner_name);
        phone = (TextView) findViewById(R.id.phone);
        email = (TextView) findViewById(R.id.email);
        kms = (TextView) findViewById(R.id.kms);
        reference = FirebaseDatabase.getInstance().getReference("Billed vehicles");
        reference3 = FirebaseDatabase.getInstance().getReference("MECHS").child(FirebaseAuth.getInstance().getUid()).child("billed vehicles");
        general = (TextView) findViewById(R.id.general_service);
        premium = (TextView) findViewById(R.id.premium_service);
        engine = (TextView) findViewById(R.id.engine);
        suspension = (TextView) findViewById(R.id.suspension);
        electrical = (TextView) findViewById(R.id.electrical);
        others = (TextView) findViewById(R.id.others);
        uid = getIntent().getStringExtra("uid");
        orderid = getIntent().getStringExtra("orderid");
        Bike_no = getIntent().getStringExtra("Bike_no");
        Owner = getIntent().getStringExtra("Owner");
        Email = getIntent().getStringExtra("Email");
        Phone = getIntent().getStringExtra("Phone");
        Kms = getIntent().getStringExtra("Kms");
        General_service = getIntent().getStringExtra("General");
        Premium_service = getIntent().getStringExtra("Premium");
        Engine = getIntent().getStringExtra("Engine");
        Suspension = getIntent().getStringExtra("Suspension");
        Electrical = getIntent().getStringExtra("Electrical");
        Others = getIntent().getStringExtra("Others");
        Date = getIntent().getStringExtra("Date");
        Address = getIntent().getStringExtra("Address");
        User = getIntent().getStringExtra("User");
        shopname = getIntent().getStringExtra("shopname");
        Shopname = (TextView) findViewById(R.id.shop_name);
        Shopname.setText(shopname);
        reference2 = FirebaseDatabase.getInstance().getReference("User").child(User).child("My old services");
        bike_no.setText("Vehicle Number: "+Bike_no);
        owner.setText("Owner Name: "+Owner);
        phone.setText("Phone: "+Phone);
        email.setText("Email: "+Email);
        kms.setText("Kms Driven: "+Kms);
        general.setText(General_service);
        premium.setText(Premium_service);
        engine.setText(Engine);
        suspension.setText(Suspension);
        electrical.setText(Electrical);
        others.setText(Others);
        date.setText("Date: "+Date);
        address.setText("Address: "+Address);
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm ");
        Date d_obj = new Date();
        Date_Billed = format.format(d_obj);
        delref1 = FirebaseDatabase.getInstance().getReference("Bike").child(orderid);
        delref2 = FirebaseDatabase.getInstance().getReference("job carded vehicles").child(orderid);
        delref3 = FirebaseDatabase.getInstance().getReference("MECHS").child(FirebaseAuth.getInstance().getUid()).child("job carded vehicles").child(orderid);
        delre4 = FirebaseDatabase.getInstance().getReference("MECHS").child(FirebaseAuth.getInstance().getUid()).child("bending services").child(orderid);
    }

    private void setEnabled()
    {
        bike_no.setEnabled(false);
        owner.setEnabled(false);
        phone.setEnabled(false);
        kms.setEnabled(false);
        email.setEnabled(false);
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

    private void del_data1()
    {
        delref1.removeValue();
    }

    private void del_data2()
    {
        delref2.removeValue();
    }

    private void del_data3()
    {
        delref3.removeValue();
    }

    private void del_data4()
    {
        delre4.removeValue();
    }
}
