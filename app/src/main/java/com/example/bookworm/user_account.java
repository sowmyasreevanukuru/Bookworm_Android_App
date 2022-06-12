package com.example.bookworm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebIconDatabase;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class user_account extends AppCompatActivity {
    NavigationView usernav_Account;
    ActionBarDrawerToggle usertoggle_account;
    DrawerLayout userdrawerLayout_accoount;
    Toolbar usertoolbar_account;
    AppCompatEditText txtaccount_name,txtaccount_email,txtaccount_password;
    String fname,lname,email,password;
    AppCompatButton btneditprofile;
    SharedPreferences sharedPreferences_logout;
    FirebaseAuth authprofile;
    ProgressBar progressBar_account;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account);

        sharedPreferences_logout=getSharedPreferences("Login",MODE_PRIVATE);

        txtaccount_email=findViewById(R.id.txtaccount_email);
        txtaccount_name=findViewById(R.id.txtaccount_name);
        txtaccount_password=findViewById(R.id.txtaccount_password);
        btneditprofile=findViewById(R.id.btn_editprofile);
        progressBar_account=findViewById(R.id.progressBar_account);
        txtaccount_password.setEnabled(false);
        txtaccount_email.setEnabled(false);
        txtaccount_name.setEnabled(false);


        usertoolbar_account=(Toolbar)findViewById(R.id.usertoolbar_account);
        setSupportActionBar(usertoolbar_account);

        usernav_Account=(NavigationView) findViewById(R.id.navusermenu_account);
        userdrawerLayout_accoount=(DrawerLayout) findViewById(R.id.userdrawer_account);

        usertoggle_account=new ActionBarDrawerToggle(this,userdrawerLayout_accoount,usertoolbar_account,R.string.open,R.string.close);
        userdrawerLayout_accoount.addDrawerListener(usertoggle_account);
        usertoggle_account.syncState();


        usernav_Account.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.menu_userhome:
                        //Toast.makeText(getApplicationContext(), "Home panel", Toast.LENGTH_SHORT).show();
                        Intent ih=new Intent(getApplicationContext(),user_home.class);
                        startActivity(ih);
                        userdrawerLayout_accoount.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.menu_usercategories:
                        // Toast.makeText(getApplicationContext(), "Categories", Toast.LENGTH_SHORT).show();
                        Intent ic=new Intent(getApplicationContext(),user_categories.class);
                        startActivity(ic);
                        userdrawerLayout_accoount.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.menu_useruploadbook:
                        //Toast.makeText(getApplicationContext(), "upload book", Toast.LENGTH_SHORT).show();
                        Intent objupload=new Intent(getApplicationContext(),upload_book.class);
                        startActivity(objupload);
                        userdrawerLayout_accoount.closeDrawer(GravityCompat.START);
                        break;




                    case R.id.menu_useraccount:
                        //Toast.makeText(getApplicationContext(), "account", Toast.LENGTH_SHORT).show();
                        Intent obj_account=new Intent(getApplicationContext(),user_account.class);
                        startActivity(obj_account);
                        userdrawerLayout_accoount.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.menu_userlogout:
                        if(sharedPreferences_logout.contains("email") && sharedPreferences_logout.contains("password"))
                        {
                            SharedPreferences.Editor editor=sharedPreferences_logout.edit();
                            editor.putString("email","");
                            editor.putString("password","");
                            editor.commit();
                        }
                        Intent objlogout=new Intent(getApplicationContext(),home.class);
                        startActivity(objlogout);
                        userdrawerLayout_accoount.closeDrawer(GravityCompat.START);
                        break;

                }

                return true;
            }
        });

        getdetails();
    }
    private void getdetails()
    {
        progressBar_account.setVisibility(View.VISIBLE);
        authprofile=FirebaseAuth.getInstance();
        FirebaseUser firebaseUser_profile=authprofile.getCurrentUser();
        if(firebaseUser_profile==null)
        {
            Toast.makeText(getApplicationContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            String uid=firebaseUser_profile.getUid();

            //Extracting user reference from database for registered users
            DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("user");
            databaseReference.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    email = snapshot.child("email").getValue().toString();
                    fname=snapshot.child("fname").getValue().toString();
                    lname=snapshot.child("lname").getValue().toString();
                    password=snapshot.child("password").getValue().toString();

                    //txtaccount_email.setEnabled(true);
                    txtaccount_email.setText(email);
                    txtaccount_name.setText(fname+" "+lname);
                    txtaccount_password.setText(password);

                    //  Toast.makeText(getApplicationContext(),email, Toast.LENGTH_SHORT).show();
                    progressBar_account.setVisibility(View.INVISIBLE);
                    btneditprofile.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            FirebaseUser id=FirebaseAuth.getInstance().getCurrentUser();
                            Intent obj_edit=new Intent(getApplicationContext(),user_editprofile.class);
                            obj_edit.putExtra("currentuser",id.getUid());
                            obj_edit.putExtra("fname",fname);
                            obj_edit.putExtra("lname",lname);
                            obj_edit.putExtra("email",email);
                            obj_edit.putExtra("password",password);


                            //Toast.makeText(getApplicationContext(),id.getUid(),Toast.LENGTH_SHORT).show();
                            startActivity(obj_edit);
                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        }
    }
}