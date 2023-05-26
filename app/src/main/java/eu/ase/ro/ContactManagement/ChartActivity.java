package eu.ase.ro.ContactManagement;

import android.content.Intent;
import android.os.Bundle;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Column;
import com.anychart.enums.Anchor;
import com.anychart.enums.Position;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import eu.ase.ro.ContactManagement.async.Callback;
import eu.ase.ro.ContactManagement.db.ContactService;
import eu.ase.ro.ContactManagement.db.GroupService;
import eu.ase.ro.ContactManagement.model.Contact;
import eu.ase.ro.ContactManagement.model.Group;

public class ChartActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private RelativeLayout chartContainer;
    private List<Group> groups = new ArrayList<>();
    private List<Long> appearances = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        configNavigation();
        chartContainer = findViewById(R.id.any_chart_view);

        // Fetch data from ContactService and GroupService
        ContactService contactService = new ContactService(getApplicationContext());
        GroupService groupService = new GroupService(getApplicationContext());

        groupService.getAll(new Callback<List<Group>>() {
            @Override
            public void runResultOnUiThread(List<Group> result) {
                if (result != null) {
                    groups.clear();
                    groups.addAll(result);
                    for(Group group : groups ){
                        contactService.getGroupCountForEach(group.getId(), new Callback<Long>(){
                            @Override
                            public void runResultOnUiThread(Long result) {
                                if (result != null){
                                    appearances.add(result);
                                }
                            }
                        });
                    }
                    createBarChart(groups, appearances);
                }
            }
        });

        navigationView = findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_chart);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.nav_contact) {//ifs are redundant here (if logs are removed)
                    //pressed on contacts
                    navigationView.setCheckedItem(R.id.nav_contact);
                    Log.i("MainActivityDrawerHome", "Pressed contacts");
                    Toast.makeText(getApplicationContext(),
                            getString(R.string.show_option, item.getTitle()),
                            Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(ChartActivity.this, ContactActivity.class);
                    startActivity(intent);
                    finish();
                }
                if (item.getItemId() == R.id.nav_group) {
                    //pressed on groups
                    navigationView.setCheckedItem(R.id.nav_group);
                    Log.i("MainActivityDrawerHome", "Pressed groups");
                    Toast.makeText(getApplicationContext(),
                            getString(R.string.show_option, item.getTitle()),
                            Toast.LENGTH_LONG).show();
//                    currentFragment = HomeFragment.getInstance();
                    Intent intent = new Intent(ChartActivity.this, GroupActivity.class);
                    startActivity(intent);
                    finish();
                }
                if (item.getItemId() == R.id.nav_exit) {
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

    private void configNavigation() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    private void createBarChart(List<Group> groups, List<Long> appearances) {
        // Initialize AnyChart library
        AnyChartView anyChartView = new AnyChartView(this);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT
        );
        anyChartView.setLayoutParams(layoutParams);

        chartContainer.addView(anyChartView);

        // Create a bar chart
        Cartesian cartesian = AnyChart.column();

        // Set data
        List<DataEntry> dataEntries = new ArrayList<>();
        int i = 0;
        for (Group group : groups) {
            dataEntries.add(new ValueDataEntry(group.getName(),appearances.get(i++)));
            Column column = cartesian.column(dataEntries);
            column.tooltip()
                    .titleFormat("{%X}")
                    .position(Position.CENTER_BOTTOM)
                    .anchor(Anchor.CENTER_BOTTOM)
                    .offsetX(0d)
                    .offsetY(5d)
                    .format("{%Value}{groupsSeparator: }");

            cartesian.title(group.getName() + " - Number of Members");
            cartesian.yAxis(0).title("Members");
            cartesian.xAxis(0).title("Group");

            anyChartView.setChart(cartesian);
        }
    }


}