package sjsu.cmpe235.smartstreet.user.services;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import sjsu.cmpe235.smartstreet.user.NearbyFragment;
import sjsu.cmpe235.smartstreet.user.model.Direction;


public class DirectionServices {
    public void searchLocation(NearbyFragment nearbyFragment, String url) {
        AsyncSearchLocation  asyncDirection = new AsyncSearchLocation();
        asyncDirection.delegate  = nearbyFragment;
        asyncDirection.execute(url);
    }

    public void showDirection(NearbyFragment nearbyFragment, String url) {
        AsyncShowDirection asyncShowDirection = new AsyncShowDirection();
        asyncShowDirection.delegate = nearbyFragment;
        asyncShowDirection.execute(url);
    }

    private class AsyncSearchLocation extends AsyncTask<String, Void, Void> {
        private NearbyFragment delegate = null;
        private String rtnVal;
        private ArrayList<Direction> directions;

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
                } catch (Exception e) {}
            }

            return null;
        }

        protected void onPostExecute(Void unused) {
            JSONObject jsonResponse;
            directions = new ArrayList<Direction>();
            try {
                jsonResponse = new JSONObject(rtnVal);
                String status = jsonResponse.getString("status");
                if (status.equals("OK")) {
                    JSONArray routeArr = jsonResponse.getJSONArray("routes");
                    JSONObject routeObj = (JSONObject) routeArr.get(0);
                    JSONArray legsArr = (JSONArray) routeObj.get("legs");
                    JSONArray stepsArr = ((JSONObject) legsArr.get(0)).getJSONArray("steps");
                    for (int i = 0; i < stepsArr.length(); i++) {
                        JSONObject stepObj = (JSONObject) stepsArr.get(i);
                        String distance = ((JSONObject) stepObj.get("distance")).get("text").toString();
                        String duration = ((JSONObject) stepObj.get("duration")).get("text").toString();
                        String instruction = stepObj.getString("html_instructions");
                        Direction direction = new Direction(duration, instruction, distance);
                        directions.add(direction);
                    }
                }
                delegate.processDirectionResult(directions);
            } catch (Exception e) {}
        }
    }

    private class AsyncShowDirection extends AsyncTask<String, Void, Void> {
        public NearbyFragment delegate = null;
        private String rtnVal;
        private ArrayList<Direction> directions;

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
                } catch (Exception e) {}
            }

            return null;
        }

        protected void onPostExecute(Void unused) {
            JSONObject jsonResponse;
            directions = new ArrayList<Direction>();
            try {
                jsonResponse = new JSONObject(rtnVal);
                String status = jsonResponse.getString("status");
                if (status.equals("OK")) {
                    JSONArray routeArr = jsonResponse.getJSONArray("routes");
                    JSONObject routeObj = (JSONObject) routeArr.get(0);
                    JSONArray legsArr = (JSONArray) routeObj.get("legs");
                    JSONArray stepsArr = ((JSONObject) legsArr.get(0)).getJSONArray("steps");
                    for (int i = 0; i < stepsArr.length(); i++) {
                        JSONObject stepObj = (JSONObject) stepsArr.get(i);
                        String distance = ((JSONObject) stepObj.get("distance")).get("text").toString();
                        String duration = ((JSONObject) stepObj.get("duration")).get("text").toString();
                        String instruction = stepObj.getString("html_instructions");
                        Direction direction = new Direction(duration, instruction, distance);
                        directions.add(direction);
                    }
                }
                delegate.processDirectionResult(directions);
            } catch (Exception e) {}
        }
    }


}
