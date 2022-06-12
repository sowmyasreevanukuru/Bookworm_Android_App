package com.example.bookworm;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;

public class home extends AppCompatActivity {
    NavigationView nav;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    SliderView sliderView;
    String url2="https://thumbs.dreamstime.com/b/online-library-e-book-reading-concept-vector-illustration-virtual-mobile-app-applications-downloading-books-228459438.jpg";
    String url1="https://image.freepik.com/free-vector/online-library-app-reading-banner_33099-1733.jpg";
    String url3="https://media.istockphoto.com/vectors/online-book-library-modern-education-mobile-app-page-onboard-screen-vector-id1125281949?b=1&k=20&m=1125281949&s=612x612&w=0&h=6bBMWCvfAKJe_kYg1-43k2laYIAbZ6moCxA3efrhK38=";

    ProgressBar p_home;
    ListView lstbooks;
    ArrayList<Book> bookslist;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        lstbooks=findViewById(R.id.lstbooks);
        bookslist=new ArrayList<Book>();
        p_home=findViewById(R.id.progressBar_home);

        p_home.setVisibility(View.VISIBLE);

       // recview = (RecyclerView) findViewById(R.id.recview);
       // recview.setLayoutManager(new LinearLayoutManager(this));

      //  FirebaseRecyclerOptions<Book> options =
                new FirebaseRecyclerOptions.Builder<Book>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Book"), Book.class)
                        .build();

      //  adapter=new myadapter(options);
        //recview.setAdapter(adapter);

        //navigation menu
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        nav=(NavigationView) findViewById(R.id.navmenu);
        drawerLayout=(DrawerLayout) findViewById(R.id.drawer);

        toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.menu_home:
                        Intent ihh=new Intent(getApplicationContext(),home.class);
                        startActivity(ihh);
                        //Toast.makeText(getApplicationContext(), "Home panel", Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.menu_categories:
                        Intent hv=new Intent(getApplicationContext(),categories.class);
                        startActivity(hv);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.menu_login:
                        Intent i=new Intent(getApplicationContext(),login.class);
                        startActivity(i);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                }

                return true;
            }
        });
        sliderView = findViewById(R.id.slider);

        slider();
        initializaListview();
    }

    private void initializaListview() {
       // ArrayAdapter<String> adapter=new ArrayAdapter<>(getApplicationContext(),R.layout.support_simple_spinner_dropdown_item,bookslist);
        reference=FirebaseDatabase.getInstance().getReference("Book");
        reference.addChildEventListener(new ChildEventListener() {
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
                bookslist.add(searchModel);

                customAdapter customAdapter = new customAdapter(getApplicationContext(), bookslist);
                lstbooks.setAdapter(customAdapter);

                p_home.setVisibility(View.GONE);
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
       // lstbooks.setAdapter(adapter);

    }

    private void slider()
    {
        ArrayList<SliderData> sliderDataArrayList = new ArrayList<>();

        sliderDataArrayList.add(new SliderData(url1));
        sliderDataArrayList.add(new SliderData(url2));
        sliderDataArrayList.add(new SliderData(url3));
        SliderAdapter adapter = new SliderAdapter(getApplicationContext(), sliderDataArrayList);
        sliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);
        sliderView.setSliderAdapter(adapter);
        sliderView.setScrollTimeInSec(3);
        sliderView.setAutoCycle(true);
        sliderView.startAutoCycle();
    }
}