package com.example.doc_mech;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

public class photos extends AppCompatActivity {

    private ImageView photos;
    private Button upload,select,view_uploads;
    private static int PICK_IMAGE = 1;
    private Uri imagepath,download_uri;
    private ProgressDialog dialog;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data.getData()!=null)
        {
            imagepath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imagepath);
                photos.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);

        setUI();

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choose_image();
            }
        });

        upload.setEnabled(true);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload.setEnabled(false);
                dialog.show();
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                upload_file();
            }
        });

        view_uploads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),view_photos.class));
            }
        });
    }

    private void setUI()
    {
        select = (Button) findViewById(R.id.select);
        photos = (ImageView) findViewById(R.id.photos);
        upload = (Button) findViewById(R.id.upload);
        view_uploads = (Button) findViewById(R.id.view_upload);
        dialog = new ProgressDialog(this);
        dialog.setMessage("Upload in progress..!");

    }

    private void choose_image()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"select image"),PICK_IMAGE);
    }

    private String getFileExtension()
    {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(imagepath));
    }
    private void upload_file()
    {
        if(imagepath!=null)
        {
            final String UID = UUID.randomUUID().toString();
            final StorageReference reference = FirebaseStorage.getInstance().getReference("IMAGES/"+FirebaseAuth.getInstance().getUid()+"/"+UID);
            UploadTask uploadTask = reference.putFile(imagepath);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("IMAGES").child(FirebaseAuth.getInstance().getUid()).child("uploads");
                            Image obj = new Image(uri.toString(),UID);
                            reference1.push().setValue(obj).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    dialog.dismiss();
                                    Toast.makeText(photos.this, "Upload  Success,,,", Toast.LENGTH_SHORT).show();
                                    photos.setImageResource(R.drawable.image_instruction);
                                }
                            });
                        }
                    });
                }
            });

        }
        else
        {
            dialog.dismiss();
            Toast.makeText(this, "No File Selected..", Toast.LENGTH_SHORT).show();
            upload.setEnabled(true);
        }
    }
}



