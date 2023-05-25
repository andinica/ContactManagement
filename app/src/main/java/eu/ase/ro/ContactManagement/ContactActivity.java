package eu.ase.ro.ContactManagement;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
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
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import eu.ase.ro.ContactManagement.async.Callback;
import eu.ase.ro.ContactManagement.db.ContactService;
import eu.ase.ro.ContactManagement.model.Contact;
import eu.ase.ro.ContactManagement.utils.ContactAdapter;

public class ContactActivity extends AppCompatActivity {

    public static final String ACTION = "action";
    public static final String UPDATE_ACTION = "update";
    public static final String UPDATED_POSITION= "updatePosition";
    public static final String ADD_ACTION = "add";
    private ActivityResultLauncher<Intent> launcher;


    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private FloatingActionButton fabAddContact;
    ListView lvContacts;

    private List<Contact> contacts = new ArrayList<>();
    private ActivityResultLauncher<Intent> addContactLauncher;

    private ContactService contactService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        configNavigation();


        initComponents();
        launcher = getLauncher();
        contactService = new ContactService(getApplicationContext());
        contactService.getAll(getAllCallback());

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

    public void initComponents(){

        lvContacts = findViewById(R.id.lv_contact);
        lvContacts.setOnItemClickListener(getItemClickEventListener());
        addAdapter();
        navigationView = findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_contact);
        fabAddContact = findViewById(R.id.fab_add_contact);
        fabAddContact.setOnClickListener(getAddContactEvent());

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
                intent.putExtra(ACTION, ADD_ACTION);
                addContactLauncher.launch(intent);
            }
        };
    }
    private ActivityResultLauncher<Intent> getLauncher() {
        ActivityResultCallback<ActivityResult> callback = getAddContactActivityResultCallback();
        return registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), callback);
    }

    private ActivityResultLauncher<Intent> registerAddContactLauncher() {

        ActivityResultCallback<ActivityResult> callback = getAddContactActivityResultCallback();
        return registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), callback);
    }


    private ActivityResultCallback<ActivityResult> getAddContactActivityResultCallback() {
        return new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getData() == null || result.getResultCode() != RESULT_OK) {
                    return;
                }
                Contact contact = (Contact) result.getData().getSerializableExtra(AddContactActivity.CONTACT_KEY);
                if (ADD_ACTION.equals(result.getData().getStringExtra(ACTION))) {
                    contactService.insert(contact, getInsertCallback());
                } else if (UPDATE_ACTION.equals(result.getData().getStringExtra(ACTION))) {
                    int position = result.getData().getIntExtra(UPDATED_POSITION, 0);
                    contactService.update(contact, getUpdateCallback(position));
                }
            }
        };
    }
    private Callback<Contact> getUpdateCallback(int position) {
        return new Callback<Contact>() {
            @Override
            public void runResultOnUiThread(Contact result) {
                Contact contact = contacts.get(position);
                contact.setAddress(result.getAddress());
                contact.setFirstName(result.getFirstName());
                contact.setLastName(result.getLastName());
                contact.setGroup(result.getGroup());
                contact.setPhoneNumber(result.getPhoneNumber());
                contact.setBirthday(
                        result.getBirthday()
                );

                notifyAdapter();
            }
        };
    }

    private Callback<Contact> getInsertCallback() {
        return new Callback<Contact>() {
            @Override
            public void runResultOnUiThread(Contact result) {
                //aici suntem cu notificarea din baza de date
                contacts.add(result);
                notifyAdapter();
            }
        };
    }
    private Callback<List<Contact>> getAllCallback() {
        return new Callback<List<Contact>>() {
            @Override
            public void runResultOnUiThread(List<Contact> result) {
                contacts.addAll(result);
                notifyAdapter();
            }
        };
    }

    private void addAdapter() {
        ContactAdapter adapter = new ContactAdapter(getApplicationContext(), R.layout.lv_item_contact, contacts, getLayoutInflater());
        lvContacts.setAdapter(adapter);
    }

    private void notifyAdapter() {
        ContactAdapter adapter = (ContactAdapter) lvContacts.getAdapter();
        adapter.notifyDataSetChanged();
    }
    private AdapterView.OnItemClickListener getItemClickEventListener() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), AddContactActivity.class);
                intent.putExtra(AddContactActivity.CONTACT_KEY, contacts.get(i));
                intent.putExtra(UPDATED_POSITION, i);
                intent.putExtra(ACTION, UPDATE_ACTION);
                launcher.launch(intent);
            }
        };
    }
}