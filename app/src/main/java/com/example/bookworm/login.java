package com.example.bookworm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login extends AppCompatActivity {
    AppCompatButton btnbackhome,btnlogin;
    AppCompatTextView txtforgotpassword,txtsignup;
    AppCompatEditText txtusername,txtpassword;
    ProgressBar progressBar2;
    FirebaseAuth fAuth;
    DBHelper DB;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ControlInitialization();
        login_code();

    }
    private void ControlInitialization()
    {
        btnlogin=findViewById(R.id.btnLogin);
        btnbackhome=findViewById(R.id.btnback);
        txtforgotpassword=findViewById(R.id.txtforgotpassword);
        txtusername=findViewById(R.id.txtloginusername);
        txtpassword=findViewById(R.id.txtloginpassword);
        txtsignup=findViewById(R.id.txtsignup);
        progressBar2=findViewById(R.id.progressBar2);
        fAuth=FirebaseAuth.getInstance();
        sharedPreferences=getSharedPreferences("Login",MODE_PRIVATE);
        DB=new DBHelper(getApplicationContext());
    }
    private void login_code()
    {
        btnbackhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent backhome=new Intent(getApplicationContext(),home.class);
                startActivity(backhome);
            }
        });

        txtsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signuppage=new Intent(getApplicationContext(),signup.class);
                startActivity(signuppage);

            }
        });

        txtforgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRecoverPasswordDialog();
            }
        });

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=txtusername.getText().toString();
                String password=txtpassword.getText().toString();


                if(email.equals("") || password.equals("")|| TextUtils.isEmpty(txtusername.getText().toString()) || TextUtils.isEmpty(txtpassword.getText().toString()))
                {
                    txtusername.setError("Please fill");
                    txtpassword.setError("Please fill");
                    return;
                }

                progressBar2.setVisibility(View.VISIBLE);

                fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            SharedPreferences.Editor editor=sharedPreferences.edit();
                            editor.putString("email",email);
                            editor.putString("password",password);
                            editor.commit();

                            if(fAuth.getCurrentUser().isEmailVerified())
                            {
                                startActivity(new Intent(getApplicationContext(),user_home.class));
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), "Please verify your email address", Toast.LENGTH_SHORT).show();
                                progressBar2.setVisibility(View.GONE);
                            }
                            txtusername.setText("");
                            txtpassword.setText("");
                        }
                        else
                        {
                            progressBar2.setVisibility(View.INVISIBLE);
                            Toast.makeText(getApplicationContext(), "INVALID EmailID or Password", Toast.LENGTH_SHORT).show(); //+ task.getException().getMessage()
                            progressBar2.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });

    }

    private void showRecoverPasswordDialog() {

        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Recover Password");
        LinearLayout linearLayout=new LinearLayout(this);
        final EditText emailet= new EditText(this);

        // write the email using which you registered
        //emailet.setText("");
        emailet.setHint("Enter your email id");
        emailet.setMinEms(16);
        emailet.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        linearLayout.addView(emailet);
        linearLayout.setPadding(10,10,10,10);
        builder.setView(linearLayout);

        // Click on Recover and a email will be sent to your registered email id
        builder.setPositiveButton("Recover", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String email=emailet.getText().toString().trim();
                beginRecovery(email);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
    ProgressDialog loadingBar;
    private void beginRecovery(String email) {
        loadingBar=new ProgressDialog(this);
        if(email.equals("")) {
            Toast.makeText(getApplicationContext(), "Please enter your email id for new password", Toast.LENGTH_LONG).show();

        }
        else
        {

            loadingBar.setMessage("Sending Email....");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            // calling sendPasswordResetEmail
            // open your email and write the new
            // password and then you can login
            fAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    loadingBar.dismiss();
                    if (task.isSuccessful()) {
                        // if isSuccessful then done message will be shown
                        // and you can change the password
                        Toast.makeText(getApplicationContext(), "Check mail for updating your password", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Error while sending mail", Toast.LENGTH_LONG).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    loadingBar.dismiss();
                    Toast.makeText(getApplicationContext(), "Error Failed", Toast.LENGTH_LONG).show();
                }
            });
        }

    }
}