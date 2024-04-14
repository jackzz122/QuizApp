package com.example.quizapp_btl;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.example.quizapp_btl.User_Account.PlayerFragment;
import com.example.quizapp_btl.User_Account.UserHomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class UserActivity extends AppCompatActivity {
    FrameLayout framUser;
    BottomNavigationView bottomNavUser;

    FragmentManager fragmentManager;
    private Fragment UserHomeFragment = new UserHomeFragment();
    private Fragment PlayerFragment = new PlayerFragment();
    Fragment active = UserHomeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        init();
    }
    public void init(){
        framUser = findViewById(R.id.FrameLayoutUser);
        bottomNavUser = findViewById(R.id.BottomNavUser);
        fragmentManager = getSupportFragmentManager();
//        UserHomeFragment userHome = new UserHomeFragment();
        fragmentManager.beginTransaction().add(R.id.FrameLayoutUser, PlayerFragment, "Account").hide(PlayerFragment).commit();
        fragmentManager.beginTransaction().add(R.id.FrameLayoutUser, UserHomeFragment, "Userhome").commit();
        bottomNavUser.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.home){
//                    UserHomeFragment userHome = new UserHomeFragment();
//                    fragmentManager.beginTransaction().replace(R.id.FrameLayoutUser, userHome).commit();
                    fragmentManager.beginTransaction().hide(active).show(UserHomeFragment).commit();
                    active = UserHomeFragment;
                    return true;
                }
                else if(item.getItemId() == R.id.user){
                    fragmentManager.beginTransaction().hide(active).show(PlayerFragment).commit();
                    active = PlayerFragment;
                    return true;
                }
                return true;
            }
        });
    }

}