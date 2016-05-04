package sjsu.cmpe235.smartstreet.admin;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

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
import java.util.ArrayList;

import sjsu.cmpe235.smartstreet.admin.models.Sensor;
import sjsu.cmpe235.smartstreet.admin.models.Tree;
import sjsu.cmpe235.smartstreet.admin.utils.CustomTreeAdapter;
import sjsu.cmpe235.smartstreet.user.Constant.Constants;
import sjsu.cmpe235.smartstreet.user.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class TreeMaintenanceFragment extends Fragment {
    ListView treeListView;
    CustomTreeAdapter adapter;
    ArrayList<Tree> trees;

    public TreeMaintenanceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_treemaint, container, false);
        treeListView = (ListView) v.findViewById(R.id.treeListView);
        new TreeAsync().execute(Constants.url + "/trees/list");
        adapter = new CustomTreeAdapter(getActivity().getApplicationContext(), trees);
        treeListView.setAdapter(adapter);
        return  v;
    }

    private class TreeAsync extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            HttpURLConnection conn = null;
            BufferedReader reader = null;
            String JsonResponse = null;
            URL url;
            trees = new ArrayList<Tree>();
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
                    String date = finalObject.getString("deployment_date");
                    String status = finalObject.getString("tree_status");
                    String treeId = finalObject.getString("tree_id");
                    String location = finalObject.getString("tree_location");
                    Tree tree = new Tree(treeId, sensorId, date, status, location);
                    trees.add(tree);
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
    }

}
