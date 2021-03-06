package sjsu.cmpe235.smartstreet.admin;


import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import sjsu.cmpe235.smartstreet.admin.models.Sensor;
import sjsu.cmpe235.smartstreet.admin.services.AdminServices;
import sjsu.cmpe235.smartstreet.admin.utils.CustomSensorAdapter;
import sjsu.cmpe235.smartstreet.admin.Constants.Constants;

import sjsu.cmpe235.smartstreet.user.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class SensorMaintenanceFragment extends Fragment {
    ListView sensorListView;
    CustomSensorAdapter adapter;
    ArrayList<Sensor> sensors;
    AdminServices adminServices = new AdminServices();

    public SensorMaintenanceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v =  inflater.inflate(R.layout.fragment_sesnormaint, container, false);
        sensorListView = (ListView) v.findViewById(R.id.sensorListView);
        new SensorAsync().execute(Constants.url + "/sensors/list");

        sensorListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Sensor sensor = (Sensor)  parent.getItemAtPosition(position);
                openSensorDialog(sensor);


            }
        });

        return v;
    }

    private void openSensorDialog(final Sensor s) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.sensor_diaglog);
        dialog.setTitle("Sensor Status Update");
        TextView sId = (TextView) dialog.findViewById(R.id.sensor_id);
        TextView sLocation = (TextView) dialog.findViewById(R.id.sensor_location);
        final EditText sStatus = (EditText) dialog.findViewById(R.id.sensor_status);
        Button sDelete = (Button) dialog.findViewById(R.id.deleteSensor);
        Button sApply = (Button) dialog.findViewById(R.id.applyChange);
        Button sCancel = (Button) dialog.findViewById(R.id.cancelChange);
        sId.setText(s.getSensorId());
        sLocation.setText(s.getLocation());
        sStatus.setText(s.getStatus());

        sCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        sDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = Constants.url + "/sensors/delete/" + s.getSensorId();
                adminServices.delete(url);
                dialog.dismiss();
            }
        });

        sApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!s.getStatus().equalsIgnoreCase(sStatus.getText().toString())) {
                    try {
                        String url = Constants.url + "/sensors/update/" + s.getSensorId();
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("sensor_status", sStatus.getText().toString());
                        adminServices.update(url, String.valueOf(jsonObject));
                        dialog.dismiss();
                    }catch (JSONException jEx) {
                        jEx.printStackTrace();
                    }
                }
            }
        });

        dialog.show();

    }


    private class SensorAsync extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            HttpURLConnection conn = null;
            BufferedReader reader = null;
            String JsonResponse = null;
            URL url;
            sensors = new ArrayList<Sensor>();
            try {
                url = new URL(params[0]);
                conn = (HttpURLConnection) url.openConnection();

                conn.setRequestMethod("GET");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept", "application/json");
                conn.setDoInput(true);
                //System.out.println("inside Async task");
                InputStream inputStream = conn.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String inputLine;
                while ((inputLine = reader.readLine()) != null)
                    buffer.append(inputLine + "\n");
                if (buffer.length() == 0) {
                    // Stream was empty.
                    return null;
                }

                String finalJson = buffer.toString();
                JSONObject parentObject = new JSONObject(finalJson);
                JSONArray parentArray = parentObject.getJSONArray("message");
                for (int i = 0; i < parentArray.length(); i++) {
                    JSONObject finalObject = parentArray.getJSONObject(i);
                    String sensorId = finalObject.getString("sensor_id");
                    String date = Constants.getDateString(finalObject.getString("deployment_date"));
                    String status = finalObject.getString("sensor_status");
                    String type = finalObject.getString("sensor_type");
                    String location = finalObject.getString("sensor_location");
                    Sensor sensor = new Sensor(sensorId, type, date, status, location);
                    sensors.add(sensor);
                }
                return null;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void unused){
            adapter = new CustomSensorAdapter(getActivity().getApplicationContext(), sensors);
            sensorListView.setAdapter(adapter);
        }
    }

}
