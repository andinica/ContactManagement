package eu.ase.ro.ContactManagement;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

public class GroupActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        configNavigation();
        navigationView = findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_group);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.nav_contact){
                    navigationView.setCheckedItem(R.id.nav_contact);
                    Log.i("MainActivityDrawerHome", "Pressed contacts");
                    Toast.makeText(getApplicationContext(),
                            getString(R.string.show_option, item.getTitle()),
                            Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(GroupActivity.this, ContactActivity.class);
                    startActivity(intent);
                    finish();
                }
                if(item.getItemId() == R.id.nav_chart){
                    navigationView.setCheckedItem(R.id.nav_chart);
                    Log.i("MainActivityDrawerHome", "Pressed charts");
                    Toast.makeText(getApplicationContext(),
                            getString(R.string.show_option, item.getTitle()),
                            Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(GroupActivity.this, ChartActivity.class);
                    startActivity(intent);
                    finish();
                }
                if(item.getItemId() == R.id.nav_exit){
                    navigationView.setCheckedItem(R.id.nav_exit);
                    Log.i("MainActivityDrawerHome", "Pressed exit");
                    finish();
                    return true;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    private void openDefaultFragment(Bundle savedInstanceState){
        if(savedInstanceState == null){
//            currentFragment = HomeFragment.getInstance();
            openFragment();
            navigationView.setCheckedItem(R.id.nav_contact);
        }
    }

    private void openFragment(){
        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_container, currentFragment).commit();
    }

    private void configNavigation() {
        //initialize toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //initialize drawer layout
        drawerLayout = findViewById(R.id.drawer_layout);
        //link lateral menu with actionbar
        //+open event
        //create utility instance
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }
}