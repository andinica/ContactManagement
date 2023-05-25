package eu.ase.ro.ContactManagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Date;

import eu.ase.ro.ContactManagement.model.Contact;
import eu.ase.ro.ContactManagement.utils.DateConverter;


public class AddContactActivity extends AppCompatActivity {

    private TextInputEditText tietFname;
    private TextInputEditText tietLname;
    private Spinner spnGroup;
    private TextInputEditText tietPnumber;
    private TextInputEditText tietBday;
    private TextInputEditText tietAddress;
    private Button btnSave;

    private Intent intent;
    private Contact contact = null;



    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        intent = getIntent();
        initComponents();
    }


    private void initComponents() {
        tietFname = findViewById(R.id.add_contact_fname_tiet);
        tietLname = findViewById(R.id.add_contact_lname_tiet);
        spnGroup = findViewById(R.id.add_contact_group_spn);
        tietPnumber = findViewById(R.id.add_contact_pnumber_tiet);
        tietBday = findViewById(R.id.add_contact_bday_tiet);
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
//                    intent.putExtra(EXPENSE_KEY, contact);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        };
    }

    private boolean isValid() {
        if (tietBday.getText() == null || tietBday.getText().toString().trim().isEmpty()
                || DateConverter.fromString(tietBday.getText().toString()) == null) {
            Toast.makeText(getApplicationContext(),
                            R.string.add_contact_date_error_msg,
                            Toast.LENGTH_SHORT)
                    .show();
            return false;
        }
        if (tietFname.getText() == null || tietFname.getText().toString().length() < 3
                || !tietFname.getText().toString().matches("[a-zA-Z]+")) {
            Toast.makeText(getApplicationContext(),
                            R.string.add_contact_text_error_msg,
                            Toast.LENGTH_SHORT)
                    .show();
            return false;
        }
        if (tietLname.getText() == null || tietLname.getText().toString().length() < 3
                || !tietLname.getText().toString().matches("[a-zA-Z]+")) {
            Toast.makeText(getApplicationContext(),
                            R.string.add_contact_text_error_msg,
                            Toast.LENGTH_SHORT)
                    .show();
            return false;
        }
        if (tietAddress.getText() == null || tietAddress.getText().toString().length() < 3
                || !tietAddress.getText().toString().matches("[a-zA-Z]+")) {
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
        Date birthday = DateConverter.fromString(tietBday.getText().toString());
        String group = spnGroup.getSelectedItem().toString();
        String fname = tietFname.getText().toString();
        String lname = tietLname.getText().toString();
        String pnumber = tietPnumber.getText().toString();
        String address = tietAddress.getText().toString();
        if (contact == null) {
            contact = new Contact(fname, lname, group, pnumber, address, birthday);
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
