package com.example.bookworm;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;

public class userNavigationFragment extends Fragment {

    View view;
    NavigationView navigationView_user;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.user_navigation_fragment, container, false);
        navigationView_user=(NavigationView)view.findViewById(R.id.navuserfragmentmenu);

        navigationView_user.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.menu_userhome:
                        Toast.makeText(getActivity(), "Home panel", Toast.LENGTH_SHORT).show();
                       // userdrawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.menu_usercategories:
                        Toast.makeText(getActivity(), "Categories", Toast.LENGTH_SHORT).show();
                        Intent objc=new Intent(getActivity(),user_categories.class);
                        startActivity(objc);

                       // userdrawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.menu_useruploadbook:
                        //Toast.makeText(getApplicationContext(), "upload book", Toast.LENGTH_SHORT).show();
                        Intent objupload=new Intent(getActivity(),upload_book.class);
                        startActivity(objupload);
                      //  userdrawerLayout.closeDrawer(GravityCompat.START);
                        break;




                    case R.id.menu_useraccount:
                        //Toast.makeText(getApplicationContext(), "account", Toast.LENGTH_SHORT).show();
                        //userdrawerLayout.closeDrawer(GravityCompat.START);
                        break;



                }

                return true;
            }
        });








        return inflater.inflate(R.layout.user_navigation_fragment,container,false);
    }
}
