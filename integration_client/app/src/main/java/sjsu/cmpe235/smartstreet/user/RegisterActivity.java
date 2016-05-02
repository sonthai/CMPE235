package sjsu.cmpe235.smartstreet.user;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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


public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "Log";
    EditText textName;
    EditText textPassword;
    EditText textEmailid;
    EditText phoneText;
    EditText lastNameText;
    Button submitBttn;
    EditText textuserName;
    private final String registerUrl = "http://ec2-52-27-135-64.us-west-2.compute.amazonaws.com:3000/users/register";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        textName = (EditText) findViewById(R.id.fnameText);
        textPassword = (EditText) findViewById(R.id.passwordText);
        textEmailid = (EditText) findViewById(R.id.emailText);
        textuserName = (EditText) findViewById(R.id.usernameTxt);
        phoneText = (EditText) findViewById(R.id.phoneText);
        lastNameText = (EditText) findViewById(R.id.lnText);
        submitBttn = (Button) findViewById(R.id.registerButton);
        submitBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser(v);

            }

        });

    }

    public void registerUser(View v) {
        String firstName = textName.getText().toString();
        String password = textPassword.getText().toString();
        String email = textEmailid.getText().toString();
        String lastName = lastNameText.getText().toString();
        String phoneNumber = phoneText.getText().toString();
        String userName = textuserName.getText().toString();

        if(password.length()<=6){
            Toast.makeText(getApplicationContext(), "Password should be of at least 7 characters", Toast.LENGTH_LONG).show(); // displaying message
            return;
        }
        if (firstName.equals("") || email.equals("") || password.equals("") || userName.equals("")) {
            Toast.makeText(getApplicationContext(), "Field Required", Toast.LENGTH_LONG).show(); // displaying message
            return;
        } else {

            JSONObject json = new JSONObject();
            try {
                json.put("phoneNumber", phoneNumber);
                json.put("password", password);
                json.put("email", email);
                json.put("firstName", firstName);
                json.put("lastName", lastName);
                json.put("userName", userName);

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

                        Toast.makeText(getApplicationContext(), "Succesfully Registered , please sign in "  , Toast.LENGTH_LONG).show();
                    }
                    Intent i = new Intent(getApplicationContext(), LoginActivity.class);
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
