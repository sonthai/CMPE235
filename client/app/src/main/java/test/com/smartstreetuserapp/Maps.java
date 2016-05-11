package test.com.smartstreetuserapp;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Maps extends  AppCompatActivity {


    public class nearbyPlaces extends AppCompatActivity {       // request code
        private static final int PLACE_PICKER_REQUEST = 1;
        private TextView name;
        private TextView address;

        // when activity is started it run onCreate method
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_maps); // set view of map
            name = (TextView) findViewById(R.id.nameView); //
            address = (TextView) findViewById(R.id.addressView);
            Button pickerButton = (Button) findViewById(R.id.searchButton);
            //add a listener to the button so that we can handle it
            pickerButton.setOnClickListener(new View.OnClickListener() {

/*
To use Place Picker,create an intent and listen for the Activity result to retrieve the Place selected by the user.
 */

                @Override
                public void onClick(View v) {
                    // check for possible exception
                    try {
                        // created a intent for place picker
                        PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();

                        Intent intent = intentBuilder.build(nearbyPlaces.this);
                        startActivityForResult(intent, PLACE_PICKER_REQUEST);// Start the intent by requesting a result,

                    } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        // A place has been received, use requestCode to track the request
        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {

            if (requestCode == PLACE_PICKER_REQUEST && resultCode == Activity.RESULT_OK) {      // pass context and intent data in getPlace method
                final Place place = PlacePicker.getPlace(this, data);
                final CharSequence mName = place.getName();
                final CharSequence mAddress = place.getAddress();


                name.setText(mName);
                address.setText(mAddress);
            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
    }
}