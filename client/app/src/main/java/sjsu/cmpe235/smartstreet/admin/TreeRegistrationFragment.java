package sjsu.cmpe235.smartstreet.admin;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import sjsu.cmpe235.smartstreet.user.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class TreeRegistrationFragment extends Fragment {
    private static final String TAG = "Log";
    EditText treeId;
    EditText sensorId;
    EditText treeStatus;
    EditText treeLocation;
    Button submitBttn;
    private final String registerUrl = "http://ec2-52-27-135-64.us-west-2.compute.amazonaws.com:3000/trees/add";


    public TreeRegistrationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_treereg, container, false);

        treeId = (EditText) v.findViewById(R.id.treeId);
        sensorId = (EditText) v.findViewById(R.id.sensorId);
        treeStatus = (EditText) v.findViewById(R.id.treeStatus);
        treeLocation = (EditText) v.findViewById(R.id.treeLocation);
        submitBttn = (Button) v.findViewById(R.id.submit);
        submitBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerTree(v);

            }

        });

        // Inflate the layout for this fragment
        return v;
    }
    public void registerTree(View v) {
        String tree_id = treeId.getText().toString();
        String sensor_id= sensorId.getText().toString();
        String tree_status= treeStatus.getText().toString();
        String tree_location= treeLocation.getText().toString();

        if (tree_id.equals("") || sensor_id.equals("") || tree_status.equals("") || tree_location.equals("")) {
            Toast.makeText(getActivity(), "Field Required", Toast.LENGTH_LONG).show(); // displaying message
            return;
        } else {

            JSONObject json = new JSONObject();
            try {
                json.put("tree_id",tree_id);
                json.put("sensor_id",sensor_id);
                json.put("tree_status",tree_status);
                json.put("tree_location",tree_location);

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
                            buffer.append(inputLine +"\n");
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
                    }
                    finally {
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

                        Toast.makeText(getActivity(), "Smart Tree Succesfully Registered  "  , Toast.LENGTH_LONG).show();
                    }
                    Intent i = new Intent(getActivity(), AdminActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);



                }
            }
            if (json.length() > 0) {
                new JSONTask().execute(String.valueOf(json));
            }

        }
    }

}
