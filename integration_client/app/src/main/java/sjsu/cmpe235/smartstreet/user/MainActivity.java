package sjsu.cmpe235.smartstreet.user;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;

public class MainActivity extends AppCompatActivity {
    private static final String USERNAME = "UserName";
    private static String userName = null;
    Toolbar toolbar;
    Button logoutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        userName = intent.getStringExtra(USERNAME);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("SmartStreet");

        logoutBtn = (Button) findViewById(R.id.logoutBtn);
        if (userName != null) {
            logoutBtn.setVisibility(View.VISIBLE);
            logoutBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    userName = null;
                    Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(loginIntent);
                }
            });
        }

        final String[] colors = {"#96CC7A", "#EA705D", "#66BBCC","#ff8080","#8080ff","#df80ff"};

        AHBottomNavigation bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_navigation);

        AHBottomNavigationItem item1 = new AHBottomNavigationItem("Interact", R.drawable.interact, Color.parseColor(colors[0]));
        AHBottomNavigationItem item2 = new AHBottomNavigationItem("About", R.drawable.about, Color.parseColor(colors[1]));
        AHBottomNavigationItem item3 = new AHBottomNavigationItem("Camera", R.drawable.camera, Color.parseColor(colors[2]));
        AHBottomNavigationItem item4 = new AHBottomNavigationItem("Nearby", R.drawable.maps, Color.parseColor(colors[3]));
        AHBottomNavigationItem item5 = new AHBottomNavigationItem("Share", R.drawable.share, Color.parseColor(colors[4]));
        AHBottomNavigationItem item6 = new AHBottomNavigationItem("Comment", R.drawable.comment, Color.parseColor(colors[5]));

        bottomNavigation.addItem(item1);
        bottomNavigation.addItem(item2);
        bottomNavigation.addItem(item3);
        bottomNavigation.addItem(item4);
        bottomNavigation.addItem(item5);
        bottomNavigation.addItem(item6);

        bottomNavigation.setDefaultBackgroundColor(Color.parseColor("#FEFEFE"));

        bottomNavigation.setAccentColor(Color.parseColor("#F63D2B"));
        bottomNavigation.setInactiveColor(Color.parseColor("#747474"));

        //  Enables Reveal effect
        bottomNavigation.setColored(true);

        bottomNavigation.setCurrentItem(0);

        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position, boolean wasSelected) {
                final InteractFragment interactFragment = new InteractFragment();
                final AboutFragment aboutFragment = new AboutFragment();
                final CameraFragment cameraFragment = new CameraFragment();
                final NearbyFragment nearbyFragment = new NearbyFragment();
                final ShareFragment shareFragment = new ShareFragment();
                final CommentFragment commentFragment = new CommentFragment();

                switch(colors[position]){
                    case "#96CC7A":
                        Toast.makeText(getApplicationContext(), "interact", Toast.LENGTH_SHORT).show();
                        getSupportFragmentManager()
                                .beginTransaction()
                                .add(R.id.frame, interactFragment)
                                .addToBackStack(null)
                                .commit();
                        break;
                    case "#EA705D":
                        Toast.makeText(getApplicationContext(),"about", Toast.LENGTH_SHORT).show();
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frame, aboutFragment)
                                .addToBackStack(null)
                                .commit();
                        break;
                    case "#66BBCC":
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frame, cameraFragment)
                                .commit();
                        break;
                    case "#ff8080":
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frame, nearbyFragment)
                                .addToBackStack(null)
                                .commit();
                        break;
                    case "#8080ff":
                        getSupportFragmentManager()
                               .beginTransaction()
                                .replace(R.id.frame, shareFragment)
                                .addToBackStack(null)
                              .commit();
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("content://media/internal/images/media"));
                                startActivity(intent);

                       break;
                    case "#df80ff":
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frame, commentFragment)
                                .addToBackStack(null)
                                .commit();
                }
        }
        });

    }

    public String getCurrentUser() {
        return userName;
    }

}
