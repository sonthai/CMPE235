package sjsu.cmpe235.smartstreet.user;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.net.URLEncoder;
import java.nio.BufferUnderflowException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import sjsu.cmpe235.smartstreet.user.model.Direction;
import sjsu.cmpe235.smartstreet.user.model.NearByPlace;
import sjsu.cmpe235.smartstreet.user.services.DirectionServices;
import sjsu.cmpe235.smartstreet.user.utils.CustomDirectionAdapter;

public class NearbyFragment extends Fragment implements NearByInterface {
    final String CURRENT_LOCATION = "Current Location";
    private LocationManager locationManager;
    private LocationListener locationLister;
    private GoogleMap map;
    private Location currLocation;
    private Map<Marker, NearByPlace> markerMap;
    private Marker lastMarker = null;
    ListView listView = null;
    int mapLayoutHeight = 0;
    int listLayoutHeight = 0;
    DirectionServices directionServices = new DirectionServices();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.nearby_fragment, container, false);

        final EditText editText = (EditText) v.findViewById(R.id.location_search_box);
        editText.setOnFocusChangeListener(new View. OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(editText.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        });

        Button searchLocationBtn = (Button) v.findViewById(R.id.searchLocationBtn);
        searchLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currLocation == null) {
                    showDirection();
                } else {
                    searchLocation();
                }
            }
        });


        // Get GPS Location Service LocationManager object
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(getActivity(), "GPS is enabled in your device", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "GPS is not enabled in your device", Toast.LENGTH_SHORT).show();
        }

        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            Toast.makeText(getActivity(), "Network is available", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "GPS is not available", Toast.LENGTH_SHORT).show();
        }

        locationLister = new MyLocationListener();

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return v;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationLister);
        Location l = null;
        if (locationManager != null) {
            l = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (l != null) {
                Log.i("Long ", String.valueOf(l.getLongitude()));
                Log.i("Lat ", String.valueOf(l.getLatitude()));
            } else {
                Log.e("Location error", "Failed to detect current location");
            }
        }

        map = ((MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.map)).getMap();

        // Retrieve the height of maplayout when it's first created
        final LinearLayout maplayout = (LinearLayout) v.findViewById(R.id.mapLayout);
        ViewTreeObserver viewTree = maplayout.getViewTreeObserver();
        viewTree.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                mapLayoutHeight = maplayout.getMeasuredHeight();
                return true;
            }
        });

        return v;
    }

    public void searchLocation() {
        EditText locationText = (EditText) getActivity().findViewById(R.id.location_search_box);
        if (locationText.getText().toString().trim().length() != 0) {
            String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";
            url += "location=" + currLocation.getLatitude() + "," + currLocation.getLongitude();
            url += "&key=AIzaSyD1GuYKN2gIJrHEYp08Bjydf9nBXhASB80";
            url += "&keyword=" + locationText.getText() + "&rankby=distance";
            directionServices.searchLocation(this, url);
        }
    }

    public void showDirection() {
        EditText from = (EditText) getActivity().findViewById(R.id.location_search_box);
        EditText to = (EditText) getActivity().findViewById(R.id.to_place);
        String origin = null;
        String destination = null;

        if (from.getText().toString().trim().length() > 0 &&
                to.getText().toString().trim().length() > 0) {
            if (from.getText().toString().equals(CURRENT_LOCATION)) {
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }

                origin = convertLonLatToAddress(currLocation);
            } else {
                origin = from.getText().toString();
            }
            destination = to.getText().toString();

            try {
                origin = URLEncoder.encode(origin, "UTF-8");
                destination = URLEncoder.encode(destination, "UTF-8");
            } catch (Exception e) {}

            String url = "https://maps.googleapis.com/maps/api/directions/json?";
            url += "origin=" + origin + "&destination=" + destination;
            url += "&mode=driving&key=AIzaSyBzyk2nvF56Mx6mJ34Nq_nWnvtzgpXCpYA";


            directionServices.showDirection(this, url);
        }
    }

    private String convertLonLatToAddress(Location location) {
        List<Address> addresses = null;
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
        } catch (Exception e) {}

        String addStr = "";
        if (addresses != null ) {
            addStr = addresses.get(0).getAddressLine(0) + "," + addresses.get(0).getLocality() +
                    "," + addresses.get(0).getAdminArea() + "," + addresses.get(0).getCountryName();
        }
        return  addStr;
    }

    @Override
    public void processFinish(ArrayList<NearByPlace> nearByList) {

    }

    @Override
    public void processDirectionResult(ArrayList<Direction> directions) {
        View v = getView();
        // Set the height for listViewLayout
        LinearLayout listLayout = (LinearLayout) v.findViewById(R.id.listViewLayout);
        listLayoutHeight =  mapLayoutHeight/4;

        // Adjust the height for mapLayout
        int newMapLayoutHeight = mapLayoutHeight - listLayoutHeight;
        LinearLayout mapLayout = (LinearLayout) v.findViewById(R.id.mapLayout);
        ViewGroup.LayoutParams mapParams = mapLayout.getLayoutParams();
        mapParams.height = newMapLayoutHeight;

        // populate data into listView
        listView = (ListView) v.findViewById(R.id.listView);
        CustomDirectionAdapter adapter = new CustomDirectionAdapter(getActivity(), directions);
        listView.setAdapter(adapter);
        listLayout.setVisibility(View.VISIBLE);


    }


    // Helper class
    private class MyLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                map.clear();

                MarkerOptions mp = new MarkerOptions();
                mp.position(new LatLng(location.getLatitude(), location.getLongitude()));
                mp.title("You are here");

                map.addMarker(mp);

                map.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(location.getLatitude(), location.getLongitude()), 16));
                currLocation = location;

                if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }

                //locationManager.removeUpdates(locationLister);
                //locationManager = null;
            } else {
                Toast.makeText(getActivity(), "Location is null", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }

}
