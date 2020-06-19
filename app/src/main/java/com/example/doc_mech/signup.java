package com.example.doc_mech;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowInsets;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class signup extends AppCompatActivity {

    private TextInputLayout shop_name, owner_name, location, email,pass,cpass;
    private Button signup;
    private CheckBox conditions;
    private String  Shop_name, Owner_name, Location, Email, Phone,Pass,Cpass,uid;
    private int MECH_COINS = 100;
    private ImageView profile_image;
    private ProgressDialog dialog;
    private static int PICK_IMAGE;
    private Uri imagepath;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data.getData()!=null)
        {
            imagepath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imagepath);
                profile_image.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        setui();

       conditions.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               open_dialog();
           }
       });
        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"select image"),PICK_IMAGE);
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getvalues();
                dialog.show();
                dialog.setCanceledOnTouchOutside(false);
                dialog.setCancelable(false);
                if (validate())
                {
                    if (FirebaseAuth.getInstance().signInWithEmailAndPassword(Email,Pass).isSuccessful())
                    {
                        dialog.dismiss();
                        Toast.makeText(signup.this, "Account aldready exists", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        FirebaseAuth.getInstance().createUserWithEmailAndPassword(Email,Pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                     if (task.isSuccessful())
                                     {
                                         senddata();
                                         update_shop_status();
                                         dialog.dismiss();
                                         Toast.makeText(getApplicationContext(),"siginin success..",Toast.LENGTH_LONG).show();
                                         Intent intent = new Intent(getApplicationContext(),Home.class);
                                         startActivity(intent);
                                         finish();
                                     }
                                     else
                                     {
                                         dialog.dismiss();
                                         Toast.makeText(signup.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                     }
                            }
                        });
                    }
                }
                else {
                    dialog.dismiss();
                }
            }
        });
    }

    private void update_shop_status() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Shop status").child(FirebaseAuth.getInstance().getUid()).child("Shop status");
        reference.setValue("opened");
    }

    private void open_dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.rules,null);
        builder.setView(view);
        builder.setPositiveButton("ACCEPT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                conditions.setChecked(true);
            }
        }).setNegativeButton("DECLINE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                conditions.setChecked(false);
            }
        });
        builder.create().show();
    }

    private void senddata() {

        uid = FirebaseAuth.getInstance().getUid();
        Add_Mech obj = new Add_Mech(Shop_name,Owner_name,Location,Email,Phone,Pass,MECH_COINS,uid);
        Add_shop obj2 = new Add_shop(Shop_name,uid,"online","now","opened");
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("MECHS").child(FirebaseAuth.getInstance().getUid());
        reference.push().setValue(obj);
        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("SHOPS").child(FirebaseAuth.getInstance().getUid());
        reference1.push().setValue(obj2);

        final StorageReference imagereference = FirebaseStorage.getInstance().getReference("Images").child(uid).child("profile_image");
        UploadTask uploadTask = imagereference.putFile(imagepath);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(signup.this, "Upload success", Toast.LENGTH_SHORT).show();
                imagereference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("Profile Image").child(FirebaseAuth.getInstance().getUid());
                        Add_shop obj = new Add_shop(uri.toString());
                        reference2.setValue(obj);
                    }
                });
            }
        });
    }

    private void getvalues() {
        Shop_name = shop_name.getEditText().getText().toString();
        Location = location.getEditText().getText().toString();
        Owner_name = owner_name.getEditText().getText().toString();
        Email = email.getEditText().getText().toString();
        Phone = getIntent().getStringExtra("mobile");
        Pass = pass.getEditText().getText().toString();
        Cpass = cpass.getEditText().getText().toString();

    }

    private void setui() {
        shop_name = (TextInputLayout) findViewById(R.id.shop_name);
        owner_name = (TextInputLayout) findViewById(R.id.ownername);
        location = (TextInputLayout) findViewById(R.id.location);
        email = (TextInputLayout) findViewById(R.id.email);
        pass = (TextInputLayout) findViewById(R.id.pass);
        cpass = (TextInputLayout) findViewById(R.id.cpass);
        signup = (Button) findViewById(R.id.signup);

        conditions = (CheckBox) findViewById(R.id.conditions);
        profile_image = (ImageView) findViewById(R.id.profile_pic);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait while we create you a Good sign...");
    }

    private boolean validate()
    {
        if (Shop_name.isEmpty() || Owner_name.isEmpty() || Location.isEmpty() || Phone.isEmpty() || Email.isEmpty() || Pass.isEmpty() || Cpass.isEmpty())
        {
            Toast.makeText(this, "All fields have to be filled", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (!Pass.equals(Cpass))
        {
            Toast.makeText(this, "Passwords does'nt match", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (Pass.length()<6)
        {
            Toast.makeText(getApplicationContext(),"Password too short",Toast.LENGTH_LONG).show();
            return false;
        }
        else if(!conditions.isChecked())
        {
            Toast.makeText(this, "Accept terms and conditions", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(imagepath == null)
        {
            Toast.makeText(this, "Please add a profile image", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(Phone.trim().length()<10)
        {
            Toast.makeText(this, "Enter a valid mobile number", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (Shop_name.length()>30)
        {
            Toast.makeText(this, "shop name must be less than 30 letters", Toast.LENGTH_SHORT).show();
            return false;
        }
        else
        {
            return true;
        }
    }
}