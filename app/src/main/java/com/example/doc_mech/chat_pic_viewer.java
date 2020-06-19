package com.example.doc_mech;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.squareup.picasso.Picasso;

public class chat_pic_viewer extends AppCompatDialogFragment {
    private ImageView profile_image;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.chat_pic_viewer,null);
        profile_image = (ImageView) view.findViewById(R.id.profile_image);
        String url = ((Message) getActivity()).geturl();
        Picasso.get().load(url).fit().into(profile_image);
        builder.setView(view);
        return builder.create();
    }
}
