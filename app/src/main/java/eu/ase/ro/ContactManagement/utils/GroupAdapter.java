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
import eu.ase.ro.ContactManagement.model.Group;

public class GroupAdapter extends ArrayAdapter<Group> {
    private Context context;
    private List<Group> groups;
    private int resource;
    private LayoutInflater inflater;

    public GroupAdapter(@NonNull Context context, int resource, @NonNull List<Group> objects, LayoutInflater inflater) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.groups = objects;
        this.inflater = inflater;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = inflater.inflate(resource, parent, false);
        Group group = groups.get(position);
        if (group != null) {
            TextView tv_name = view.findViewById(R.id.tv_lv_group_name);
            addTextViewContent(tv_name, group.getName());
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
