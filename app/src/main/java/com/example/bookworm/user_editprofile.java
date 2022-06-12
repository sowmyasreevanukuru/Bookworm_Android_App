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
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class user_editprofile extends AppCompatActivity {
    NavigationView usernav_editprofile;
    ActionBarDrawerToggle usertoggle_editprofile;
    DrawerLayout userdrawerLayout_editprofile;
    Toolbar usertoolbar_editprofile;
    AppCompatEditText txtprofile_fname,txtprofile_lname,txtprofile_email,txtprofile_password,txtaccount_new_retypepassword;
    String new_fname,new_lname,new_email,new_password;
    AppCompatButton btnsave;
    SharedPreferences sharedPreferences_logout;
    FirebaseAuth authprofile;
    DatabaseReference reference;
    AppCompatButton btn_savechanges;
    SharedPreferences sharedPreferences;

    String _fname,_lname,_password,_currentuser,_email;

    ProgressBar progressBar_profile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_editprofile);

        sharedPreferences_logout=getSharedPreferences("Login",MODE_PRIVATE);

        txtprofile_fname=findViewById(R.id.txtaccount_newfname);
        txtprofile_lname=findViewById(R.id.txtaccount_newlname);
        txtprofile_email=findViewById(R.id.txtaccount_newemail);
        txtprofile_password=findViewById(R.id.txtaccount_newpassword);
        txtaccount_new_retypepassword=findViewById(R.id.txtaccount_new_retypepassword);
        btn_savechanges=findViewById(R.id.btn_savechanges);
        txtprofile_email.setEnabled(false);

        progressBar_profile=findViewById(R.id.progressBar_editprofile);


        usertoolbar_editprofile=(Toolbar)findViewById(R.id.usertoolbar_editprofile);
        setSupportActionBar(usertoolbar_editprofile);

        usernav_editprofile=(NavigationView) findViewById(R.id.navusermenu_editprofile);
        userdrawerLayout_editprofile=(DrawerLayout) findViewById(R.id.userdrawer_editprofile);

        usertoggle_editprofile=new ActionBarDrawerToggle(this,userdrawerLayout_editprofile,usertoolbar_editprofile,R.string.open,R.string.close);
        userdrawerLayout_editprofile.addDrawerListener(usertoggle_editprofile);
        usertoggle_editprofile.syncState();


        usernav_editprofile.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.menu_userhome:
                        //Toast.makeText(getApplicationContext(), "Home panel", Toast.LENGTH_SHORT).show();
                        Intent ih=new Intent(getApplicationContext(),user_home.class);
                        startActivity(ih);
                        userdrawerLayout_editprofile.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.menu_usercategories:
                        //Toast.makeText(getApplicationContext(), "Categories", Toast.LENGTH_SHORT).show();
                        Intent icc=new Intent(getApplicationContext(),user_categories.class);
                        startActivity(icc);
                        userdrawerLayout_editprofile.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.menu_useruploadbook:
                        //Toast.makeText(getApplicationContext(), "upload book", Toast.LENGTH_SHORT).show();
                        Intent objupload=new Intent(getApplicationContext(),upload_book.class);
                        startActivity(objupload);
                        userdrawerLayout_editprofile.closeDrawer(GravityCompat.START);
                        break;




                    case R.id.menu_useraccount:
                        //Toast.makeText(getApplicationContext(), "account", Toast.LENGTH_SHORT).show();
                        Intent obj_account=new Intent(getApplicationContext(),user_account.class);
                        startActivity(obj_account);
                        userdrawerLayout_editprofile.closeDrawer(GravityCompat.START);
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
                        userdrawerLayout_editprofile.closeDrawer(GravityCompat.START);
                        break;

                }
                return true;
            }
        });

        reference= FirebaseDatabase.getInstance().getReference("user");

        Intent i=getIntent();
        _fname=i.getStringExtra("fname");
        _lname=i.getStringExtra("lname");
        _email=i.getStringExtra("email");
        _password=i.getStringExtra("password");
        _currentuser=i.getStringExtra("currentuser");

        txtprofile_fname.setText(_fname);
        txtprofile_lname.setText(_lname);
        txtprofile_email.setText(_email);
        txtprofile_password.setText(_password);
        txtaccount_new_retypepassword.setText(_password);


    }


    public void update(View view) {
        if(txtprofile_fname.equals("") || txtprofile_lname.equals("") || txtprofile_password.equals("") || txtaccount_new_retypepassword.equals(""))
        {
            txtprofile_fname.setError("Please fill");
            txtprofile_lname.setError("Please fill");
            txtprofile_password.setError("Please fill");
            txtaccount_new_retypepassword.setError("Please fill");

            //empty all edit text
        }

        if(txtprofile_password.length()<6)
        {
            txtprofile_password.setError("Password must be more than 6 characters");
            return;
        }
        if(!(txtprofile_password.getText().toString()).equals(txtaccount_new_retypepassword.getText().toString()))
        {
            Toast.makeText(getApplicationContext(), "PASSWORD DOESN'T MATCH", Toast.LENGTH_SHORT).show();
            return;
        }


        if(isNameChanged() || isPasswordChange())
        {
            /*SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putString("email",txtprofile_email.getText().toString());
            editor.putString("password",txtprofile_password.getText().toString());
            editor.commit();

             */
            Toast.makeText(getApplicationContext(),"Data has been Updated",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(),user_account.class));
        }
        else{
            Toast.makeText(getApplicationContext(),"Data is same cannot be updated",Toast.LENGTH_SHORT).show();
        }
    }


    private boolean isPasswordChange() {
        if(!_password.equals(txtprofile_password.getText().toString())){
            reference.child(_currentuser).child("password").setValue(txtprofile_password.getText().toString());
            _password=txtprofile_password.getText().toString();
            return true;
        }else{
            return false;
        }
    }

    private boolean isNameChanged() {
        if(!_fname.equals(txtprofile_fname.getText().toString()) || !_lname.equals(txtprofile_lname.getText().toString()) ){
            reference.child(_currentuser).child("fname").setValue(txtprofile_fname.getText().toString());
            _fname=txtprofile_fname.getText().toString();

            reference.child(_currentuser).child("lname").setValue(txtprofile_lname.getText().toString());
            _lname=txtprofile_lname.getText().toString();

            return true;
        }else{
            return false;
        }
    }
}