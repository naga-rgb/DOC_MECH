package com.example.doc_mech;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class Home extends AppCompatActivity {

    private ImageView service_vehicles,job_carded_vehicles,billed_vrhicles,chat,offers,reviews,profile_image,photos,view_profile,prices;
    private TextView username;
    private Button logout,auth;
    private Boolean back_pressed=false;
    private String key;
    private static int REQUEST_CODE = 123;
    private ProgressDialog loading_dialog;
    private ImageView shop_status;
    private boolean status;
    private String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        loading_dialog = new ProgressDialog(this);
        loading_dialog.setMessage("Loading Profile..!");
        loading_dialog.setCanceledOnTouchOutside(false);
        loading_dialog.setCancelable(false);
        loading_dialog.show();
        launch_price_dialog();
        check_con();
        setui();
        fetch_name();
        fetch_status();
        check_permissions();
        get_image_url();
        shop_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("SHOPS").child(FirebaseAuth.getInstance().getUid()).child(key).child("shop_status");
                status = !status;
                if(status)
                {
                    shop_status.setImageResource(R.drawable.open);
                    reference.setValue("opened");
                    Toast.makeText(Home.this, "Shop is visible as open to others", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    shop_status.setImageResource(R.drawable.closed);
                    reference.setValue("closed");
                    Toast.makeText(Home.this, "Shop is visible as closed to others", Toast.LENGTH_SHORT).show();
                }
            }
        });
        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open_profile_dialog();
            }
        });
        service_vehicles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),service_vehicles.class);
                startActivity(intent);
            }
        });

        job_carded_vehicles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),job_card_vehicles.class));
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout_dialog();
            }
        });

        billed_vrhicles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),billed_vehicles.class));
            }
        });

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(getApplicationContext(),chat_users.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        offers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),offers.class));
            }
        });

        reviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),reviews.class));
            }
        });

        view_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),my_profile.class));
            }
        });

        photos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),photos.class));
            }
        });

        prices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open_price_dialog();
            }
        });
    }

    private void get_image_url() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Profile Image").child(FirebaseAuth.getInstance().getUid()).child("url");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                     url = dataSnapshot.getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void fetch_status() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("SHOPS").child(FirebaseAuth.getInstance().getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    if(snapshot.child("shop_status").getValue().toString().equals("opened"))
                    {
                        shop_status.setVisibility(View.VISIBLE);
                        shop_status.setImageResource(R.drawable.open);
                        status = true;
                        break;
                    }
                    else
                    {
                        shop_status.setVisibility(View.VISIBLE);
                        shop_status.setImageResource(R.drawable.closed);
                        status = false;
                        break;
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void launch_price_dialog() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("PRICE LIST").child(FirebaseAuth.getInstance().getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() == 0)
                {
                    show_alert_dialog();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void show_alert_dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder
                .setTitle("DOC MECH PRICE LIST")
                .setMessage("Since you are new to the DOC MECH these tips will help you. You have to add a price for your Services that you are providing " +
                        "here we have two major categories General Service Cost and Premium Service Cost Please do add those prices and you can also view and update it" +
                        "\n Thank you \n DOC MECH SERVICES ")
                .setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Price_dialog dialog1 = new Price_dialog();
                        dialog1.show(getSupportFragmentManager(),"Price List");
                    }
                });
        builder.create().show();
    }

    private void open_price_dialog() {
        Price_dialog dialog = new Price_dialog();
        dialog.show(getSupportFragmentManager(),"Price");
    }

    private void check_permissions() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) +
                ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) +
                ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) +
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) +
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.CAMERA) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.RECEIVE_SMS) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.SEND_SMS))
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
                builder.setTitle("Please Grant these Permissions")
                        .setMessage("These Permissions are used for Efficient running of DOC MECH")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(Home.this,new String[]
                                        {
                                                Manifest.permission.CAMERA,
                                                Manifest.permission.RECEIVE_SMS,
                                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                                Manifest.permission.SEND_SMS

                                        },REQUEST_CODE);
                            }
                        }).setNegativeButton("Cancel",null);
                builder.create().show();
            }
            else
            {
                ActivityCompat.requestPermissions(this,new String[]
                        {
                                Manifest.permission.CAMERA,
                                Manifest.permission.RECEIVE_SMS,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.SEND_SMS

                        },REQUEST_CODE);
            }
        }
    }

    private void open_profile_dialog() {
        Profile_pic_dialog dialog = new Profile_pic_dialog();
        dialog.show(getSupportFragmentManager(),"profile image");
    }

    private void logout_dialog() {
        Logout_dialog dialog = new Logout_dialog();
        dialog.show(getSupportFragmentManager(),"logout");
    }


    private void setui()
    {
        shop_status = (ImageView) findViewById(R.id.shop_status);
        prices = (ImageView) findViewById(R.id.prices);
        billed_vrhicles = (ImageView) findViewById(R.id.billed_vehicles);
        logout = (Button) findViewById(R.id.logout);
        job_carded_vehicles = (ImageView) findViewById(R.id.job_carded_vehicles);
        service_vehicles = (ImageView) findViewById(R.id.service_vehicles);
        username = (TextView) findViewById(R.id.user_name);
        chat = (ImageView) findViewById(R.id.chat);
        offers = (ImageView) findViewById(R.id.offers);
        reviews = (ImageView) findViewById(R.id.reviews);
        profile_image = (ImageView) findViewById(R.id.profile_image);
        view_profile = (ImageView) findViewById(R.id.view_profile);
        photos = (ImageView) findViewById(R.id.photos);
    }

    private void fetch_name()
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("MECHS/"+FirebaseAuth.getInstance().getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot){
                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    key = snapshot.getKey();
                    username.setText("Hey, "+snapshot.child("owner_name").getValue().toString()+" !!");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            loading_dialog.dismiss();
                        }
                    },800);

                    break;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void onBackPressed() {
        if (back_pressed)
        {
            Log.d("bacpressed","called");
            super.onBackPressed();
        }
        this.back_pressed = true;
        Toast.makeText(this, "Press BACK again to exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                back_pressed = false;
            }
        },2000);
    }

    private void check_con() {
        final DatabaseReference conref = FirebaseDatabase.getInstance().getReference("SHOPS").child(FirebaseAuth.getInstance().getUid());
        final  DatabaseReference inforef = FirebaseDatabase.getInstance().getReference(".info/connected");

        inforef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean connected = dataSnapshot.getValue(Boolean.class);
                if (connected)
                {
                    update_status("online");
                    conref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren())
                            {
                                SimpleDateFormat format = new SimpleDateFormat("dd/MM hh:mm aa");
                                Date  date = new Date();
                                conref.child(snapshot.getKey()).child("status").onDisconnect().setValue("offline");
                                conref.child(snapshot.getKey()).child("lastseen").onDisconnect().setValue(format.format(date));
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private void update_status(final String status)
    {
        if (FirebaseAuth.getInstance().getCurrentUser() !=null) {
            final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("SHOPS").child(FirebaseAuth.getInstance().getUid());
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        key = snapshot.getKey();
                        reference.child(snapshot.getKey()).child("status").setValue(status);
                        reference.child(snapshot.getKey()).child("lastseen").setValue("now");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        StorageReference reference = FirebaseStorage.getInstance().getReference("Images/"+FirebaseAuth.getInstance().getUid()+"/profile_image");
        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).fit().centerCrop().into(profile_image);
            }
        });
    }

    public String geturl()
    {
        return url;
    }
}
