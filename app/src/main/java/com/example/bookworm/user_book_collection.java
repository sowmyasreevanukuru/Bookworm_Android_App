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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;

public class user_book_collection extends AppCompatActivity {
    NavigationView nav_collection;
    ActionBarDrawerToggle toggle_collection;
    DrawerLayout drawerLayout_collection;
    Toolbar toolbar_collection;
    SharedPreferences sharedPreferences_logout;
    ListView lstbook_fav;
    FirebaseAuth firebaseAuth_fav;
    DatabaseReference databaseReference_book;
    ArrayList<Book> bookslist_user_fav;
    DatabaseReference databaseReference_fav,databaseReference_book_2;

    ListView lstbooks_user_fav;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_book_collection);
        firebaseAuth_fav=FirebaseAuth.getInstance();
        //navigation menu
        toolbar_collection = (Toolbar) findViewById(R.id.toolbar_collection);
        setSupportActionBar(toolbar_collection);

        nav_collection = (NavigationView) findViewById(R.id.navmenu_collection);
        drawerLayout_collection = (DrawerLayout) findViewById(R.id.drawer_collection);

        toggle_collection = new ActionBarDrawerToggle(this, drawerLayout_collection, toolbar_collection, R.string.open, R.string.close);
        drawerLayout_collection.addDrawerListener(toggle_collection);
        toggle_collection.syncState();

        nav_collection.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_userhome:
                        Intent ob_home=new Intent(getApplicationContext(),user_home.class);
                        startActivity(ob_home);
                        drawerLayout_collection.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.menu_usercategories:
                        Intent ob_c=new Intent(getApplicationContext(),user_categories.class);
                        startActivity(ob_c);
                        drawerLayout_collection.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.menu_useruploadbook:
                        Intent objupload = new Intent(getApplicationContext(), upload_book.class);
                        startActivity(objupload);
                        drawerLayout_collection.closeDrawer(GravityCompat.START);
                        break;




                    case R.id.menu_useraccount:
                        Intent obj_account1 = new Intent(getApplicationContext(), user_account.class);
                        startActivity(obj_account1);
                        drawerLayout_collection.closeDrawer(GravityCompat.START);
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
                        drawerLayout_collection.closeDrawer(GravityCompat.START);
                        break;
                }
                return true;
            }
        });

        lstbook_fav=findViewById(R.id.lstbooks_fav);
        loadfavorites();
    }

    private void loadfavorites() {
        databaseReference_book_2=FirebaseDatabase.getInstance().getReference("Book");
        databaseReference_book=FirebaseDatabase.getInstance().getReference("Book");
        databaseReference_book.addChildEventListener(new ChildEventListener() {
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


                databaseReference_fav= FirebaseDatabase.getInstance().getReference("user");
                databaseReference_fav.child(firebaseAuth_fav.getUid()).child("Favorities").child(bookname)
                        .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot_fav) {
                        Log.i("sn",snapshot_fav.toString());
                        String get_key=snapshot_fav.getKey();
                        if (get_key.equals(bookname))
                        {
                            databaseReference_book_2=FirebaseDatabase.getInstance().getReference("Book");
                            databaseReference_book_2.addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                    //  Toast.makeText(getApplicationContext(), snapshot.child("bookAuthor").getValue().toString(), Toast.LENGTH_LONG).show();

                                    String bookname = snapshot.child("bookName").getValue().toString();
                                    String bookauthor = snapshot.child("bookAuthor").getValue().toString();
                                    String bookcategory = snapshot.child("bookCategory").getValue().toString();
                                    String bookimage = snapshot.child("coverimg").getValue().toString();
                                    String bookpdf = snapshot.child("bookpdf").getValue().toString();
                                    String bookpublisher = snapshot.child("bookPublisher").getValue().toString();
                                    String bookdescription = snapshot.child("bookDescription").getValue().toString();
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
                       // Toast.makeText(getApplicationContext(), snapshot_fav.toString(), Toast.LENGTH_SHORT).show();
                            Book searchModel = new Book(bookname, bookauthor, bookcategory,bookimage,bookpdf,bookpublisher,bookdescription);
                            bookslist_user_fav.add(searchModel);

                            customAdapter_userfav customAdapter_user = new customAdapter_userfav(getApplicationContext(), bookslist_user_fav);
                            lstbooks_user_fav.setAdapter(customAdapter_user);




                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



                //p_userhome.setVisibility(View.GONE);
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
}