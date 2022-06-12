package com.example.bookworm;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class user_home extends AppCompatActivity {
    NavigationView nav_userhome;
    ActionBarDrawerToggle toggle_userhome;
    DrawerLayout drawerLayout_userhome;
    Toolbar toolbar_userhome;
    SliderView sliderView_userhome;
    SharedPreferences sharedPreferences_logout,sharedPreferences_signup;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    String url2="https://thumbs.dreamstime.com/b/online-library-e-book-reading-concept-vector-illustration-virtual-mobile-app-applications-downloading-books-228459438.jpg";
    String url1="https://image.freepik.com/free-vector/online-library-app-reading-banner_33099-1733.jpg";
    String url3="https://media.istockphoto.com/vectors/online-book-library-modern-education-mobile-app-page-onboard-screen-vector-id1125281949?b=1&k=20&m=1125281949&s=612x612&w=0&h=6bBMWCvfAKJe_kYg1-43k2laYIAbZ6moCxA3efrhK38=";

    ProgressBar p_userhome;
    ListView lstbooks_user;
    ArrayList<Book> bookslist_user;
    DatabaseReference reference_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);
        // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        sharedPreferences_signup=getSharedPreferences("Login",MODE_PRIVATE);
        if(sharedPreferences_signup.contains("email")&& sharedPreferences_signup.contains("password")) {
            String email = sharedPreferences_signup.getString("email", "");
            if (email.equals("")) {
                Intent obj_home = new Intent(getApplicationContext(), signup.class);
                startActivity(obj_home);

            } else {


                firebaseAuth = FirebaseAuth.getInstance();
                firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null && firebaseUser.isEmailVerified()) {


                    lstbooks_user = findViewById(R.id.lstbooks_user);
                    bookslist_user = new ArrayList<Book>();
                    p_userhome = findViewById(R.id.progressBar_userhome);

                    p_userhome.setVisibility(View.VISIBLE);


                    sharedPreferences_logout = getSharedPreferences("Login", MODE_PRIVATE);

                    //navigation menu
                    toolbar_userhome = (Toolbar) findViewById(R.id.toolbar_userhome);
                    setSupportActionBar(toolbar_userhome);

                    nav_userhome = (NavigationView) findViewById(R.id.navmenu_userhome);
                    drawerLayout_userhome = (DrawerLayout) findViewById(R.id.drawer_userhome);

                    toggle_userhome = new ActionBarDrawerToggle(this, drawerLayout_userhome, toolbar_userhome, R.string.open, R.string.close);
                    drawerLayout_userhome.addDrawerListener(toggle_userhome);
                    toggle_userhome.syncState();

                    nav_userhome.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                        @Override
                        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.menu_userhome:
                                    Intent ob_home = new Intent(getApplicationContext(), user_home.class);
                                    startActivity(ob_home);
                                    drawerLayout_userhome.closeDrawer(GravityCompat.START);
                                    break;

                                case R.id.menu_usercategories:
                                    Intent ob_c = new Intent(getApplicationContext(), user_categories.class);
                                    startActivity(ob_c);
                                    drawerLayout_userhome.closeDrawer(GravityCompat.START);
                                    break;

                                case R.id.menu_useruploadbook:
                                    Intent objupload = new Intent(getApplicationContext(), upload_book.class);
                                    startActivity(objupload);
                                    drawerLayout_userhome.closeDrawer(GravityCompat.START);
                                    break;


                               /* case R.id.menu_usercurrentRead:
                                    Intent objc = new Intent(getApplicationContext(), user_book_collection.class);
                                    startActivity(objc);
                                    //Toast.makeText(getApplicationContext(), "current read", Toast.LENGTH_SHORT).show();
                                    drawerLayout_userhome.closeDrawer(GravityCompat.START);
                                    break;*/


                                case R.id.menu_useraccount:
                                    Intent obj_account1 = new Intent(getApplicationContext(), user_account.class);
                                    startActivity(obj_account1);
                                    drawerLayout_userhome.closeDrawer(GravityCompat.START);
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
                                    drawerLayout_userhome.closeDrawer(GravityCompat.START);
                                    break;
                            }
                            return true;
                        }
                    });

                    sliderView_userhome = findViewById(R.id.slider_userhome);

                    slider();
                    initializaListview_user();
                } else {
                    Intent h = new Intent(getApplicationContext(), home.class);
                    startActivity(h);
                }
            }
        }

    }

    private void initializaListview_user() {

        reference_user=FirebaseDatabase.getInstance().getReference("Book");
        reference_user.addChildEventListener(new ChildEventListener() {
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

                Book searchModel = new Book(bookname, bookauthor, bookcategory,bookimage,bookpdf,bookpublisher,bookdescription);
                bookslist_user.add(searchModel);

                customAdapter_user customAdapter_user = new customAdapter_user(getApplicationContext(), bookslist_user);
                lstbooks_user.setAdapter(customAdapter_user);

                p_userhome.setVisibility(View.GONE);
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

    private void slider()
    {
        ArrayList<SliderData> sliderDataArrayList = new ArrayList<>();
        sliderDataArrayList.add(new SliderData(url1));
        sliderDataArrayList.add(new SliderData(url2));
        sliderDataArrayList.add(new SliderData(url3));
        SliderAdapter adapter = new SliderAdapter(getApplicationContext(), sliderDataArrayList);

        sliderView_userhome.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);
        sliderView_userhome.setSliderAdapter(adapter);
        sliderView_userhome.setScrollTimeInSec(3);
        sliderView_userhome.setAutoCycle(true);
        sliderView_userhome.startAutoCycle();
    }


    //add to fav
    public static void addToFavorite(Context context, String bookId)
    {
        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() == null)
        {
            Toast.makeText(context, "You're not logged in", Toast.LENGTH_SHORT).show();

        }
        else
        {
            long timestamp=System.currentTimeMillis();
            //setup data to add in firebase
            HashMap<String, Object> hashMap=new HashMap<>();
            hashMap.put("bookId",""+bookId);
            hashMap.put("timestamp",""+timestamp);

            //save
            DatabaseReference ref= FirebaseDatabase.getInstance().getReference("user");
            ref.child(firebaseAuth.getUid()).child("Favorites").child(bookId)
                    .setValue(hashMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                            Toast.makeText(context, "Added to Fav list...", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, "Failedd to add"+ e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });
        }
    }

    public static void removeFromFavorite(Context context,String bookId)
    {
        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() == null)
        {
            Toast.makeText(context, "You're not logged in", Toast.LENGTH_SHORT).show();

        }
        else
        {

            //remove from db
            DatabaseReference ref= FirebaseDatabase.getInstance().getReference("user");
            ref.child(firebaseAuth.getUid()).child("Favorites").child(bookId)
                    .removeValue()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                            Toast.makeText(context, "removed from Fav list...", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, "Failedd to remove"+ e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });
        }
    }


}