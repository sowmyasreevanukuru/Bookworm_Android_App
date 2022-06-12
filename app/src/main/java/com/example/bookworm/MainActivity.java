package com.example.bookworm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ProgressBar;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    AppCompatButton btnnext;
    SharedPreferences sharedPreferences_checklogin;
    Timer timer;
    ProgressBar progressBar_homescreen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                Intent i = new Intent(getApplicationContext(), home.class);
                startActivity(i);
                finish();
            }
        },2000);

        sharedPreferences_checklogin=getSharedPreferences("Login",MODE_PRIVATE);
        if(sharedPreferences_checklogin.contains("email") && sharedPreferences_checklogin.contains("password"))
        {
            String email=sharedPreferences_checklogin.getString("email","");
            if(email!="") {
                Intent obj_redirect = new Intent(getApplicationContext(), user_home.class);
                startActivity(obj_redirect);
            }
        }
        setContentView(R.layout.activity_main);

       // btnnext = findViewById(R.id.btnHomepage_next);
/*

        btnnext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getApplicationContext(), home.class);
                    startActivity(i);
                }
            });*/
    }
}