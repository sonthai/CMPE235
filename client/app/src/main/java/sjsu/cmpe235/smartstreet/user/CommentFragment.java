package sjsu.cmpe235.smartstreet.user;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import sjsu.cmpe235.smartstreet.user.Constant.Constants;
import sjsu.cmpe235.smartstreet.user.model.Comment;
import sjsu.cmpe235.smartstreet.user.utils.CommentAdapter;
import sjsu.cmpe235.smartstreet.user.services.CommentServices;

public class CommentFragment extends Fragment {
    private static final String TAG = "Comment";
    ArrayList<Comment> comments = new ArrayList<>();
    CommentServices services = new CommentServices();
    RatingBar ratingbar;
    EditText commentText;
    ListView commentListView;
    Button commentBttn;
    Context context;
    CommentAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final String userName = ((MainActivity) getActivity()).getCurrentUser();

        View v = inflater.inflate(R.layout.comment_fragment, container, false);
        if (userName != null) {
            ratingbar = (RatingBar) v.findViewById(R.id.ratingBar);
            commentText = (EditText) v.findViewById(R.id.commentBox);
            commentBttn = (Button) v.findViewById(R.id.commentButton);
            ratingbar.setVisibility(View.VISIBLE);
            commentBttn.setVisibility(View.VISIBLE);
            commentText.setVisibility(View.VISIBLE);

            commentBttn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(commentText.getText().toString().equals("") || ratingbar.getRating() == 0){
                        if (commentText.getText().toString().equals("")) {
                            Toast.makeText(getActivity().getApplicationContext(), "Field Required", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getActivity().getApplicationContext(), "Invalid Rating", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        try {
                            Date dateObj = new Date();
                            SimpleDateFormat format = new SimpleDateFormat(Constants.DATEFORMAT);
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("userName", userName);
                            jsonObject.put("comment", commentText.getText().toString());
                            jsonObject.put("rating", String.valueOf(ratingbar.getRating()));
                            jsonObject.put("date", format.format(dateObj));
                            ;
                            services.addComment(String.valueOf(jsonObject));
                            Toast.makeText(getActivity(),"Comment is successfully inserted", Toast.LENGTH_SHORT).show();
                            commentText.setText("");
                            ratingbar.setRating(0);
                            Comment comment = new Comment(commentText.getText().toString(), ratingbar.getRating(), userName,
                                    format.format(dateObj));
                            comments.add(comment);
                            commentListView.invalidateViews();
                        } catch (JSONException jsonE) {
                            jsonE.printStackTrace();
                        }
                    }
                }
            });
        }


        commentListView = (ListView) v.findViewById(R.id.commentlistView);
        context = getActivity().getApplicationContext();
        services.displayComments(this);

        return v;
    }

    public void renderCommentListView(ArrayList<Comment> comments) {
        Collections.sort(comments, new Comparator<Comment>() {
            @Override
            public int compare(Comment lhs, Comment rhs) {
                return rhs.getDate().compareTo(lhs.getDate());
            }
        });
        adapter = new CommentAdapter(context,  comments);
        commentListView.setAdapter(adapter);
    }
}
