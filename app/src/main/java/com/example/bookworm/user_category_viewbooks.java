package com.example.bookworm;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;

public class user_category_viewbooks extends AppCompatActivity {
    ImageView iv;
    String cname;
    int temp=0;

    NavigationView nav_usercategorybooks;
    ActionBarDrawerToggle toggle_usercategorybooks;
    DrawerLayout drawerLayout_usercategorybooks;
    Toolbar toolbar_usercategorybooks;
    //SliderView sliderView_userhome_usercategorybooks;
    SharedPreferences sharedPreferences_logout;

    ProgressBar progressBar_usercategorybooks;
    ListView lstbooks_usercategorybooks;
    ArrayList<Book> bookslist_usercategorybooks;
    DatabaseReference reference_usercategorybooks;
    TextView txtmessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_category_viewbooks);

        cname= String.valueOf(getIntent().getExtras().getString("category"));

        //Toast.makeText(this, cname, Toast.LENGTH_SHORT).show();
        txtmessage=findViewById(R.id.txtmessage);
        txtmessage.setText("");
        lstbooks_usercategorybooks=findViewById(R.id.lstbooks_usercategorybooks);
        bookslist_usercategorybooks=new ArrayList<Book>();
        progressBar_usercategorybooks=findViewById(R.id.progressBar_usercategorybooks);

        progressBar_usercategorybooks.setVisibility(View.VISIBLE);


        sharedPreferences_logout = getSharedPreferences("Login", MODE_PRIVATE);

        //navigation menu
        toolbar_usercategorybooks = (Toolbar) findViewById(R.id.toolbar_usercategorybooks);
        setSupportActionBar(toolbar_usercategorybooks);

        nav_usercategorybooks = (NavigationView) findViewById(R.id.navmenu_usercategorybooks);
        drawerLayout_usercategorybooks = (DrawerLayout) findViewById(R.id.drawer_usercategorybooks);

        toggle_usercategorybooks = new ActionBarDrawerToggle(this, drawerLayout_usercategorybooks, toolbar_usercategorybooks, R.string.open, R.string.close);
        drawerLayout_usercategorybooks.addDrawerListener(toggle_usercategorybooks);
        toggle_usercategorybooks.syncState();

        nav_usercategorybooks.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_userhome:
                        Intent ob_home=new Intent(getApplicationContext(),user_home.class);
                        startActivity(ob_home);
                        drawerLayout_usercategorybooks.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.menu_usercategories:
                        Intent ob_c=new Intent(getApplicationContext(),user_categories.class);
                        startActivity(ob_c);
                        drawerLayout_usercategorybooks.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.menu_useruploadbook:
                        Intent objupload = new Intent(getApplicationContext(), upload_book.class);
                        startActivity(objupload);
                        drawerLayout_usercategorybooks.closeDrawer(GravityCompat.START);
                        break;




                    case R.id.menu_useraccount:
                        Intent obj_account1 = new Intent(getApplicationContext(), user_account.class);
                        startActivity(obj_account1);
                        drawerLayout_usercategorybooks.closeDrawer(GravityCompat.START);
                        break;


                    case R.id.menu_userlogout:
                        if (sharedPreferences_logout.contains("email") && sharedPreferences_logout.contains("password")) {
                            SharedPreferences.Editor editor = sharedPreferences_logout.edit();
                            editor.putString("email", "");
                            editor.putString("password", "");
                            editor.commit();
                        }
                        Intent objlogout = new Intent(getApplicationContext(), login.class);
                        startActivity(objlogout);
                        drawerLayout_usercategorybooks.closeDrawer(GravityCompat.START);
                        break;
                }
                return true;
            }
        });

        getcateogrybooks();

    }

    private void getcateogrybooks() {

        reference_usercategorybooks= FirebaseDatabase.getInstance().getReference("Book");
        reference_usercategorybooks.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                //  Toast.makeText(getApplicationContext(), snapshot.child("bookAuthor").getValue().toString(), Toast.LENGTH_LONG).show();
                String bookname = snapshot.child("bookName").getValue().toString();
                String bookauthor = snapshot.child("bookAuthor").getValue().toString();
                String bookcategory = snapshot.child("bookCategory").getValue().toString();
                String bookimage=snapshot.child("coverimg").getValue().toString();
                String bookpdf=snapshot.child("bookpdf").getValue().toString();
                String bookpublisher=snapshot.child("bookPublisher").getValue().toString();
                String bookdescription=snapshot.child("bookDescription").getValue().toString();


                if(bookcategory.equals(cname)) {
                    Book searchModel = new Book(bookname, bookauthor, bookcategory, bookimage, bookpdf, bookpublisher, bookdescription);
                    bookslist_usercategorybooks.add(searchModel);

                    customAdapter_user customAdapter_user = new customAdapter_user(getApplicationContext(), bookslist_usercategorybooks);
                    lstbooks_usercategorybooks.setAdapter(customAdapter_user);

                    progressBar_usercategorybooks.setVisibility(View.GONE);
                    temp++;

                }
                else
                {

                    progressBar_usercategorybooks.setVisibility(View.GONE);
                    temp=-1;
                }
                if(temp==-1)
                {
                    progressBar_usercategorybooks.setVisibility(View.GONE);
                    //txtmessage.setText("No books available of this cateogory");

                }
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

}