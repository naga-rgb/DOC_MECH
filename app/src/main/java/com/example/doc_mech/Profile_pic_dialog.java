package com.example.doc_mech;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class Profile_pic_dialog  extends AppCompatDialogFragment {

    private ImageView profile_image;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.image_view2,null);
        profile_image = (ImageView) view.findViewById(R.id.image);
        String url = ((Home) getActivity()).geturl();
        Picasso.get().load(url).fit().into(profile_image);
        builder.setView(view);

    return builder.create();
    }

}
