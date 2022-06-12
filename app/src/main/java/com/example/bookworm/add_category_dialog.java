package com.example.bookworm;

import static android.app.Activity.RESULT_OK;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

public class add_category_dialog extends AppCompatDialogFragment {
    EditText txtcategory_name;
    ImageView imgcategory;
    layout_interface listener;
    Uri uricategoryimg;
    String getimgurl;
    private final int PICK_IMAGE_REQUEST = 22;
    String get_category_uri;
    StorageReference storageReference_Category;
    DatabaseReference databaseReference_category;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        storageReference_Category= FirebaseStorage.getInstance().getReference();
        databaseReference_category= FirebaseDatabase.getInstance().getReference("Category");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.add_category_layout, null);
        imgcategory= view.findViewById(R.id.img_category);
        imgcategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image from here..."),PICK_IMAGE_REQUEST);
            }
        });
        builder.setView(view)
                .setTitle("Add category")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                        String c_name = txtcategory_name.getText().toString();


                        Toast.makeText(getActivity().getApplicationContext(), uricategoryimg.toString(), Toast.LENGTH_SHORT).show();
                        StorageReference ref = storageReference_Category.child("Cover_imgs/" + UUID.randomUUID().toString());
                        ref.putFile(uricategoryimg).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        getimgurl=uri.toString();
                                        category_modal obj=new category_modal(c_name,getimgurl);
                                        databaseReference_category.child(databaseReference_category.push().getKey()).setValue(obj);
                                        Toast.makeText(getActivity().getApplicationContext(), "Category added", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                Toast.makeText(getActivity().getApplicationContext(), "Image Uploaded!", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity().getApplicationContext(), "Failed to upload image!", Toast.LENGTH_SHORT).show();
                            }
                        });


                        listener.applyTexts(c_name,uricategoryimg);

                    }
                });

        txtcategory_name = view.findViewById(R.id.txtcategory_name);

        return builder.create();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // Get the Uri of data
            uricategoryimg = data.getData();
            try {

                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uricategoryimg);
                imgcategory.setImageBitmap(bitmap);
            }

            catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (layout_interface) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement ExampleDialogListener");
        }
    }

    public interface layout_interface {
        void applyTexts(String cname,Uri curi);
    }
}
