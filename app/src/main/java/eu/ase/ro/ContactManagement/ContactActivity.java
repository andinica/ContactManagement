package eu.ase.ro.ContactManagement;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

public class ContactActivity extends AppCompatActivity {

    public static final String ACTION = "action";
    public static final String UPDATE_ACTION = "update";
    public static final String ADD_ACTION = "add";

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private FloatingActionButton fabAddContact;

    private ActivityResultLauncher<Intent> addContactLauncher;

    private Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        configNavigation();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_contact);
        fabAddContact = findViewById(R.id.fab_add_contact);
        fabAddContact.setOnClickListener(getAddContactEvent());

        addContactLauncher = registerAddContactLauncher();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.nav_group){
                    navigationView.setCheckedItem(R.id.nav_group);
                    Log.i("MainActivityDrawerHome", "Pressed contacts");
                    Toast.makeText(getApplicationContext(),
                            getString(R.string.show_option, item.getTitle()),
                            Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(ContactActivity.this, GroupActivity.class);
                    startActivity(intent);
                    finish();
                }
                if(item.getItemId() == R.id.nav_chart){
                    navigationView.setCheckedItem(R.id.nav_chart);
                    Log.i("MainActivityDrawerHome", "Pressed charts");
                    Toast.makeText(getApplicationContext(),
                            getString(R.string.show_option, item.getTitle()),
                            Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(ContactActivity.this, ChartActivity.class);
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

    private View.OnClickListener getAddContactEvent() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContactActivity.this, AddContactActivity.class);
//                intent.putExtra(ACTION, ADD_ACTION);
                addContactLauncher.launch(intent);
            }
        };
    }

    private ActivityResultLauncher<Intent> registerAddContactLauncher() {

        ActivityResultCallback<ActivityResult> callback = getAddContactResultCallback();
        return registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), callback);
    }

    //
    private ActivityResultCallback<ActivityResult> getAddContactResultCallback() {
        return new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
//                    Contact contact = result.getData().getParcelableExtra(AddCo.EXPENSE_KEY);
//                    expenses.add(expense);
//                    Log.i("MainActivity", expenses.toString());
//                    if (currentFragment instanceof HomeFragment) {
//                        ((HomeFragment) currentFragment).notifyAdapter();
//                    }
                }
            }
        };
    }



//    @Override TO DELETE
//    public boolean onCreateOptionsMenu(Menu menu) {
//        super.onCreateOptionsMenu(menu);
//        getMenuInflater().inflate(R.menu.old_main_menu, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) { // same as onNavigationItemSelected
//        super.onOptionsItemSelected(item);
//        if(item.getItemId() == R.id.new_expense_menu){
//            Log.i("MainActivityOldMenu", "New expense option was pressed");
//            Toast.makeText(getApplicationContext(),
//                    getString(R.string.show_option, item.getTitle()), Toast.LENGTH_LONG).show();
//        }
//        return true;
//    }
}