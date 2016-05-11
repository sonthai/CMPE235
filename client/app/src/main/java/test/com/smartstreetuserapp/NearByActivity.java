package test.com.smartstreetuserapp;

import android.Manifest;
import android.support.v7.app.AppCompatActivity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;


import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


import test.com.smartstreetuserapp.com.smartstreet.models.Direction;
import test.com.smartstreetuserapp.com.smartstreet.models.NearByPlace;
import test.com.smartstreetuserapp.com.smartstreet.utils.AsyncComplex;
import test.com.smartstreetuserapp.com.smartstreet.utils.AsyncDirectionTask;
import test.com.smartstreetuserapp.com.smartstreet.utils.AsyncResponse;
import test.com.smartstreetuserapp.com.smartstreet.utils.ListAdapter;


public class NearByActivity extends AppCompatActivity implements AsyncResponse {
    final String CURRENT_LOCATION = "Current Location";
    private LocationManager locationManager;
    private LocationListener locationLister;
    private GoogleMap map;
    private Location currLocation;
    private Map<Marker, NearByPlace> markerMap;
    private Marker lastMarker = null;
    AsyncComplex asyncComplex = null;
    AsyncDirectionTask asyncDirectionTask = null;
    ListView listView = null;
    int mapLayoutHeight = 0;
    int listLayoutHeight = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby);
        final EditText editText = (EditText) findViewById(R.id.location_search_box);
        editText.setOnFocusChangeListener(new View. OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(editText.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        });


        // Get GPS Location Service LocationManager object
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        locationLister = new MyLocationListener();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationLister);

        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

        // Retrieve the height of maplayout when it's first created
        final LinearLayout maplayout = (LinearLayout) findViewById(R.id.mapLayout);
        ViewTreeObserver viewTree = maplayout.getViewTreeObserver();
        viewTree.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                mapLayoutHeight = maplayout.getMeasuredHeight();
                return true;
            }
        });

    }

    public void searchLocation(View v) {
        asyncComplex = new AsyncComplex();
        asyncComplex.delegate = this;
        EditText locationText = (EditText) findViewById(R.id.location_search_box);
        if (locationText.getText().toString().trim().length() != 0) {
            String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";
            url += "location=" + currLocation.getLatitude() + "," + currLocation.getLongitude();
            url += "&key=AIzaSyD1GuYKN2gIJrHEYp08Bjydf9nBXhASB80";
            url += "&keyword=" + locationText.getText() + "&rankby=distance";
            asyncComplex.execute(url);
        }
    }

    public void showDirection(View v) {
        EditText from = (EditText) findViewById(R.id.location_search_box);
        EditText to = (EditText) findViewById(R.id.to_place);
        String origin = null;
        String destination = null;

        if (from.getText().toString().trim().length() > 0 &&
                to.getText().toString().trim().length() > 0) {
            if (from.getText().toString().equals(CURRENT_LOCATION)) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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

            asyncDirectionTask = new AsyncDirectionTask();
            asyncDirectionTask.delegate = this;
            asyncDirectionTask.execute(url);
        }
    }

    private String convertLonLatToAddress(Location location) {
        List<Address> addresses = null;
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
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
        markerMap = new LinkedHashMap<>();
        map.clear();

        for (final NearByPlace place : nearByList) {
            MarkerOptions mp = new MarkerOptions();
            mp.position(new LatLng(place.getLocation().getLat(), place.getLocation().getLon()));
            mp.title(place.getName());
            mp.snippet(place.getVicinity());

            Marker marker = map.addMarker(mp);
            markerMap.put(marker, place);
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(place.getLocation().getLat(), place.getLocation().getLon()), 16));

            map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    LinearLayout linlay = (LinearLayout) findViewById(R.id.direction);
                    EditText from = (EditText) findViewById(R.id.location_search_box);
                    EditText to = (EditText) findViewById(R.id.to_place);
                    if (!markerMap.get(marker).getTap()) {
                        linlay.setVisibility(View.VISIBLE);
                        from.setText(CURRENT_LOCATION);
                        to.setText(markerMap.get(marker).getVicinity());
                        markerMap.get(marker).setTap(true);
                        if (lastMarker != null && !lastMarker.equals(marker)) {
                            markerMap.get(lastMarker).setTap(false);
                        }
                        lastMarker = marker;
                    } else {
                        linlay.setVisibility(View.GONE);
                        from.setText("");
                        to.setText("");
                        markerMap.get(marker).setTap(false);
                    }

                    // If user untap the searching location, hide listView if it's already displayed
                    // and restore the height of maplayout
                    LinearLayout listLayout = (LinearLayout) findViewById(R.id.listViewLayout);
                    if (listLayout.getVisibility() == View.VISIBLE) {
                        // Hide the listView
                        listLayout.setVisibility(View.GONE);
                        // Restore  height for mapLayout
                        LinearLayout mapLayout = (LinearLayout) findViewById(R.id.mapLayout);
                        ViewGroup.LayoutParams mapParams = mapLayout.getLayoutParams();
                        mapParams.height = mapParams.MATCH_PARENT;
                    }

                    return true;
                }
            });

        }

    }

    @Override
    public void processDirectionResult(ArrayList<Direction> directions) {
        // Set the height for listViewLayout
        LinearLayout listLayout = (LinearLayout) findViewById(R.id.listViewLayout);
        listLayoutHeight =  mapLayoutHeight/4;

        // Adjust the height for mapLayout
        int newMapLayoutHeight = mapLayoutHeight - listLayoutHeight;
        LinearLayout mapLayout = (LinearLayout) findViewById(R.id.mapLayout);
        ViewGroup.LayoutParams mapParams = mapLayout.getLayoutParams();
        mapParams.height = newMapLayoutHeight;

        // populate data into listView
        listView = (ListView) findViewById(R.id.listView);
        ListAdapter adapter = new ListAdapter(this, directions);
        listView.setAdapter(adapter);
        listLayout.setVisibility(View.VISIBLE);
    }

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

                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }

                locationManager.removeUpdates(locationLister);
                locationManager = null;
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
