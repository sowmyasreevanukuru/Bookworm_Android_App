package com.example.bookworm;

import androidx.activity.ComponentActivity;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.transition.MaterialElevationScale;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class upload_book extends AppCompatActivity {
    private static final int SELECT_PICTURE = 100;

    NavigationView useruploadnav;
    ActionBarDrawerToggle useruploadtoggle;
    DrawerLayout useruploaddrawerLayout;
    Toolbar useruploadtoolbar;
    ImageView iv_bookpic;
    EditText txtuploadbookname,txtuploadbookauthor,txtuploadpublisher,txtbookdescription;

    AppCompatTextView txtuploadimg,txtuploadpdf;
    TextView txtupload_pdf,edtext;
    Button btnuploadbookpdf,btnuploadbook;
    Uri pdfUri;
    Uri imageuri;

    String getimgurl;

    ProgressDialog progressDialog;
    SharedPreferences sharedPreferences_upload;
    private final int PICK_IMAGE_REQUEST = 22;


    String bookName,bookAuthor,bookPublisher,bookDescription,bookCategory;

    String currentUser;

    AppCompatSpinner sp_bookcateogries;
    //String[] categories=new String[]{"--Select category--","Adventure","Biography","Children","Fiction","Mystery","History","Motivational","Novels","Others","Plays","Science Fiction"};

    FirebaseStorage storage; //add dependency
    FirebaseDatabase database;
    FirebaseAuth fAuth;
    StorageReference storageReference;
    DatabaseReference databaseReference,countbooksref;
    int countbook=0;
    Book book;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    //
    ValueEventListener listener;
    ArrayAdapter adapter;
    ArrayAdapter<String> adapter1;
    ArrayList<String> spinnerDataList=new ArrayList<>();
    DatabaseReference databaseReference_spinner=FirebaseDatabase.getInstance().getReference();
    Spinner sp_category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_book);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //id
        sp_category=findViewById(R.id.sp_category);

