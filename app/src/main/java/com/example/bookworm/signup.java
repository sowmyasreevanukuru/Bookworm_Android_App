package com.example.bookworm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class signup extends AppCompatActivity {
    AppCompatButton btnbacklogin,btnsignup;
    AppCompatEditText txtsignupfname,txtsignuplname,txtsignupemail,txtsignupassword,txtsignureenterpassword;
    AppCompatTextView txtlogin;
    ProgressBar progressBar;
    FirebaseAuth fAuth;
    SharedPreferences sharedPreferences_signup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        ControlInitialization();

       signupCode();
    }
    private void ControlInitialization()
    {

        btnbacklogin=findViewById(R.id.btnbacklogin);
        btnsignup=findViewById(R.id.btnsignup);
        txtlogin=findViewById(R.id.txtlogin);
        txtsignupemail=findViewById(R.id.txtsignupemail);
        txtsignupassword=findViewById(R.id.txtsignupassword);
        txtsignureenterpassword=findViewById(R.id.txtsignureenterpassword);
        txtsignupfname=findViewById(R.id.txtsignupfname);
        txtsignuplname=findViewById(R.id.txtsignuplname);
        progressBar=findViewById(R.id.progressBar);
        fAuth=FirebaseAuth.getInstance();
    }
    private void signupCode()
    {
        btnbacklogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent bcklogin=new Intent(getApplicationContext(),login.class);
                startActivity(bcklogin);
            }
        });

        txtlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent bcktxtlogin=new Intent(getApplicationContext(),login.class);
                startActivity(bcktxtlogin);
            }
        });

        if(fAuth.getCurrentUser() != null)
        {
            startActivity(new Intent(getApplicationContext(),user_home.class));
            finish();
        }

        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fname=txtsignupfname.getText().toString();
                String lname=txtsignuplname.getText().toString();
                String email=txtsignupemail.getText().toString();
                String password=txtsignupassword.getText().toString();
                String repassword=txtsignureenterpassword.getText().toString();

                if(fname.equals("") || lname.equals("") || email.equals("") || password.equals("") || repassword.equals(""))
                {
                    txtsignupemail.setError("Please fill");
                    txtsignuplname.setError("Please fill");
                    txtsignupfname.setError("Please fill");
                    txtsignupassword.setError("Please fill");
                    txtsignureenterpassword.setError("Please fill");

                    //empty all edit text
                }

                if(txtsignupassword.length()<6)
                {
                    txtsignupassword.setError("Password must be more than 6 characters");
                    return;
                }
                if(!(txtsignupassword.getText().toString()).equals(txtsignureenterpassword.getText().toString()))
                {
                    Toast.makeText(getApplicationContext(), "PASSWORD DOESN'T MATCH", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                //register user in firebase

                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            UserHelperClass userhelper=new UserHelperClass(fname,lname,email,password);
                            FirebaseDatabase.getInstance().getReference("user").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(userhelper).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    fAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful())
                                            {
                                                Toast.makeText(getApplicationContext(), "Registered successfully,Please check your email for verification", Toast.LENGTH_SHORT).show();

                                                txtsignupfname.setText("");
                                                txtsignuplname.setText("");
                                                txtsignupassword.setText("");
                                                txtsignureenterpassword.setText("");
                                                txtsignupemail.setText("");

                                                Intent ob_login=new Intent(getApplicationContext(),login.class);
                                                startActivity(ob_login);
                                            }
                                            else {
                                                Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                                }
                            });

                            //startActivity(new Intent(getApplicationContext(),user_categories.class));
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "USER NOT CREATED"+ task.getException().getMessage(), Toast.LENGTH_SHORT).show(); //+ task.getException().getMessage()
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });

    }
}