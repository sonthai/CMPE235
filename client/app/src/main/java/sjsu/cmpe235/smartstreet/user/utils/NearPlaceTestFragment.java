package sjsu.cmpe235.smartstreet.user.utils;

import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import sjsu.cmpe235.smartstreet.user.R;

public class NearPlaceTestFragment extends Fragment {

    private static final int PLACE_PICKER_REQUEST = 1;
    private TextView name;
    private TextView address;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_nearby, container, false);
        name = (TextView) v.findViewById(R.id.nameView); //
        address = (TextView) v.findViewById(R.id.addressView);
        //Button pickerButton = (Button) v.findViewById(R.id.searchButton);
        //add a listener to the button so that we can handle it
        //pickerButton.setOnClickListener(new View.OnClickListener() {

            //@Override
            //public void onClick(View v) {
                // check for possible exception
                try {
                    // created a intent for place picker
                    PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();

                    Intent intent = intentBuilder.build(getActivity());
                    startActivityForResult(intent, PLACE_PICKER_REQUEST);// Start the intent by requesting a result,

                } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
           // }
        //});

        return  v;
    }

    // A place has been received, use requestCode to track the request
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PLACE_PICKER_REQUEST && resultCode == Activity.RESULT_OK) {      // pass context and intent data in getPlace method
            final Place place = PlacePicker.getPlace(getActivity(), data);
            final CharSequence mName = place.getName();
            final CharSequence mAddress = place.getAddress();


            name.setText(mName);
            address.setText(mAddress);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}

