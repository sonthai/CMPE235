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

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "Log";
    private final String loginUrl = "http://ec2-52-27-135-64.us-west-2.compute.amazonaws.com:3000/users/login";

    EditText usernameText;
    EditText passwordText;
    Button signBttn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameText = (EditText) findViewById(R.id.idText);
        passwordText = (EditText) findViewById(R.id.pwdText);
        signBttn = (Button) findViewById(R.id.loginBttn);
        signBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signinUser(v);
            }
        });
    }

    public void signinUser(View v) {
        String userName = usernameText.getText().toString();
        String password = passwordText.getText().toString();
        if (password.equals("") || userName.equals("")) {
            Toast.makeText(getApplicationContext(), "Field Required", Toast.LENGTH_LONG).show(); // displaying message
            return;
        } else {

            JSONObject json = new JSONObject();
            try {
                json.put("userName", userName);
                json.put("password", password);


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
                    String JsonDATA = params[0];
                    URL url;

                    try {

                        url = new URL(loginUrl);
                        conn = (HttpURLConnection) url.openConnection();

                        conn.setRequestMethod("POST");
                        conn.setRequestProperty("Content-Type", "application/json");
                        conn.setRequestProperty("Accept", "application/json");
                        conn.setDoInput(true);
                        conn.setDoOutput(true);
                        Writer writer = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), "UTF-8"));
                        writer.write(JsonDATA);
                        System.out.println(JsonDATA);
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
                            // Stream was empty. No point in parsing.
                            return null;
                        }
                        JsonResponse = buffer.toString();
                        System.out.println(JsonResponse);

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
                            ex.printStackTrace();
                        }
                    }


                    return JsonResponse;

                }

                @Override
                protected void onPostExecute(String response) {
                    super.onPostExecute(response);
                    try {
                        JSONObject parentObject = new JSONObject(response);
                        int errorCode = parentObject.getInt("errorCode");
                        String mode = parentObject.optString("mode");
                        String user = "user";
                        System.out.println(errorCode);
                        if (errorCode == 0) {

                            if (mode.equals(user)) {
                                Toast.makeText(getApplicationContext(), "Welcome User", Toast.LENGTH_LONG).show();
                                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(i);
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), "Welcome User", Toast.LENGTH_LONG).show();
                                //Intent i = new Intent(getApplicationContext(), AdminHome.class);
                                //i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                //startActivity(i);
                                //finish();

                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Invalid Username/ password, please try again", Toast.LENGTH_LONG).show();
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }

            if (json.length() > 0)

            {
               new JSONTask().execute(String.valueOf(json));
                //Intent i = new Intent(this, MainActivity.class);
                //startActivity(i);
                //finish();
            }

        }


    }

    private void viewMain() {

        Intent loginIntent = new Intent(this, MainActivity.class);
        startActivity(loginIntent);
    }

    private void registerView() {
        Intent registerIntent = new Intent(this, RegisterActivity.class);
        startActivity(registerIntent);

    }


    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.loginBttn:
                viewMain();
                break;

            case R.id.regbttn:
                registerView();
                break;

            case R.id.skipText:
                viewMain();
                break;
        }
    }
}

