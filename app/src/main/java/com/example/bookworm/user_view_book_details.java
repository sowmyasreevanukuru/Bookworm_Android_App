package com.example.bookworm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class user_view_book_details extends AppCompatActivity {



    NavigationView view_bookk_nav_user;
    ActionBarDrawerToggle view_book_toggle_user;
    DrawerLayout view_book_drawerLayout_user;
    Toolbar view_book_toolbar_user;
    Button btnread_user,btnreport,fav_book;
    AppCompatTextView txtbookname_user,txtbookauthor_user,txtbookdescription_user,txtpublisher_user;
    ImageView img_user;

    boolean isInMyFavorite=false;
    private FirebaseAuth firebaseAuth;
    String bookId,bookname;

    SharedPreferences sharedPreferences_view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_view_book_details);

        Intent i=getIntent();
        bookId=i.getStringExtra("bookId");
        bookId=i.getStringExtra("bookname");
        firebaseAuth=FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() != null)
        {
            checkIsFavorite();
        }



        sharedPreferences_view=getSharedPreferences("Login",MODE_PRIVATE);
        btnread_user=findViewById(R.id.btnread_user);
        // btnreport=findViewById(R.id.btnreport);
        fav_book=findViewById(R.id.fav_book);
        view_book_toolbar_user=(Toolbar)findViewById(R.id.book_details_toolbar_user);
        setSupportActionBar(view_book_toolbar_user);

        view_bookk_nav_user=(NavigationView) findViewById(R.id.navmenu_userhome_user);
        view_book_drawerLayout_user=(DrawerLayout) findViewById(R.id.bookdetails_drawer_user);

        view_book_toggle_user=new ActionBarDrawerToggle(this,view_book_drawerLayout_user,view_book_toolbar_user,R.string.open,R.string.close);
        view_book_drawerLayout_user.addDrawerListener(view_book_toggle_user);
        view_book_toggle_user.syncState();


        view_bookk_nav_user.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.menu_userhome:
                        Intent obj_home=new Intent(getApplicationContext(),user_home.class);
                        startActivity(obj_home);
                        view_book_drawerLayout_user.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.menu_usercategories:
                        Intent ob_cat=new Intent(getApplicationContext(),user_categories.class);
                        startActivity(ob_cat);
                        view_book_drawerLayout_user.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.menu_useruploadbook:
                        Intent objupload=new Intent(getApplicationContext(),upload_book.class);
                        startActivity(objupload);
                        view_book_drawerLayout_user.closeDrawer(GravityCompat.START);
                        break;




                    case R.id.menu_useraccount:
                        Intent obj_account1=new Intent(getApplicationContext(),user_account.class);
                        startActivity(obj_account1);
                        view_book_drawerLayout_user.closeDrawer(GravityCompat.START);
                        break;


                    case R.id.menu_userlogout:
                        if(sharedPreferences_view.contains("email") && sharedPreferences_view.contains("password"))
                        {
                            SharedPreferences.Editor editor=sharedPreferences_view.edit();
                            editor.putString("email","");
                            editor.putString("password","");
                            editor.commit();
                        }
                        Intent objlogout=new Intent(getApplicationContext(),home.class);
                        startActivity(objlogout);
                        view_book_drawerLayout_user.closeDrawer(GravityCompat.START);
                        break;
                }
                return true;
            }
        });

        txtbookauthor_user=findViewById(R.id.view_bookauthor_user);
        txtbookdescription_user=findViewById(R.id.view_bookdescription_user);
        txtpublisher_user=findViewById(R.id.view_boookpublisher_user);
        txtbookname_user=findViewById(R.id.view_bookname_user);
        img_user=findViewById(R.id.view_book_img_user);

        getdetails_user();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_report, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.item_report:
                //Report book code
                Toast.makeText(getApplicationContext(),"Report ",Toast.LENGTH_LONG).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getdetails_user() {

        bookname=getIntent().getStringExtra("bookname");
        String authorname=getIntent().getStringExtra("authorname");
        String publisher=getIntent().getStringExtra("publisher");
        String description=getIntent().getStringExtra("description");
        String bookimg=getIntent().getStringExtra("bookimg");
        String bookpdf=getIntent().getStringExtra("bookpdf");

        Picasso.get().load(bookimg).into(img_user);

        txtbookname_user.setText(bookname);
        txtbookauthor_user.setText(authorname);
        txtpublisher_user.setText(publisher);
        txtbookdescription_user.setText(description);

        btnread_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ih=new Intent(getApplicationContext(),viewpdf.class);
                ih.putExtra("filename",bookname);
                ih.putExtra("fileurl",bookpdf);
                ih.putExtra("bookId",bookId);
                ih.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(ih);
            }
        });


        //
        fav_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(firebaseAuth.getCurrentUser() == null)
                {
                    Toast.makeText(getApplicationContext(), "You're not logged in", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if (isInMyFavorite)
                    {
                        //in fav,remove from fav
                        user_home.removeFromFavorite(getApplicationContext(),bookId);
                        fav_book.setCompoundDrawablesRelativeWithIntrinsicBounds(0,R.drawable.ic_baseline_favorite_border_24,0,0);
                        //fav_book.setText("Like");
                        isInMyFavorite=false;

                    }
                    else
                    {
                        //not in fav,add to fav
                        user_home.addToFavorite(getApplicationContext(),bookId);
                        fav_book.setCompoundDrawablesRelativeWithIntrinsicBounds(0,R.drawable.ic_baseline_favorite_24,0,0);
                        //fav_book.setText("Like");
                        isInMyFavorite=true;
                    }
                }
            }
        });

    }

    private void checkIsFavorite()
    {

        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("user");
        reference.child(firebaseAuth.getUid()).child("Favorities").child(bookId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        isInMyFavorite= snapshot.exists();
                        String key=snapshot.getKey();

                      //  Toast.makeText(getApplicationContext(), snapshot.toString(), Toast.LENGTH_SHORT).show();
                        if(bookname.equals(key))
                        {
                            //exists in fav
                            fav_book.setCompoundDrawablesRelativeWithIntrinsicBounds(0,R.drawable.ic_baseline_favorite_24,0,0);

                            //fav_book.setText("Dislike");
                        }
                        else
                        {
                            fav_book.setCompoundDrawablesRelativeWithIntrinsicBounds(0,R.drawable.ic_baseline_favorite_border_24,0,0);
                            //fav_book.setText("Like");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }


}