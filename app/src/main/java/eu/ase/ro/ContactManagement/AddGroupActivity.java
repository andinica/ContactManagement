package eu.ase.ro.ContactManagement;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

import eu.ase.ro.ContactManagement.async.Callback;
import eu.ase.ro.ContactManagement.db.ContactService;
import eu.ase.ro.ContactManagement.db.GroupService;
import eu.ase.ro.ContactManagement.model.Contact;
import eu.ase.ro.ContactManagement.model.Group;
import eu.ase.ro.ContactManagement.utils.MemberAdapter;

public class AddGroupActivity extends AppCompatActivity {


    public static final String GROUP_KEY = "groupKey";

    private Intent intent;
    private Group group = null;
    private Button btnSave;
    TextInputEditText tietGroupName;
    private Long groupId = null;
    GroupService groupService;
    ContactService contactService;
    private List<Contact> members = new ArrayList<>();
    ListView lvMembers;
    MemberAdapter memberAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("AddGroupActivity", "Group ID: " + groupId);
        setContentView(R.layout.activity_add_group);
        initComponents();
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
        tietGroupName = findViewById(R.id.add_group_name_tiet);
        btnSave = findViewById(R.id.add_group_save_btn);
        btnSave.setOnClickListener(saveGroupEventListener());
        lvMembers = findViewById(R.id.add_group_members_lv);
        addMemberAdapter();
    }
    private View.OnClickListener saveGroupEventListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValid()) {
                    createFromViews();
                    Log.i("MainActivityDrawer","Group is: " + group.toString());
                    intent.putExtra(GROUP_KEY, group);
                    setResult(RESULT_OK, intent);
                    finish();
                }
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
    }

    private void addMemberAdapter() {
        memberAdapter = new MemberAdapter(getApplicationContext(), R.layout.lv_item_member, members, getLayoutInflater());
        lvMembers.setAdapter(memberAdapter);
    }

    private void notifyAdapter() {
        MemberAdapter adapter = (MemberAdapter) lvMembers.getAdapter();
        adapter.notifyDataSetChanged();
    }
}
