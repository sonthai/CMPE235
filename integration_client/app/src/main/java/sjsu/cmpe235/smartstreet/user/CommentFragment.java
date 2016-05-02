package sjsu.cmpe235.smartstreet.user;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
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
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import sjsu.cmpe235.smartstreet.user.model.Comment;
import sjsu.cmpe235.smartstreet.user.utils.SessionHandler;

public class CommentFragment extends Fragment {
    private static final String TAG = "Comment";
    RatingBar ratingbar;
    EditText commentText;
    ListView commentLisView;
    Button commentBttn;
    private final String postcommentUrl = "http://ec2-52-27-135-64.us-west-2.compute.amazonaws.com:3000/comments/add";
    private final String getcommentURL = "http://ec2-52-27-135-64.us-west-2.compute.amazonaws.com:3000/comments/list";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_interact, container, false);
        View v = inflater.inflate(R.layout.comment_fragment, null);
        ratingbar = (RatingBar) v.findViewById(R.id.ratingBar);
        commentText = (EditText) v.findViewById(R.id.commentBox);
        commentLisView = (ListView) v.findViewById(R.id.commentlistView);
        commentBttn = (Button) v.findViewById(R.id.commentButton);

        final SessionHandler session = new SessionHandler();

        commentBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(commentText.equals("")||ratingbar.equals("")){
                    Toast.makeText(getActivity().getApplicationContext(), "Field Required", Toast.LENGTH_LONG).show();
                }
                else
                    userComment();

            }
        });
        return v;
    }

    // method to display all the comments in custom listView
    private void displayComment() {

        SessionHandler session = new SessionHandler(getActivity().getApplicationContext());
        session.checkLoginStatus();
        HashMap<String, String> map = session.userDetails();
        final String userName = map.get(SessionHandler.KEY_USERNAME);
        System.out.println("inside display comment");

        class getJSON extends AsyncTask<String, String, List<Comment>> {

            @Override
            protected List<Comment> doInBackground(String... params) {
                HttpURLConnection conn = null;
                BufferedReader reader = null;
                String JsonResponse = null;
                URL url;
                List<Comment> commentModelList = new ArrayList<>();
                try {

                    url = new URL(params[0]);
                    conn = (HttpURLConnection) url.openConnection();

                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("Content-Type", "application/json");
                    conn.setRequestProperty("Accept", "application/json");
                    conn.setDoInput(true);
                    System.out.println("inside Async task");
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
                    System.out.println("Final Json" + finalJson);
                    JSONObject parentObject = new JSONObject(finalJson);
                    JSONArray parentArray = parentObject.getJSONArray("message");
                    for (int i = 0; i < parentArray.length(); i++) {
                        JSONObject finalObject = parentArray.getJSONObject(i);
                        Comment commentModel = new Comment();
                        commentModel.setUserName(finalObject.getString("userName"));
                        // commentModel.setDate(finalObject.getString("Date"));
                        commentModel.setComment(finalObject.getString("comment"));
                        commentModel.setRating((float) finalObject.getDouble("rating"));
                        System.out.println(commentModel);
                        commentModelList.add(commentModel);
                        System.out.println("Hello");
                    }
                    return commentModelList;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally

                {
                    if (conn != null) {
                        conn.disconnect();
                    }
                }


                return commentModelList;
            }




            @Override
            protected void onPostExecute(List<Comment> comment) {
                super.onPostExecute(comment);
                {
                    CommentAdapter adapter = new CommentAdapter(getActivity().getApplicationContext(), R.layout.row, comment);
                    commentLisView.setAdapter(adapter);

                    Toast.makeText(getActivity().getApplicationContext(), "Succesfully comment ", Toast.LENGTH_LONG).show();
                }
            }

        }

        new getJSON().execute(getcommentURL);
        System.out.println("getJSONTask.");


    }

    // method to post user comments to server
    private void userComment() {

        String comment = commentText.getText().toString();
        String rating = String.valueOf(ratingbar.getRating());

        JSONObject json = new JSONObject();
        SessionHandler session = new SessionHandler(getActivity().getApplicationContext());
        session.checkLoginStatus();
        HashMap<String, String> map = session.userDetails();
        String userName = map.get(SessionHandler.KEY_USERNAME);


        try {
            json.put("userName", userName);
            json.put("comment", comment);
            json.put("rating", rating);

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

                    url = new URL(postcommentUrl);
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
                    displayComment();
                    System.out.println("Check");
                    Toast.makeText(getActivity().getApplicationContext(), "Succesfully comment ", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getActivity().getApplicationContext(),CommentFragment.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    getActivity().finish();
                }


            }
        }
        if (json.length() > 0) {
            new JSONTask().execute(String.valueOf(json));
        }

    }
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_Logout) {
            Intent i = new Intent(getActivity().getApplicationContext(), LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            getActivity().finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Adapter for comment to populate listView
    public class CommentAdapter extends ArrayAdapter {

        private List<Comment> commentModelList;
        private int resource;
        private LayoutInflater inflater;

        // constructor
        public CommentAdapter(Context context, int resource, List<Comment> objects) {
            super(context, resource, objects);
            commentModelList = objects;
            this.resource = resource;
            inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.row, null);
            }
            // TextView Time;
            TextView UserName = (TextView)convertView.findViewById(R.id.userNameView);
            TextView Comment = (TextView)convertView.findViewById(R.id.commentView);
            //Time = (TextView) findViewById(R.id.timeView);
            RatingBar rating = (RatingBar)convertView.findViewById(R.id.ratingBar2);
            if (UserName != null) {
                UserName.setText(commentModelList.get(position).getUserName());
            }
            if (Comment != null) {
                Comment.setText("Comment:" + commentModelList.get(position).getComment());
            }
            //Time.setText(commentModelList.get(position).getDate());
            if (rating != null) {
                rating.setRating(commentModelList.get(position).getRating());
            }
            System.out.println(rating);

            return convertView;

        }
    }


}
