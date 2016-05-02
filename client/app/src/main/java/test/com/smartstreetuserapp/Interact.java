package test.com.smartstreetuserapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

//Tree interaction class
//light and sound interaction

public class Interact extends AppCompatActivity {
    private static final String TAG = "Log";
    RadioGroup radiogrpAction;
    RadioGroup radiogrpColor;
    RadioButton radioaActionLight;
    RadioButton radiocolorGreen;
    RadioButton radiocolorBlue;
    Button offBttn;
    Button onBttn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        radioaActionLight = (RadioButton) findViewById(R.id.lightButton);
        radiocolorGreen = (RadioButton) findViewById(R.id.greenButton);
        radiocolorBlue = (RadioButton) findViewById(R.id.blueButton);
        radiogrpAction = (RadioGroup) findViewById(R.id.radioGrpAction);
        //ledo
        radiogrpColor = (RadioGroup) findViewById(R.id.radioGrpColor);//led1
        offBttn = (Button) findViewById(R.id.offButton);
        onBttn = (Button) findViewById(R.id.onButton);


        offBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Interact.this, "OFF", Toast.LENGTH_SHORT).show();
                int selectId = radiogrpColor.getCheckedRadioButtonId();
                if (selectId == radiocolorBlue.getId())
                    new SparkInteract().execute("D7,off");
                else if (selectId == radiocolorGreen.getId())
                    new SparkInteract().execute("D0,off");
            }
        });

        onBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Interact.this, "ON", Toast.LENGTH_SHORT).show();
                int selectId = radiogrpColor.getCheckedRadioButtonId();
                if (selectId == radiocolorBlue.getId())
                    new SparkInteract().execute("D7,on");
                else if (selectId == radiocolorGreen.getId())
                    new SparkInteract().execute("D0,on");
            }
        });
    }

    /*
     * POST EXAMPLE
     */
    // We must do this as a background task, elsewhere our app crashes
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_Logout) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}


