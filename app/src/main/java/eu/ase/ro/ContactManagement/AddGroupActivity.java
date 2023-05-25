package eu.ase.ro.ContactManagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Date;

import eu.ase.ro.ContactManagement.model.Contact;
import eu.ase.ro.ContactManagement.model.Group;
import eu.ase.ro.ContactManagement.utils.DateConverter;

public class AddGroupActivity extends AppCompatActivity {


    private Intent intent;
    private Group group = null;
    private Button btnSave;

    TextInputEditText tietGroupName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group);
        intent = getIntent();
        initComponents();
    }

    public void initComponents() {
        tietGroupName = findViewById(R.id.add_group_name_tiet);
        btnSave = findViewById(R.id.add_group_save_btn);
        btnSave.setOnClickListener(saveGroupEventListener());
    }

    private View.OnClickListener saveGroupEventListener() {
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

}
