package eu.ase.ro.ContactManagement;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import eu.ase.ro.ContactManagement.async.Callback;
import eu.ase.ro.ContactManagement.db.ContactService;
import eu.ase.ro.ContactManagement.db.DatabaseManager;
import eu.ase.ro.ContactManagement.db.GroupDao;
import eu.ase.ro.ContactManagement.db.GroupService;
import eu.ase.ro.ContactManagement.model.Contact;
import eu.ase.ro.ContactManagement.model.Group;
import eu.ase.ro.ContactManagement.utils.DateConverter;


public class AddContactActivity extends AppCompatActivity {
    public static final String CONTACT_KEY = "contactKey";
    private TextInputEditText tietFname;
    private TextInputEditText tietLname;
    private Spinner spnGroup;
    private TextInputEditText tietPnumber;
    private DatePicker dpBday;
    private TextInputEditText tietAddress;
    private Button btnSave;
    ContactService contactService;
    private Intent intent;
    private Contact contact = new Contact();

    private GroupService groupService;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        intent = getIntent();
        groupService = new GroupService(getApplicationContext());
        initComponents();
        contactService = new ContactService(getApplicationContext());

        if (intent.hasExtra(CONTACT_KEY)) {
            contact = (Contact) intent.getSerializableExtra(CONTACT_KEY);
            createViewFromObject();
        }
    }

    private void createViewFromObject() {
        if (contact != null) {
            tietFname.setText(contact.getFirstName());
            tietAddress.setText(contact.getAddress());
            tietLname.setText(contact.getLastName());
            tietPnumber.setText(contact.getPhoneNumber());

            if (contact.getBirthday() != null) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(contact.getBirthday());
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                dpBday.updateDate(year, month, day);
            }

            groupService.getGroupNameById(contact.getGroupId(), new Callback<String>() {
                @Override
                public void runResultOnUiThread(String result) {
                    ArrayAdapter<CharSequence> adapter = (ArrayAdapter<CharSequence>) spnGroup.getAdapter();
                    for (int i = 0; i < adapter.getCount(); i++) {
                        String item = adapter.getItem(i).toString();
                        if (item.equals(result)) {
                            spnGroup.setSelection(i);
                            break;
                        }
                    }
                }
            });
        }
    }

    private void initComponents() {
        tietFname = findViewById(R.id.add_contact_fname_tiet);
        tietLname = findViewById(R.id.add_contact_lname_tiet);
        spnGroup = findViewById(R.id.add_contact_group_spn);
        populateGroupSpinner();
        tietPnumber = findViewById(R.id.add_contact_pnumber_tiet);
        dpBday = findViewById(R.id.add_contact_bday_dp);
        tietAddress = findViewById(R.id.add_contact_address_tiet);
        btnSave = findViewById(R.id.add_contact_save_btn);
        btnSave.setOnClickListener(saveContactEventListener());
    }


    private View.OnClickListener saveContactEventListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValid()) {
                    createFromViews();
                }
            }
        };
    }




    private boolean isValid() {
        if (tietFname.getText() == null || tietFname.getText().toString().length() < 3) {
            Toast.makeText(getApplicationContext(),
                            R.string.add_contact_text_error_msg,
                            Toast.LENGTH_SHORT)
                    .show();
            return false;
        }
        if (tietLname.getText() == null || tietLname.getText().toString().length() < 3) {
            Toast.makeText(getApplicationContext(),
                            R.string.add_contact_text_error_msg,
                            Toast.LENGTH_SHORT)
                    .show();
            return false;
        }
        if (tietAddress.getText() == null || tietAddress.getText().toString().length() < 3) {
            Toast.makeText(getApplicationContext(),
                            R.string.add_contact_text_error_msg,
                            Toast.LENGTH_SHORT)
                    .show();
            return false;
        }
        if (tietPnumber.getText() == null || tietPnumber.getText().toString().length() != 10
                || tietPnumber.getText().toString().matches("[a-zA-Z]+")) {
            Toast.makeText(getApplicationContext(),
                            R.string.add_contact_pnumber_error_msg,
                            Toast.LENGTH_SHORT)
                    .show();
            return false;
        }

        return true;
    }


    private void createFromViews() {
        int day = dpBday.getDayOfMonth();
        int month = dpBday.getMonth();
        int year =  dpBday.getYear();

        String birthday = day + "/" + month + "/" + year;
        String groupName = spnGroup.getSelectedItem().toString();
        String fname = tietFname.getText().toString();
        String lname = tietLname.getText().toString();
        String pnumber = tietPnumber.getText().toString();
        String address = tietAddress.getText().toString();
        if(!groupName.equals("No groups yet, please add one")){
            groupService.getGroupIdByName(groupName, new Callback<Long>() {
                @Override
                public void runResultOnUiThread(Long groupId) {
                    Log.d("MainActivityDrawerHome", "Check group id: Group ID: " + groupId);
                    contact.setFirstName(fname);
                    contact.setLastName(lname);
                    contact.setGroupId(groupId);
                    contact.setAddress(address);
                    contact.setBirthday(DateConverter.fromString(birthday));
                    contact.setPhoneNumber(pnumber);
                    intent.putExtra(CONTACT_KEY, contact);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            });
        } else {
            if (contact == null) {
                contact = new Contact(fname, lname, null, pnumber, address, DateConverter.fromString(birthday));
                intent.putExtra(CONTACT_KEY, contact);
                setResult(RESULT_OK, intent);
                finish();
            } else {
                contact.setFirstName(fname);
                contact.setLastName(lname);
                contact.setGroupId(null);
                contact.setAddress(address);
                contact.setPhoneNumber(pnumber);
                contact.setBirthday(DateConverter.fromString(birthday));
                intent.putExtra(CONTACT_KEY, contact);
                setResult(RESULT_OK, intent);
                finish();
            }
        }
    }

    private void populateGroupSpinner() {
        groupService.getAllGroupNames(new Callback<List<String>>() {
            @Override
            public void runResultOnUiThread(List<String> result) {
                if (result != null && !result.isEmpty()) {
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(),
                            android.R.layout.simple_spinner_dropdown_item, result);
                    spnGroup.setAdapter(adapter);
                } else {
                    List<String> defaultList = new ArrayList<>();
                    defaultList.add("No groups yet, please add one");
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(),
                            android.R.layout.simple_spinner_dropdown_item, defaultList);
                    spnGroup.setAdapter(adapter);
                }
            }
        });
    }


}
