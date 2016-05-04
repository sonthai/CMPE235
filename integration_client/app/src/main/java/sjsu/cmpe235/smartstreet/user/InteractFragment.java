package sjsu.cmpe235.smartstreet.user;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import sjsu.cmpe235.smartstreet.user.Constant.Constants;
import sjsu.cmpe235.smartstreet.user.utils.CommentAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class InteractFragment extends Fragment {
    private static final String TAG ="Interact" ;
    private String LIGHTON = "Light on";
    private String LIGHTOFF = "Light off";
    private String SOUNDON = "Sound on";
    private String SOUNDOFF = "Sound off";

    RadioGroup radiogrpAction;
    RadioGroup radiogrpColor;
    RadioButton radioaActionLight;
    RadioButton radiocolorGreen;
    RadioButton radiocolorBlue;
    RadioButton radioActionSound;
    EditText treeId = null;
    Button offBttn;
    Button onBttn;
    String userName = null;


    public InteractFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_interact, container, false);
        View v = inflater.inflate(R.layout.fragment_interact,container, false);
        radioaActionLight = (RadioButton) v.findViewById(R.id.lightButton);
        radiocolorGreen = (RadioButton) v.findViewById(R.id.greenButton);
        radiocolorBlue = (RadioButton) v.findViewById(R.id.blueButton);
        radiogrpAction = (RadioGroup) v.findViewById(R.id.radioGrpAction);
        radioActionSound = (RadioButton) v.findViewById(R.id.soundButton);
        //ledo
        radiogrpColor = (RadioGroup) v.findViewById(R.id.radioGrpColor);//led1
        offBttn = (Button) v.findViewById(R.id.offButton);
        onBttn = (Button) v.findViewById(R.id.onButton);
        userName = ((MainActivity) getActivity()).getCurrentUser();
        treeId = (EditText) v.findViewById(R.id.treeId);

        radioActionSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radiogrpColor.clearCheck();
                radiocolorGreen.setVisibility(View.INVISIBLE);
                radiocolorBlue.setVisibility(View.INVISIBLE);
            }
        });

        radioaActionLight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radiocolorGreen.setVisibility(View.VISIBLE);
                radiocolorBlue.setVisibility(View.VISIBLE);
            }
        });


        offBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(Interact.this, "OFF", Toast.LENGTH_SHORT).show();
                String action = LIGHTOFF;
                int selectId = radiogrpColor.getCheckedRadioButtonId();
                if(selectId==radiocolorBlue.getId())
                    new SparkInteract().execute("D7,off");
                else
                if(selectId==radiocolorGreen.getId())
                    new SparkInteract().execute("D0,off");
                int selectActionSound = radiogrpAction.getCheckedRadioButtonId();
                if(selectActionSound==radioActionSound.getId()){
                    action = SOUNDOFF;
                    new SparkSoundInteract().execute("off");
                }
                recordActivity(action);
            }
        });

        onBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(Interact.this, "OFF", Toast.LENGTH_SHORT).show();
                String action = LIGHTON;
                int selectId = radiogrpColor.getCheckedRadioButtonId();
                if(selectId==radiocolorBlue.getId())
                    new SparkInteract().execute("D7,on");
                else
                if(selectId==radiocolorGreen.getId())
                    new SparkInteract().execute("D0,on");
                int selectActionSound = radiogrpAction.getCheckedRadioButtonId();
                if(selectActionSound==radioActionSound.getId()){
                    action = SOUNDON;
                    new SparkSoundInteract().execute("on");
                    radiogrpColor.setVisibility(View.INVISIBLE);
                }
                recordActivity(action);

            }
        });


        return v;
    }

    public void recordActivity(String action) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("activity", action);
            jsonObject.put("userName", userName);
            jsonObject.put("tree_id", treeId.getText().toString());
            String url = Constants.url + "/activity/record";
            new RecordUserActivityAsync().execute(url, String.valueOf(jsonObject));
        } catch (JSONException jEx) {
            jEx.printStackTrace();
        }
    }


    class SparkInteract extends AsyncTask<String, Void, String> {
        public String doInBackground(String... IO) {

            // Predefine variables
            String action = new String(IO[0]);
            URL url;

            try {
                // variables
                url = new URL("https://api.spark.io/v1/devices/55ff73066678505545421367/led/");
                String param = "access_token=5d6046be7ee3398b12ad3068a14cdb53d456084e&params=" + action;
                Log.d(TAG, "param:" + param);

                // Open a connection using HttpURLConnection
                HttpsURLConnection con = (HttpsURLConnection) url.openConnection();

                con.setReadTimeout(4000);
                con.setConnectTimeout(4000);
                con.setDoOutput(true);
                con.setDoInput(true);
                con.setInstanceFollowRedirects(false);
                con.setRequestMethod("POST");
                con.setFixedLengthStreamingMode(param.getBytes().length);
                con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                // Send
                PrintWriter out = new PrintWriter(con.getOutputStream());
                out.print(param);
                out.close();

                con.connect();

                BufferedReader in = null;
                if (con.getResponseCode() != 200) {
                    in = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                    //Log.d(TAG, "!=200: " + in);
                } else {
                    in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    Log.d(TAG, "POST request send successful: " + in);
                }
                ;


            } catch (Exception e) {
                Log.d(TAG, "Exception");
                e.printStackTrace();
                return null;
            }
            // Set null
            return null;
        }

    }

    class SparkSoundInteract extends AsyncTask<String, Void, String> {
        public String doInBackground(String... IO) {

            // Predefine variables
            String action = new String(IO[0]);
            URL url;

            try {
                // variables
                url = new URL("https://api.spark.io/v1/devices/55ff73066678505545421367/sound/");
                String param = "access_token=5d6046be7ee3398b12ad3068a14cdb53d456084e&params=" + action;
                Log.d(TAG, "param:" + param);

                // Open a connection using HttpURLConnection
                HttpsURLConnection con = (HttpsURLConnection) url.openConnection();

                con.setReadTimeout(4000);
                con.setConnectTimeout(4000);
                con.setDoOutput(true);
                con.setDoInput(true);
                con.setInstanceFollowRedirects(false);
                con.setRequestMethod("POST");
                con.setFixedLengthStreamingMode(param.getBytes().length);
                con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                // Send
                PrintWriter out = new PrintWriter(con.getOutputStream());
                out.print(param);
                out.close();

                con.connect();

                BufferedReader in = null;
                if (con.getResponseCode() != 200) {
                    in = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                    //Log.d(TAG, "!=200: " + in);
                } else {
                    in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    Log.d(TAG, "POST request send successful: " + in);
                }
                ;


            } catch (Exception e) {
                Log.d(TAG, "Exception");
                e.printStackTrace();
                return null;
            }
            // Set null
            return null;
        }

    }

    class RecordUserActivityAsync extends AsyncTask<String, String, Void> {

        @Override
        protected Void doInBackground(String... params) {
            HttpURLConnection conn = null;
            BufferedReader reader = null;
            String JsonResponse = null;

            URL url;
            try {
                url = new URL(params[0]);
                String JsonData = params[1];
                conn = (HttpURLConnection) url.openConnection();

                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept", "application/json");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                Writer writer = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), "UTF-8"));
                writer.write(JsonData);
                //System.out.println(JsonData);
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
                return null;


            } catch (UnsupportedEncodingException e2) {
                e2.printStackTrace();
            } catch (IOException e2) {
                e2.printStackTrace();
            } finally {
                try {
                    reader.close();
                } catch (Exception ex) {
                    ex.printStackTrace();

                }
            }

            return null;
        }
    }

}
