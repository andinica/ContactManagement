package eu.ase.ro.ContactManagement;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import eu.ase.ro.ContactManagement.async.Callback;
import eu.ase.ro.ContactManagement.db.GroupService;
import eu.ase.ro.ContactManagement.model.Contact;
import eu.ase.ro.ContactManagement.model.Group;
import eu.ase.ro.ContactManagement.utils.GroupAdapter;

public class GroupActivity extends AppCompatActivity {


    public static final String ACTION = "action";
    public static final String UPDATE_ACTION = "update";
    public static final String UPDATED_POSITION = "updatePosition";
    public static final String ADD_ACTION = "add";
    private ActivityResultLauncher<Intent> launcher;

    private List<Group> groups = new ArrayList<>();
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private FloatingActionButton fabAddGroup;
    ListView lvGroups;
    private TextInputEditText tietGroup;

    private GroupService groupService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        configNavigation();

        initComponents();
        launcher = getLauncher();
        groupService = new GroupService(getApplicationContext());
        groupService.getAll(getAllCallback());

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

    private void initComponents() {
        // initialise your views
        tietGroup = findViewById(R.id.tiet_group);
        fabAddGroup = findViewById(R.id.fab_add_group);
        fabAddGroup.setOnClickListener(getAddGroupEvent());
        lvGroups = findViewById(R.id.lv_group);
        lvGroups.setOnItemClickListener(getItemClickEventListener());
        lvGroups.setOnItemLongClickListener(getItemLongClickEventListener());
        addAdapter();
        navigationView = findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_group);
        fabAddGroup = findViewById(R.id.fab_add_group);
        fabAddGroup.setOnClickListener(getAddGroupEvent());
        tietGroup.addTextChangedListener(searchTextWatcher());
    }

    private TextWatcher searchTextWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().isEmpty()) {
                    // Fetch all contacts
                    groupService.getAll(new Callback<List<Group>>() {
                        @Override
                        public void runResultOnUiThread(List<Group> result) {
                            if (result != null) {
                                groups.clear();
                                groups.addAll(result);
                                notifyAdapter();
                            }
                        }
                    });
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String searchTerm = editable.toString().trim();
                groupService.queryGroups(searchTerm, new Callback<List<Group>>() {
                    @Override
                    public void runResultOnUiThread(List<Group> result) {
                        if (result != null) {
                            groups.clear();
                            groups.addAll(result);
                            notifyAdapter();
                        }
                    }
                });
            }
        };

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

    private View.OnClickListener getAddGroupEvent() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GroupActivity.this, AddGroupActivity.class);
                intent.putExtra(ACTION, ADD_ACTION);
                launcher.launch(intent);
            }
        };
    }

    private ActivityResultLauncher<Intent> getLauncher() {
        ActivityResultCallback<ActivityResult> callback = getAddGroupActivityResultCallback();
        return registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), callback);
    }

    private ActivityResultCallback<ActivityResult> getAddGroupActivityResultCallback() {
        return new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getData() == null || result.getResultCode() != RESULT_OK) {
                    return;
                }
                Group group = (Group) result.getData().getSerializableExtra(AddGroupActivity.GROUP_KEY);
                if (ADD_ACTION.equals(result.getData().getStringExtra(ACTION))) {
                    groupService.insert(group, getInsertCallback());
                } else if (UPDATE_ACTION.equals(result.getData().getStringExtra(ACTION))) {
                    int position = result.getData().getIntExtra(UPDATED_POSITION, 0);
                    Log.i("MainActivityDrawerHome", "Group name "+ group.getName());
                    groupService.update(group, getUpdateCallback(position));
                }
            }
        };
    }

    private Callback<Group> getInsertCallback() {
        return new Callback<Group>() {
            @Override
            public void runResultOnUiThread(Group result) {
                //aici suntem cu notificarea din baza de date
                groups.add(result);
                Log.i("MainActivityDrawerHome", "Group name on getInsertCallback" + result.toString());
                notifyAdapter();
            }
        };
    }
    private Callback<List<Group>> getAllCallback() {
        return new Callback<List<Group>>() {
            @Override
            public void runResultOnUiThread(List<Group> result) {
                groups.addAll(result);
                notifyAdapter();
            }
        };
    }

    private Callback<Group> getUpdateCallback(int position) {
        return new Callback<Group>() {
            @Override
            public void runResultOnUiThread(Group result) {
                Group group = groups.get(position);
                group.setName(result.getName());
                notifyAdapter();
            }
        };
    }



    private void addAdapter() {
        GroupAdapter adapter = new GroupAdapter(getApplicationContext(), R.layout.lv_item_group, groups, getLayoutInflater());
        lvGroups.setAdapter(adapter);
    }

    private void notifyAdapter() {
        GroupAdapter adapter = (GroupAdapter) lvGroups.getAdapter();
        adapter.notifyDataSetChanged();
    }
    private AdapterView.OnItemClickListener getItemClickEventListener() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), AddGroupActivity.class);
                intent.putExtra(AddGroupActivity.GROUP_KEY, groups.get(i));
                intent.putExtra(UPDATED_POSITION, i);
                intent.putExtra(ACTION, UPDATE_ACTION);
                launcher.launch(intent);
            }
        };
    }

    private AdapterView.OnItemLongClickListener getItemLongClickEventListener() {
        return new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showDeleteConfirmationDialog(position);
                return true;
            }
        };
    }

    private void showDeleteConfirmationDialog(int position) {
        new AlertDialog.Builder(GroupActivity.this)
                .setTitle("Delete group")
                .setMessage("Are you sure you want to delete this group?")
                .setPositiveButton(android.R.string.no, null)
                .setNegativeButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // delete group
                        Group groupToDelete = groups.get(position);
                        groupService.delete(groupToDelete, getDeleteCallback(position));
                    }
                })
                .setIcon(ContextCompat.getDrawable(GroupActivity.this, R.drawable.ic_baseline_front_hand_24))
                .show();
    }

    private Callback<Boolean> getDeleteCallback(final int position) {
        return new Callback<Boolean>() {
            @Override
            public void runResultOnUiThread(Boolean result) {
                if(result) {
                    groups.remove(position);
                    notifyAdapter();
                    Toast.makeText(GroupActivity.this, "Group deleted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(GroupActivity.this, "Error occurred while deleting group", Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

}