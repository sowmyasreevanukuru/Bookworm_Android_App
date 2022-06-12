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
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class categories extends AppCompatActivity {
    GridView categorygv_c;
    NavigationView usernav_c;
    ActionBarDrawerToggle usertoggle_c;
    DrawerLayout userdrawerLayout_c;
    Toolbar usertoolbar_c;
    SharedPreferences sharedPreferences_c;
    ArrayList<Category> dataModalArrayList;
    DatabaseReference databaseReference_c_visitor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        categorygv_c = findViewById(R.id.idGVCategory_category);

        //sharedPreferences_categories=getSharedPreferences("Login",MODE_PRIVATE);

        usertoolbar_c=(Toolbar)findViewById(R.id.usertoolbar_c);
        setSupportActionBar(usertoolbar_c);

        usernav_c=(NavigationView) findViewById(R.id.navusermenu_categories);
        userdrawerLayout_c=(DrawerLayout) findViewById(R.id.userdrawer_c);

        usertoggle_c=new ActionBarDrawerToggle(this,userdrawerLayout_c,usertoolbar_c,R.string.open,R.string.close);
        userdrawerLayout_c.addDrawerListener(usertoggle_c);
        usertoggle_c.syncState();


        usernav_c.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.menu_home:
                        //Toast.makeText(getApplicationContext(), "Home panel", Toast.LENGTH_SHORT).show();
                        Intent ih=new Intent(getApplicationContext(),home.class);
                        startActivity(ih);
                        userdrawerLayout_c.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.menu_categories:

                        userdrawerLayout_c.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.menu_login:
                        Intent i=new Intent(getApplicationContext(),login.class);
                        startActivity(i);
                        userdrawerLayout_c.closeDrawer(GravityCompat.START);
                        break;
                }
                return true;
            }
        });
        showcategories();
    }
    private void showcategories()
    {
        dataModalArrayList=new ArrayList<>();
        databaseReference_c_visitor= FirebaseDatabase.getInstance().getReference("Category");
        databaseReference_c_visitor.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                //  Toast.makeText(getApplicationContext(), snapshot.child("bookAuthor").getValue().toString(), Toast.LENGTH_LONG).show();
                String getc_name=snapshot.child("category_name").getValue().toString();
                String getc_imgurl=snapshot.child("imgid").getValue().toString();

                Category getc=new Category(getc_name,getc_imgurl);
                dataModalArrayList.add(getc);

                categoryGVAdapter_visitor adapter = new categoryGVAdapter_visitor(categories.this, dataModalArrayList);
                categorygv_c.setAdapter(adapter);

                // p_userhome.setVisibility(View.GONE);
                // bookslist.add(snapshot.getKey());
                // adapter.notifyDataSetChanged();
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

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(),login.class));
        finish(); }


}