/*
        databaseReference_spinner.child("Category").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                spinnerDataList.clear();
                Toast.makeText(getApplicationContext(),snapshot.getChildren().toString(),Toast.LENGTH_SHORT).show();

                for (DataSnapshot item : snapshot.getChildren()){
                    //Toast.makeText(getApplicationContext(),snapshot.child("category_name").getValue(String.class),Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(),item.getKey().toString(),Toast.LENGTH_SHORT).show();
                    spinnerDataList.add(item.child("category_name").getValue(String.class));

                }

                adapter1=new ArrayAdapter<>(upload_book.this, android.R.layout.simple_spinner_dropdown_item,spinnerDataList);
                sp_category.setAdapter(adapter1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


 */

        /*databaseReference_spinner.child("Category").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //spinnerDataList.clear();
                for(DataSnapshot item:dataSnapshot.getChildren())
                {
                    Toast.makeText(getApplicationContext(),dataSnapshot.child("category_name").getValue(String.class),Toast.LENGTH_SHORT).show();
                    spinnerDataList.add(item.child("category_name").getValue(String.class));
                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getApplicationContext(),R.layout.style_spinner,spinnerDataList);
                sp_category.setAdapter(arrayAdapter);
                //adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

         */



        txtuploadbookname=findViewById(R.id.txtuploadbookname);
        txtuploadbookauthor=findViewById(R.id.txtuploadbookauthor);
        txtuploadpublisher=findViewById(R.id.txtuploadpublisher);
        txtbookdescription=findViewById(R.id.txtbookdescription);
        // edtext=findViewById(R.id.edtext);
        sharedPreferences_upload=getSharedPreferences("Login",MODE_PRIVATE);
        sp_bookcateogries=findViewById(R.id.sp_category);
        btnuploadbookpdf=findViewById(R.id.txtuploadbookpdf);
        txtupload_pdf=findViewById(R.id.txtupload_pdf);
        btnuploadbook=findViewById(R.id.btnuploadbook);
        storage=FirebaseStorage.getInstance();
        database=FirebaseDatabase.getInstance();
        iv_bookpic=findViewById(R.id.img_uploadbook);

        fAuth=FirebaseAuth.getInstance();

        //navigation menu
        useruploadtoolbar=(Toolbar)findViewById(R.id.useruploadtoolbar);
        setSupportActionBar(useruploadtoolbar);

        useruploadnav=(NavigationView) findViewById(R.id.navuseruploadmenu);
        useruploaddrawerLayout=(DrawerLayout) findViewById(R.id.upload_drawer);

        useruploadtoggle=new ActionBarDrawerToggle(this,useruploaddrawerLayout,useruploadtoolbar,R.string.open,R.string.close);
        useruploaddrawerLayout.addDrawerListener(useruploadtoggle);
        useruploadtoggle.syncState();

        useruploadnav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.menu_userhome:
                        // Toast.makeText(getApplicationContext(), "Home panel", Toast.LENGTH_SHORT).show();
                        Intent i=new Intent(getApplicationContext(),user_home.class);
                        startActivity(i);
                        useruploaddrawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.menu_usercategories:
                        //Toast.makeText(getApplicationContext(), "Categories", Toast.LENGTH_SHORT).show();
                        Intent ii=new Intent(getApplicationContext(),user_categories.class);
                        startActivity(ii);
                        useruploaddrawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.menu_useruploadbook:
                        //Toast.makeText(getApplicationContext(), "upload book", Toast.LENGTH_SHORT).show();
                        Intent objupload=new Intent(getApplicationContext(),upload_book.class);
                        startActivity(objupload);
                        useruploaddrawerLayout.closeDrawer(GravityCompat.START);
                        break;




                    case R.id.menu_useraccount:
                        //Toast.makeText(getApplicationContext(), "account", Toast.LENGTH_SHORT).show();
                        Intent obj_account=new Intent(getApplicationContext(),user_account.class);
                        startActivity(obj_account);
                        useruploaddrawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.menu_userlogout:
                        if(sharedPreferences_upload.contains("email") && sharedPreferences_upload.contains("password"))
                        {
                            SharedPreferences.Editor editor=sharedPreferences_upload.edit();
                            editor.putString("email","");
                            editor.putString("password","");
                            editor.commit();
                        }
                        Intent objlogout=new Intent(getApplicationContext(),home.class);
                        startActivity(objlogout);
                        useruploaddrawerLayout.closeDrawer(GravityCompat.START);
                        break;
                }

                return true;
            }
        });

        loadSpinner();
       /* ArrayAdapter<String> adapter_c=new ArrayAdapter<String>(getApplicationContext(),R.layout.support_simple_spinner_dropdown_item,categories)
        {
            @Override
            public boolean isEnabled(int position) {
                if(position==0)
                { return false;}
                else
                {return true;}
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        sp_bookcateogries.setAdapter(adapter_c);
        */
        iv_bookpic=findViewById(R.id.img_uploadbook);

        upload_book();

        txtuploadimg=findViewById(R.id.edtext_coverimg);
        txtuploadpdf=findViewById(R.id.txtupload_pdf);

    }

    private void loadSpinner() {
        databaseReference_spinner.child("Category").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //spinnerDataList.clear();
                //Toast.makeText(getApplicationContext(),dataSnapshot.child("category_name").getValue(String.class),Toast.LENGTH_SHORT).show();

                for(DataSnapshot item:dataSnapshot.getChildren())
                {
                    //Toast.makeText(getApplicationContext(),dataSnapshot.child("category_name").getValue(String.class),Toast.LENGTH_SHORT).show();
                    spinnerDataList.add(item.child("category_name").getValue(String.class));
                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getApplicationContext(),R.layout.style_spinner,spinnerDataList);
                sp_category.setAdapter(arrayAdapter);
                //adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        /*
        databaseReference_spinner=FirebaseDatabase.getInstance().getReference("Category");
        databaseReference_spinner.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                //Toast.makeText(getApplicationContext(), snapshot.child("category_name").getValue().toString(), Toast.LENGTH_LONG).show();
                String cat = snapshot.child("category_name").getValue().toString();
                spinnerDataList=new ArrayList<>();
                //Toast.makeText(getApplicationContext(), spinnerDataList.toString(), Toast.LENGTH_LONG).show();
                //adapter=new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, Collections.singletonList(cat));
                adapter=new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, spinnerDataList);

                //else call this method in onCreate method
                sp_category.setAdapter(adapter);
                retrieveData();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

