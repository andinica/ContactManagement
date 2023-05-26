package eu.ase.ro.ContactManagement;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import eu.ase.ro.ContactManagement.async.Callback;
import eu.ase.ro.ContactManagement.db.ContactService;
import eu.ase.ro.ContactManagement.db.GroupService;
import eu.ase.ro.ContactManagement.model.Contact;
import eu.ase.ro.ContactManagement.model.Group;
import eu.ase.ro.ContactManagement.utils.ContactAdapter;
import eu.ase.ro.ContactManagement.utils.MemberAdapter;

public class AddGroupActivity extends AppCompatActivity {


    public static final String GROUP_KEY = "groupKey";
    public static final String EDITED_CONTACTS = "editedContacts";
    private ActivityResultLauncher<Intent> launcher;

    private Intent intent;
    private Group group = null;
    private Button btnSave;
    private Button btnEdit;
    private TextInputEditText tietGroupName;
    private Long groupId = null;
    GroupService groupService;
    ContactService contactService;
    private List<Contact> members = new ArrayList<>();
    List<Contact> editedContacts = new ArrayList<>();
    ListView lvMembers;
    MemberAdapter memberAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("AddGroupActivity", "Group ID: " + groupId);
        setContentView(R.layout.activity_add_group);
        initComponents();
        launcher = getLauncher();

        intent = getIntent();
        groupService = new GroupService(getApplicationContext());
        contactService = new ContactService(getApplicationContext());
        if (intent.hasExtra(GROUP_KEY)) {
            Log.i("MainActivityDrawerHome", "has group_key");
            group = (Group) intent.getSerializableExtra(GROUP_KEY);
            Log.i("MainActivityDrawerHome", group.toString());
            tietGroupName.setText(group.getName());
            groupId = group.getId();
            contactService.getContactsByGroupId(groupId, new Callback<List<Contact>>() {
                @Override
                public void runResultOnUiThread(List<Contact> result) {
                    members.clear(); // Clear the existing list
                    members.addAll(result); // Add the retrieved contacts
                    Log.d("MainActivityDrawerHome", "Retrieved contacts: " + members.size());

                    for (Contact contact : members) {
                        Log.i("MainActivityDrawerHome", "Contact: " + contact.getFirstName() + " " + contact.getLastName());
                    }

                    memberAdapter.notifyDataSetChanged(); // Notify the adapter to refresh the view
                }
            });
        }
        else {             Log.i("MainActivityDrawerHome", "has no group_key");}
    }

    public void initComponents() {
        tietGroupName = findViewById(R.id.tiet_add_group_name);
        btnSave = findViewById(R.id.btn_add_group_save);
        btnEdit = findViewById(R.id.btn_add_group_edit);
        btnSave.setOnClickListener(saveGroupEventListener());
        btnEdit.setOnClickListener(saveGroupEventListener());
        lvMembers = findViewById(R.id.lv_activity_add_group);
        addMemberAdapter();
    }
    private View.OnClickListener saveGroupEventListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               createFromViews();
            }
        };
    }

    private View.OnClickListener getEditMembers() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddGroupActivity.this, EditMembersActivity.class);
                launcher.launch(intent);
            }
        };
    }

    private boolean isValid() {
        if (tietGroupName.getText() == null || tietGroupName.getText().toString().length() < 3) {
            Toast.makeText(getApplicationContext(),
                            R.string.add_group_name_error_msg,
                            Toast.LENGTH_SHORT)
                    .show();
            return false;
        }
        return true;
    }

    private void createFromViews() {
        String name = tietGroupName.getText().toString();
        if (group == null) {
            group = new Group(name);
        } else {
            group.setName(name);
        }
        intent.putExtra(GROUP_KEY, group);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void addMemberAdapter() {
        memberAdapter = new MemberAdapter(getApplicationContext(), R.layout.lv_item_member, members, getLayoutInflater());
        lvMembers.setAdapter(memberAdapter);
    }

    private void notifyMemberAdapter() {
        ContactAdapter adapter = (ContactAdapter) lvMembers.getAdapter();
        adapter.notifyDataSetChanged();
    }


    private ActivityResultLauncher<Intent> getLauncher() {
        ActivityResultCallback<ActivityResult> callback = getEditMembersActivityResultCallback();
        return registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), callback);
    }

    private Callback<List<Contact>> getEditCallback() {
        return new Callback<List<Contact>>() {
            @Override
            public void runResultOnUiThread(List<Contact> result) {
                members.addAll(result);
                Log.i("MainActivityDrawerHome", "Contact on getInsertCallback" + result.toString());
                notifyMemberAdapter();
            }
        };
    }


    private ActivityResultCallback<ActivityResult> getEditMembersActivityResultCallback() {
        return new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getData() == null || result.getResultCode() != RESULT_OK) {
                    return;
                }
//                contactService.updateContacts(members, getEditCallback());
            }
        };
    }

}