package test.com.smartstreetuserapp.com.smartstreet.utils;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import test.com.smartstreetuserapp.com.smartstreet.models.Location;
import test.com.smartstreetuserapp.com.smartstreet.models.NearByPlace;


public class AsyncComplex extends AsyncTask<String, Void, Void> {
    public AsyncResponse delegate = null;
    private String rtnVal;
    private ArrayList<NearByPlace> nearByList = new ArrayList<NearByPlace>();

    @Override
    protected Void doInBackground(String... params) {
        String urlString = params[0];
        BufferedReader reader = null;

        try {
            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = "";

            while ((line = reader.readLine()) != null) {
                sb.append(line + " ");
            }
            rtnVal = sb.toString();
        } catch(Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                reader.close();
            } catch (Exception e) {};
        }

        return null;
    }

    protected void onPostExecute(Void unused) {
        NearByPlace nearByPlace = null;
        JSONObject jsonResponse;
        try {
            jsonResponse = new JSONObject(rtnVal);
            JSONArray jsonMainNode = jsonResponse.optJSONArray("results");
            int lengthJsonArr = jsonMainNode.length();
            for(int i = 0; i< lengthJsonArr; i++) {
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                String name = jsonChildNode.getString("name");
                JSONObject locObj = jsonChildNode.getJSONObject("geometry").getJSONObject("location");
                String vicinity = jsonChildNode.getString("vicinity");

                double lat = Double.parseDouble(locObj.getString("lat"));
                double lon = Double.parseDouble(locObj.getString("lng"));
                Location location = new Location(lat, lon);

                nearByPlace = new NearByPlace(location, name, vicinity);;
                nearByList.add(nearByPlace);
            }
            delegate.processFinish(nearByList);

        } catch (Exception e) {};


    }
}
