package test.com.smartstreetuserapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;

public class SensorRegistration extends AppCompatActivity {
    private static final String TAG = "Log";
    EditText sensorId;
    EditText sensorType;
    EditText sensorLocation;
    EditText sensorStatus;
    Button submitBttn;
    private final String registerUrl = "http://ec2-52-27-135-64.us-west-2.compute.amazonaws.com:3000/sensors/add";
    ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_registration);

        // change id based on xml or vice -versa
        sensorId = (EditText) findViewById(R.id.treeIDText);
        sensorType = (EditText) findViewById(R.id.treeLocationText);
        sensorLocation = (EditText) findViewById(R.id.treeLocationText);
        sensorStatus = (EditText) findViewById(R.id.sensorIDText);

        submitBttn = (Button) findViewById(R.id.registerButton);
        submitBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerSensor(v);

            }

        });

    }

    public void registerSensor(View v) {
        String sensor_id = sensorId.getText().toString();
        String sensor_type = sensorType.getText().toString();
        String sensor_location = sensorLocation.getText().toString();
        String senosr_status = sensorStatus.getText().toString();

        if (sensor_id.equals("") || sensor_type.equals("") || sensor_location.equals("") || sensorStatus.equals("")) {
            Toast.makeText(getApplicationContext(), "Field Required", Toast.LENGTH_LONG).show(); // displaying message
            return;
        } else {

            JSONObject json = new JSONObject();
            try {
                json.put("tree_id", sensor_id);
                json.put("sensor_id", sensor_type);
                json.put("tree_status", sensor_location);
                json.put("tree_location", sensorStatus);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            class JSONTask extends AsyncTask<String, String, String> {
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                }

                @Override
                protected String doInBackground(String... params) {
                    HttpURLConnection conn = null;
                    BufferedReader reader = null;
                    String JsonResponse = null;
                    String JsonData = params[0];
                    URL url;
                    try {
                        url = new URL(registerUrl);
                        conn = (HttpURLConnection) url.openConnection();

                        conn.setRequestMethod("POST");
                        conn.setRequestProperty("Content-Type", "application/json");
                        conn.setRequestProperty("Accept", "application/json");
                        conn.setDoInput(true);
                        conn.setDoOutput(true);
                        Writer writer = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), "UTF-8"));
                        writer.write(JsonData);
                        System.out.println(JsonData);
                        writer.flush();
                        writer.close();
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
                        JsonResponse = buffer.toString();
                        //response data
                        Log.i(TAG, JsonResponse);
                        //send to post execute
                        return JsonResponse;


                    } catch (UnsupportedEncodingException e2) {
                        e2.printStackTrace();
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    } finally {
                        try {
                            reader.close();
                        } catch (Exception ex) {
                        }
                    }


                    return JsonResponse;
                }


                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    {

                        Toast.makeText(getApplicationContext(), "Smart Tree Succesfully Registered  ", Toast.LENGTH_LONG).show();
                    }
                    Intent i = new Intent(getApplicationContext(), AdminHome.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    finish();


                }
            }
            if (json.length() > 0) {
                new JSONTask().execute(String.valueOf(json));
            }

        }
    }

}