*/

        //
      /*  DatabaseReference databaseReference_spinner;
        ArrayList<Category> datamodelspinner=new ArrayList<>();

        databaseReference_spinner=FirebaseDatabase.getInstance().getReference("Category");
        databaseReference_spinner.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                //  Toast.makeText(getApplicationContext(), snapshot.child("bookAuthor").getValue().toString(), Toast.LENGTH_LONG).show();
                String getc_name=snapshot.child("category_name").getValue().toString();
                String getc_imgurl=snapshot.child("imgid").getValue().toString();

                Category getc=new Category(getc_name,getc_imgurl);
                datamodelspinner.add(getc);

                ArrayAdapter<String> adapter=new ArrayAdapter<>(getApplicationContext(),R.layout.support_simple_spinner_dropdown_item,datamodelspinner);


                ArrayAdapter<String> adapter = new ArrayAdapter<String>(upload_book.this,R.layout.support_simple_spinner_dropdown_item,datamodelspinner);
                adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                sp_bookcateogries.setAdapter(adapter);





                // p_userhome.setVisibility(View.GONE);
                // bookslist.add(snapshot.getKey());
                //   adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {


            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

*/

    }

    private void upload_book()
    {


        storageReference= FirebaseStorage.getInstance().getReference();
        databaseReference= FirebaseDatabase.getInstance().getReference("Book");
        countbooksref=FirebaseDatabase.getInstance().getReference().child("Book");

        countbooksref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                countbook=(int) snapshot.getChildrenCount();
                String c= String.valueOf(countbook);
                //Toast.makeText(getApplicationContext(), c, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        storage=FirebaseStorage.getInstance();

        iv_bookpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });


        btnuploadbookpdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dexter.withContext(getApplicationContext())
                        .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                                Intent intent=new Intent();
                                intent.setType("application/pdf");
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(Intent.createChooser(intent,"Select Pdf File"),101);
                                btnuploadbookpdf.setText("PDF Selected!");
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                                permissionToken.continuePermissionRequest();
                            }
                        }).check();
            }
        });

        btnuploadbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookName=txtuploadbookname.getText().toString();
                bookAuthor=txtuploadbookauthor.getText().toString();
                bookPublisher=txtuploadpublisher.getText().toString();
                bookDescription=txtbookdescription.getText().toString();
                bookCategory = sp_bookcateogries.getSelectedItem().toString();


                if(bookName.equals("")) {
                    txtuploadbookname.setError("Please fill");
                    return;
                }
                if(bookAuthor.equals("")) {
                    txtuploadbookauthor.setError("Please fill");
                    return;
                }
                if(bookPublisher.equals("")) {
                    txtuploadpublisher.setError("Please fill");
                    return;
                }
                if(bookDescription.equals("") ) {
                    txtbookdescription.setError("Please fill");
                    return;

                }
                //name must be more than 2 character
                if(bookAuthor.length()<3) {
                    txtuploadbookname.setError("Author name must be more than 2 letters");
                    return;
                }
                if(bookPublisher.length()<3) {
                    txtuploadpublisher.setError("Publisher name must be more than 2 letters");
                    return;
                }


                processupload(pdfUri,imageuri);
            }
        });
    }


    private void selectImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image from here..."),PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==101 && resultCode==RESULT_OK)
        {
            pdfUri=data.getData();
        }
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // Get the Uri of data
            imageuri = data.getData();
            try {

                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageuri);
                iv_bookpic.setImageBitmap(bitmap);
            }

            catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }

    public void processupload(Uri filepath,Uri ImgPath)
    {
        final ProgressDialog pd=new ProgressDialog(this);
        pd.setTitle("File Uploading...!!!");
        pd.show();


        //cover image upload
        StorageReference ref = storageReference.child("Cover_imgs/" + UUID.randomUUID().toString());
        ref.putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        getimgurl=uri.toString();
                    }
                });
                Toast.makeText(getApplicationContext(), "Image Uploaded!", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Failed to upload image!", Toast.LENGTH_SHORT).show();
            }
        });

        final StorageReference reference=storageReference.child("uploads/"+System.currentTimeMillis()+".pdf");
        reference.putFile(filepath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Toast.makeText(getApplicationContext(), "Book details uploading!", Toast.LENGTH_LONG).show();
                                String uid=user.getUid();
                                bookName=txtuploadbookname.getText().toString();
                                bookAuthor=txtuploadbookauthor.getText().toString();
                                bookPublisher=txtuploadpublisher.getText().toString();
                                bookDescription=txtbookdescription.getText().toString();
                                bookCategory = sp_bookcateogries.getSelectedItem().toString();
                                String url=uri.toString();

                                Toast.makeText(getApplicationContext(), "Book pdf uploaded!", Toast.LENGTH_LONG).show();
                                Book obj=new Book(bookAuthor,bookCategory,bookDescription,bookName,bookPublisher,url,getimgurl,0,0,0,uid);
                                databaseReference.child(databaseReference.push().getKey()).setValue(obj);

                                pd.dismiss();
                                Toast.makeText(getApplicationContext(),"Book Uploaded! All set ;)",Toast.LENGTH_LONG).show();

                                txtuploadbookname.setText("");
                                txtuploadbookauthor.setText("");
                                txtuploadpublisher.setText("");
                                txtbookdescription.setText("");
                                txtupload_pdf.setText("");
                                btnuploadbookpdf.setText("UPLOAD");
                                iv_bookpic.setImageResource(0);
                            }
                        });

                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        float percent=(100*taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                        pd.setMessage("Uploaded :"+(int)percent+"%");
                    }
                });
    }


    /*
    public void retrieveData()
    {
        listener=databaseReference_spinner.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot item:dataSnapshot.getChildren())
                {
                    spinnerDataList.add(item.getValue().toString());
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }*/
}