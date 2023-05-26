package eu.ase.ro.ContactManagement.utils;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import eu.ase.ro.ContactManagement.R;
import eu.ase.ro.ContactManagement.model.Contact;

public class MemberAdapter extends ArrayAdapter<Contact> {
    private Context context;
    private List<Contact> contacts;
    private int resource;
    private LayoutInflater inflater;

    public MemberAdapter(@NonNull Context context, int resource, @NonNull List<Contact> objects, LayoutInflater inflater) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.contacts = objects;
        this.inflater = inflater;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Log.d("MainActivityDrawerHome", "getView: Position: " + position);
        View view = inflater.inflate(resource, parent, false);
        Contact contact = contacts.get(position);
        if (contact != null) {
            TextView tv_name = view.findViewById(R.id.tv_lv_member_name);
            String name = contact.getFirstName() + " " + contact.getLastName();
            addTextViewContent(tv_name, name);
        }
        return view;
    }

    private void addTextViewContent(TextView textView, String value) {
        if (value != null && !value.isEmpty()) {
            textView.setText(value);
        } else {
            textView.setText(R.string.lv_contact_value);
        }
    }
}