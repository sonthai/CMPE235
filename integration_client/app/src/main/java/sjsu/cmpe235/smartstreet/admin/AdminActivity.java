package sjsu.cmpe235.smartstreet.admin;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;

import sjsu.cmpe235.smartstreet.user.R;

/**
 * Created by Son Thai on 5/4/2016.
 */
public class AdminActivity extends AppCompatActivity {
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);



        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("SmartStreet");

        final String[] colors = {"#96CC7A", "#EA705D", "#66BBCC","#ff8080"};

        AHBottomNavigation bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_navigation);

        AHBottomNavigationItem item1 = new AHBottomNavigationItem("Tree", R.drawable.treeicon, Color.parseColor(colors[0]));
        AHBottomNavigationItem item2 = new AHBottomNavigationItem("Maintenance", R.drawable.treem, Color.parseColor(colors[1]));
        AHBottomNavigationItem item3 = new AHBottomNavigationItem("Sensor", R.drawable.sensor, Color.parseColor(colors[2]));
        AHBottomNavigationItem item4 = new AHBottomNavigationItem("Setting", R.drawable.sensorm, Color.parseColor(colors[3]));

        bottomNavigation.addItem(item1);
        bottomNavigation.addItem(item2);
        bottomNavigation.addItem(item3);
        bottomNavigation.addItem(item4);


        bottomNavigation.setDefaultBackgroundColor(Color.parseColor("#FEFEFE"));

        bottomNavigation.setAccentColor(Color.parseColor("#F63D2B"));
        bottomNavigation.setInactiveColor(Color.parseColor("#747474"));

        //  Enables Reveal effect
        bottomNavigation.setColored(true);

        bottomNavigation.setCurrentItem(0);

        final TreeRegistrationFragment treeRegistrationFragment = new TreeRegistrationFragment();
        final TreeMaintenanceFragment treeMaintenanceFragment = new TreeMaintenanceFragment();
        final SensorRegistrationFragment sensorRegistrationFragment = new SensorRegistrationFragment();
        final SensorMaintenanceFragment sensorMaintenanceFragment = new SensorMaintenanceFragment();

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.frame, treeRegistrationFragment)
                .addToBackStack(null)
                .commit();
        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position, boolean wasSelected) {
               switch(colors[position]){
                    case "#96CC7A":
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frame, treeRegistrationFragment)
                                .addToBackStack(null)
                                .commit();
                        break;
                    case "#EA705D":
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frame, treeMaintenanceFragment)
                                .addToBackStack(null)
                                .commit();
                        break;
                   case "#66BBCC":
                       getSupportFragmentManager()
                               .beginTransaction()
                               .replace(R.id.frame, sensorRegistrationFragment)
                               .addToBackStack(null)
                               .commit();
                       break;
                   case "#ff8080":
                       getSupportFragmentManager()
                               .beginTransaction()
                               .replace(R.id.frame, sensorMaintenanceFragment)
                               .addToBackStack(null)
                               .commit();
                       break;
                }
            }
        });

    }
}
