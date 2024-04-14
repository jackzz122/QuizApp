package com.example.quizapp_btl;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.quizapp_btl.Fragment.HomeFragment;
import com.example.quizapp_btl.Fragment.UserFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    CardView cardview;
    FrameLayout frameLayout;
    BottomNavigationView bottomNavigationView;

    final Fragment homeFragment = new HomeFragment();
    final Fragment userFragment = new UserFragment();
    final FragmentManager fragmentManager = getSupportFragmentManager();
    Fragment active = homeFragment;
    SearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    public void init(){
        frameLayout = findViewById(R.id.FrameLayout);
        bottomNavigationView = findViewById(R.id.bottom);
        fragmentManager.beginTransaction().add(R.id.FrameLayout, userFragment, "USER").hide(userFragment).commit();
        fragmentManager.beginTransaction().add(R.id.FrameLayout, homeFragment, "HOME").commit();
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.home){
                    fragmentManager.beginTransaction().hide(active).show(homeFragment).commit();
                    active = homeFragment;
                    return true;
                }
                if(item.getItemId() == R.id.user){
                    fragmentManager.beginTransaction().hide(active).show(userFragment).commit();
                    active = userFragment;
                    return true;
                }
                return false;
            }
        });
    }
    public void handleDelete(int position){

    }
}