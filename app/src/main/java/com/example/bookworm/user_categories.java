package com.example.bookworm;

import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class user_categories extends AppCompatActivity{
    private static final int PICK_IMAGE_REQUEST = 22;
    GridView categorygv;
    final Context context = this;
    NavigationView usernav;
    ActionBarDrawerToggle usertoggle;
    DrawerLayout userdrawerLayout;
    Toolbar usertoolbar;
    Uri uricategoryimg;
    SharedPreferences sharedPreferences_categories;
    FloatingActionButton btn_add_category;
    String c_name=" ";
    String getc_imgurl;
    Uri category_uri;
    StorageReference storageReference_Category;
    DatabaseReference databaseReference_category;
    ImageView imgcategory;
    EditText txtcategory_name;
    AppCompatTextView txtadd,txtcancel;
    ProgressBar pb_category;
    ArrayList<Category> dataModalArrayList;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_categories);
        categorygv = findViewById(R.id.idGVCategory);

        sharedPreferences_categories=getSharedPreferences("Login",MODE_PRIVATE);
        storageReference_Category= FirebaseStorage.getInstance().getReference();
        databaseReference_category= FirebaseDatabase.getInstance().getReference("Category");

        usertoolbar=(Toolbar)findViewById(R.id.usertoolbar);
        setSupportActionBar(usertoolbar);

        usernav=(NavigationView) findViewById(R.id.navusermenu);
        userdrawerLayout=(DrawerLayout) findViewById(R.id.userdrawer);

        usertoggle=new ActionBarDrawerToggle(this,userdrawerLayout,usertoolbar,R.string.open,R.string.close);
        userdrawerLayout.addDrawerListener(usertoggle);
        usertoggle.syncState();


        usernav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.menu_userhome:
                        Intent obj_home=new Intent(getApplicationContext(),user_home.class);
                        startActivity(obj_home);
                        userdrawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.menu_usercategories:
                        Intent ob_cat=new Intent(getApplicationContext(),user_categories.class);
                        startActivity(ob_cat);
                        userdrawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.menu_useruploadbook:
                        Intent objupload=new Intent(getApplicationContext(),upload_book.class);
                        startActivity(objupload);
                        userdrawerLayout.closeDrawer(GravityCompat.START);
                        break;





                    case R.id.menu_useraccount:
                        Intent obj_account1=new Intent(getApplicationContext(),user_account.class);
                        startActivity(obj_account1);
                        userdrawerLayout.closeDrawer(GravityCompat.START);
                        break;


                    case R.id.menu_userlogout:
                        if(sharedPreferences_categories.contains("email") && sharedPreferences_categories.contains("password"))
                        {
                            SharedPreferences.Editor editor=sharedPreferences_categories.edit();
                            editor.putString("email","");
                            editor.putString("password","");
                            editor.commit();
                        }
                        Intent objlogout=new Intent(getApplicationContext(),home.class);
                        startActivity(objlogout);
                        userdrawerLayout.closeDrawer(GravityCompat.START);
                        break;
                }
                return true;
            }
        });
        //showcategories();
        btn_add_category=findViewById(R.id.btn_add_category);
        addCategory();

        dataModalArrayList = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        loadDatainGridView();
    }

    private void loadDatainGridView() {

        databaseReference_category=FirebaseDatabase.getInstance().getReference("Category");
        databaseReference_category.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                //  Toast.makeText(getApplicationContext(), snapshot.child("bookAuthor").getValue().toString(), Toast.LENGTH_LONG).show();
                String getc_name=snapshot.child("category_name").getValue().toString();
                String getc_imgurl=snapshot.child("imgid").getValue().toString();

                Category getc=new Category(getc_name,getc_imgurl);
                dataModalArrayList.add(getc);

                categoryGVAdapter adapter = new categoryGVAdapter(user_categories.this, dataModalArrayList);
                categorygv.setAdapter(adapter);

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
    }

    private void addCategory()
    {

        btn_add_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog=new Dialog(user_categories.this);
                dialog.setContentView(R.layout.add_category_layout);
                dialog.show();

                imgcategory=dialog.findViewById(R.id.img_category);
                txtadd=dialog.findViewById(R.id.txtaddcategory);
                txtcancel=dialog.findViewById(R.id.txtcancel);
                txtcategory_name=dialog.findViewById(R.id.txtcategory_name);
                pb_category=dialog.findViewById(R.id.pb_category);

                txtcancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                imgcategory.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Select Image from here..."), PICK_IMAGE_REQUEST);
                    }
                });

                txtadd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pb_category.setVisibility(View.VISIBLE);
                        if(TextUtils.isEmpty(txtcategory_name.getText().toString()))
                        {
                            txtcategory_name.setError("Please Fill");
                            pb_category.setVisibility(View.GONE);
                            return;
                        }

                        StorageReference ref = storageReference_Category.child("Category_imgs/" + UUID.randomUUID().toString());
                        ref.putFile(uricategoryimg).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        pb_category.setVisibility(View.VISIBLE);
                                        c_name= String.valueOf(txtcategory_name.getText());
                                        getc_imgurl=uri.toString();
                                        Category obj_C=new Category(c_name,getc_imgurl);
                                        databaseReference_category.child(databaseReference_category.push().getKey()).setValue(obj_C);
                                        Toast.makeText(getApplicationContext(), "New category added", Toast.LENGTH_SHORT).show();
                                        pb_category.setVisibility(View.GONE);
                                    }
                                });
                                pb_category.setVisibility(View.GONE);
                                dialog.dismiss();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), "Failed to upload image", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // Get the Uri of data
            uricategoryimg = data.getData();
            try {
                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uricategoryimg);
                imgcategory.setImageBitmap(bitmap);
            }

            catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }

   /* private void showcategories()
    {
        ArrayList<category_modal> cateogryModelArrayList = new ArrayList<category_modal>();
        cateogryModelArrayList.add(new category_modal("Adventure", R.drawable.adventure));
        cateogryModelArrayList.add(new category_modal("Biography", R.drawable.biography));
        cateogryModelArrayList.add(new category_modal("Children", R.drawable.children));
        cateogryModelArrayList.add(new category_modal("Fiction", R.drawable.ficction));
        cateogryModelArrayList.add(new category_modal("History", R.drawable.history));
        cateogryModelArrayList.add(new category_modal("Mystery", R.drawable.mystery));
        cateogryModelArrayList.add(new category_modal("Motivational", R.drawable.motivational));
        cateogryModelArrayList.add(new category_modal("Novels", R.drawable.novel));
        cateogryModelArrayList.add(new category_modal("Others", R.drawable.others));
        cateogryModelArrayList.add(new category_modal("Plays", R.drawable.plays));
        cateogryModelArrayList.add(new category_modal("Science Fiction", R.drawable.science_fiction));


        categoryGVAdapter adapter = new categoryGVAdapter(this, cateogryModelArrayList);
        categorygv.setAdapter(adapter);


        categorygv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent objsendcategory=new Intent(getApplicationContext(),user_category_viewbooks.class);
                objsendcategory.putExtra("position",i);
                startActivity(objsendcategory);
            };
        });
    }*/

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(),login.class));
        finish(); }



}