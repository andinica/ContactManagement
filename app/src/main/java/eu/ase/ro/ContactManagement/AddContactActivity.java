package eu.ase.ro.ContactManagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;
import java.util.Date;

import eu.ase.ro.ContactManagement.async.Callback;
import eu.ase.ro.ContactManagement.db.ContactService;
import eu.ase.ro.ContactManagement.model.Contact;
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

    private Intent intent;
    private Contact contact = null;

    private ContactService contactService;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        intent = getIntent();

        initComponents();
        if (intent.hasExtra(CONTACT_KEY)) {
            contact = (Contact) intent.getSerializableExtra(CONTACT_KEY);
            createViewFromObject();
        }
    }

    private void createViewFromObject() {
        tietFname.setText(contact.getFirstName());
        tietAddress.setText(contact.getAddress());
        tietLname.setText(contact.getLastName());
        tietPnumber.setText(contact.getPhoneNumber());


        ArrayAdapter<CharSequence> adapter = (ArrayAdapter<CharSequence>) spnGroup.getAdapter();
        for (int i = 0; i < adapter.getCount(); i++) {
            String item = adapter.getItem(i).toString();
            if (item.equals(contact.getGroup())) {
                spnGroup.setSelection(i);
            }
        }
    }
    private void initComponents() {
        tietFname = findViewById(R.id.add_contact_fname_tiet);
        tietLname = findViewById(R.id.add_contact_lname_tiet);
        spnGroup = findViewById(R.id.add_contact_group_spn);
        tietPnumber = findViewById(R.id.add_contact_pnumber_tiet);
        dpBday = findViewById(R.id.add_contact_bday_dp);
        tietAddress = findViewById(R.id.add_contact_address_tiet);
        btnSave = findViewById(R.id.add_contact_save_btn);
        btnSave.setOnClickListener(saveContactEventListener());
    }

//    private Callback<Contact> getInsertCallback() {
//        return new Callback<Contact>() {
//            @Override
//            public void runResultOnUiThread(Contact result) {
//                //aici suntem cu notificarea din baza de date
//                contacts.add(result);
//                notifyAdapter();
//            }
//        };
//    }
    private View.OnClickListener saveContactEventListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValid()) {
                    createFromViews();
                    intent.putExtra(CONTACT_KEY, contact);
                    setResult(RESULT_OK, intent);
                    finish();
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

//    private void populateFields() {
//        tietFname.setText("test");
//        tietLname.setText("TEST");
//        spnGroup.setSelection(0); // selects the first item in the spinner
//        tietPnumber.setText("1234567890");
//        tietAddress.setText("Test Street");
//    }

    private void createFromViews() {
        int day = dpBday.getDayOfMonth();
        int month = dpBday.getMonth();
        int year =  dpBday.getYear();

        String birthday = day + "/" + month + "/" + year;

        String group = spnGroup.getSelectedItem().toString();
        String fname = tietFname.getText().toString();
        String lname = tietLname.getText().toString();
        String pnumber = tietPnumber.getText().toString();
        String address = tietAddress.getText().toString();
        if (contact == null) {
            contact = new Contact(fname, lname, group, pnumber, address, DateConverter.fromString(birthday));
        } else {
            contact.setFirstName(fname);
            contact.setLastName(lname);
            contact.setGroup(fname);
            contact.setAddress(fname);
            contact.setFirstName(fname);
            contact.setFirstName(fname);
        }
    }

}
