package com.example.zenzone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ShareCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.zenzone.Fragments.AddPostActivity;
import com.example.zenzone.Fragments.HomeFragment;
import com.example.zenzone.Fragments.NotificationFragment;
import com.example.zenzone.Fragments.OtherFragments;
import com.example.zenzone.Fragments.ProfileFragment;
import com.example.zenzone.Fragments.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    BottomNavigationView bottomNavigationView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        init();

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.container, new HomeFragment());
        ft.commit();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.container, new HomeFragment());
                ft.commit();
                int id = item.getItemId();
                if (id == R.id.nav_home) {
                    ft.replace(R.id.container, new HomeFragment());
                } else if (id == R.id.nav_search) {
                    ft.replace(R.id.container, new SearchFragment());
                } else if (id == R.id.nav_add) {
                    ft.replace(R.id.container, new OtherFragments());
                } else if (id == R.id.nav_notifications) {
                    ft.replace(R.id.container, new NotificationFragment());
                } else if (id == R.id.nav_profile) {
                    ft.replace(R.id.container, new ProfileFragment());
                }

                return true;
            }
        });
    }

    private void init() {
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_nav);

        //Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("ZenZone");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.getOverflowIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_share:
                String appPackageName = getPackageName();
                String message = "Check out this amazing app: https://play.google.com/store/apps/details?id=" + appPackageName;

                ShareCompat.IntentBuilder
                        .from(this)
                        .setType("text/plain")
                        .setChooserTitle("Share via")
                        .setText(message)
                        .startChooser();
                Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.nav_rateus:
                Toast.makeText(this, "Rate Us", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.nav_logout:
                mAuth.signOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}