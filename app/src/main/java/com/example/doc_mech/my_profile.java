package com.example.doc_mech;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class my_profile extends AppCompatActivity {

    private ImageView profile_image;
    private EditText shop_name,owner_name,address,email,phone;
    private Button update,auth,change_pic,status;
    private String path,key;
    private static int PICK_IMAGE=1;
    private Uri imagepath;
    private ProgressDialog dialog;
    private boolean allowBack = true;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data.getData()!=null)
        {
            imagepath = data.getData();

            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imagepath);
                profile_image.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        setUI();
        get_path();
        getkey();
        get_values();
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate())
                {
                    update_data();
                }
            }
        });

        auth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),change_auth.class);
                intent.putExtra("path",path);
                startActivity(intent);
            }
        });

        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choose_image();
            }
        });

        change_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imagepath == null)
                {
                    Toast.makeText(my_profile.this, "No file selected", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    allowBack = false;
                    dialog.show();
                    dialog.setCanceledOnTouchOutside(false);
                    upload_image();
                }
            }
        });
    }

    private void getkey() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("SHOPS").child(FirebaseAuth.getInstance().getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    if (snapshot.child("uid").getValue().toString().equals(FirebaseAuth.getInstance().getUid()))
                    {
                        key = snapshot.getKey();
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void upload_image() {
        final StorageReference imagereference = FirebaseStorage.getInstance().getReference("Images").child(FirebaseAuth.getInstance().getUid()).child("profile_image");
        imagereference.putFile(imagepath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imagereference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                       DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Profile Image").child(FirebaseAuth.getInstance().getUid()).child("url");
                       reference.setValue(uri.toString());
                       dialog.dismiss();
                       Toast.makeText(my_profile.this, "Profile picture Updated successfully", Toast.LENGTH_SHORT).show();
                       allowBack = true;
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                allowBack = true;
                dialog.dismiss();
                Toast.makeText(my_profile.this, "Failed to update profile picture..", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void choose_image() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"select image"),PICK_IMAGE);
    }

    private void update_data()
    {
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("MECHS").child(FirebaseAuth.getInstance().getUid()).child(path);
        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("SHOPS").child(FirebaseAuth.getInstance().getUid()).child(key).child("shopname");
        reference.child("shop_name").setValue(shop_name.getText().toString());
        reference1.setValue(shop_name.getText().toString());
        reference.child("owner_name").setValue(owner_name.getText().toString());
        reference.child("location").setValue(address.getText().toString());
        reference.child("phone").setValue(phone.getText().toString());
        Toast.makeText(this, "Profile Updated..", Toast.LENGTH_SHORT).show();
    }

    private void setUI()
    {
        dialog = new ProgressDialog(this);
        dialog.setMessage("Updating profile picture...");
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        change_pic = (Button) findViewById(R.id.change_pic);
        profile_image = (ImageView) findViewById(R.id.profile_image);
        auth = (Button) findViewById(R.id.auth);
        shop_name = (EditText) findViewById(R.id.shop_name);
        owner_name = (EditText) findViewById(R.id.owner_name);
        address = (EditText) findViewById(R.id.address);
        phone = (EditText) findViewById(R.id.phone);

        update = (Button) findViewById(R.id.update);

        StorageReference reference = FirebaseStorage.getInstance().getReference("Images/"+FirebaseAuth.getInstance().getUid()+"/profile_image");
        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).fit().centerCrop().into(profile_image);
            }
        });
    }


    private void get_values()
    {
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("MECHS/"+ FirebaseAuth.getInstance().getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    shop_name.setText(snapshot.child("shop_name").getValue().toString());
                    owner_name.setText(snapshot.child("owner_name").getValue().toString());
                    address.setText(snapshot.child("location").getValue().toString());
                    phone.setText(snapshot.child("phone").getValue().toString());
                    break;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private boolean validate()
    {
        if(shop_name.getText().toString().isEmpty() || owner_name.getText().toString().isEmpty() || address.getText().toString().isEmpty()
        || phone.getText().toString().isEmpty())
        {
            Toast.makeText(this, "Fill all the fields", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (shop_name.getText().toString().trim().length()>30)
        {
            Toast.makeText(this, "Shop Name Too long provide a name within 30 letters", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(owner_name.getText().toString().trim().length()>15)
        {
            Toast.makeText(this, "Owner name too long can be within 15 characters", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(phone.getText().toString().trim().length() != 10)
        {
            Toast.makeText(this, "invalid mobile number", Toast.LENGTH_SHORT).show();
            return false;
        }
        else
        {
            return true;
        }
    }
    private void get_path() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("MECHS").child(FirebaseAuth.getInstance().getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    path = snapshot.getKey();
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
        if(allowBack)
        {
            super.onBackPressed();
        }
        else
        {
            Toast.makeText(this, "cannot go back process is ongoing..Please wait", Toast.LENGTH_SHORT).show();
        }
    }
}
