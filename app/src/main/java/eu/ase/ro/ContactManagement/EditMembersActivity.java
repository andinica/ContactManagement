package eu.ase.ro.ContactManagement;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;

import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.List;

import eu.ase.ro.ContactManagement.async.Callback;
import eu.ase.ro.ContactManagement.db.ContactService;
import eu.ase.ro.ContactManagement.model.Contact;
import eu.ase.ro.ContactManagement.utils.ContactAdapter;
import eu.ase.ro.ContactManagement.utils.EditContactsAdapter;
import eu.ase.ro.ContactManagement.utils.MemberAdapter;

public class EditMembersActivity extends AppCompatActivity {

    public static final String ACTION = "action";
    public static final String UPDATE_ACTION = "update";
    public static final String UPDATED_POSITION= "updatePosition";
    public static final String ADD_ACTION = "add";
    private ActivityResultLauncher<Intent> launcher;


    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private FloatingActionButton fabAddContact;
    ListView lvMembers;
    private TextInputEditText tietEditMembers;
    private CheckBox cbContacts;

    private List<Contact> contacts = new ArrayList<>();

    private ContactService contactService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_members);

        initComponents();
        launcher = getLauncher();
        contactService = new ContactService(getApplicationContext());
        contactService.getAll(getAllCallback()); // GET ONLY FROM GROUP
    }

    public void initComponents(){

        tietEditMembers = findViewById(R.id.tiet_edit_members);
        lvMembers = findViewById(R.id.lv_edit_group_contact);
        lvMembers.setOnItemClickListener(getItemClickEventListener());// EDIT TO BE CHECKBOX, HAVE A LIST
        cbContacts = findViewById(R.id.cb_lv_edit_contact);
        // handle checkbox events here (or you can create a separate method for it)

        addAdapter();
        tietEditMembers.addTextChangedListener(searchTextWatcher());
    }

    private TextWatcher searchTextWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().isEmpty()) {
                    // Fetch all contacts
                    contactService.getAll(new Callback<List<Contact>>() {
                        @Override
                        public void runResultOnUiThread(List<Contact> result) {
                            if (result != null) {
                                contacts.clear();
                                contacts.addAll(result);
                                notifyAdapter();
                            }
                        }
                    });
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String searchTerm = editable.toString().trim();
                contactService.queryContacts(searchTerm, new Callback<List<Contact>>() {
                    @Override
                    public void runResultOnUiThread(List<Contact> result) {
                        if (result != null) {
                            contacts.clear();
                            contacts.addAll(result);
                            notifyAdapter();
                        }
                    }
                });
            }
        };

    }


    private View.OnClickListener getAddContactEvent() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditMembersActivity.this, AddContactActivity.class);
                intent.putExtra(ACTION, ADD_ACTION);
                launcher.launch(intent);
            }
        };
    }
    private ActivityResultLauncher<Intent> getLauncher() {
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
                    Log.i("MainActivityDrawerHome", "Position "+ position);
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
                Log.i("MainActivityDrawerHome", "Position "+ position);
                contact.setAddress(result.getAddress());
                contact.setFirstName(result.getFirstName());
                contact.setLastName(result.getLastName());
                contact.setGroupId(result.getGroupId());
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
                Log.i("MainActivityDrawerHome", "Contact on getInsertCallback" + result.toString());
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
        EditContactsAdapter adapter = new EditContactsAdapter(getApplicationContext(), R.layout.lv_item_edit_contact, contacts, getLayoutInflater());
        lvMembers.setAdapter(adapter);
    }

    private void notifyAdapter() {
        EditContactsAdapter adapter = (EditContactsAdapter) lvMembers.getAdapter();
        adapter.notifyDataSetChanged();
    }
    private AdapterView.OnItemClickListener getItemClickEventListener() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        };
    }
}
