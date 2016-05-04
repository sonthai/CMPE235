package sjsu.cmpe235.smartstreet.user;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;


/**
 * A simple {@link Fragment} subclass.
 */
public class InteractFragment extends Fragment {
    private static final String TAG ="Interact" ;
    RadioGroup radiogrpAction;
    RadioGroup radiogrpColor;
    RadioButton radioaActionLight;
    RadioButton radiocolorGreen;
    RadioButton radiocolorBlue;
    RadioButton radioActionSound;
    Button offBttn;
    Button onBttn;


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
                int selectId = radiogrpColor.getCheckedRadioButtonId();
                if(selectId==radiocolorBlue.getId())
                    new SparkInteract().execute("D7,off");
                else
                if(selectId==radiocolorGreen.getId())
                    new SparkInteract().execute("D0,off");
                int selectActionSound = radiogrpAction.getCheckedRadioButtonId();
                if(selectActionSound==radioActionSound.getId()){

                    new SparkSoundInteract().execute("off");
                }
            }
        });

        onBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(Interact.this, "OFF", Toast.LENGTH_SHORT).show();
                int selectId = radiogrpColor.getCheckedRadioButtonId();
                if(selectId==radiocolorBlue.getId())
                    new SparkInteract().execute("D7,on");
                else
                if(selectId==radiocolorGreen.getId())
                    new SparkInteract().execute("D0,on");
                int selectActionSound = radiogrpAction.getCheckedRadioButtonId();
                if(selectActionSound==radioActionSound.getId()){
                    new SparkSoundInteract().execute("on");
                    radiogrpColor.setVisibility(View.INVISIBLE);
                }

            }
        });


        return v;
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

}
