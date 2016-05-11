package sjsu.cmpe235.smartstreet.admin.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import sjsu.cmpe235.smartstreet.admin.models.Sensor;
import sjsu.cmpe235.smartstreet.admin.models.Tree;
import sjsu.cmpe235.smartstreet.user.R;

public class CustomTreeAdapter extends BaseAdapter {
    private ArrayList<Tree> trees;
    private LayoutInflater mInflater;

    public CustomTreeAdapter(Context context, ArrayList<Tree> trees) {
        this.trees = trees;
        this.mInflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return trees.size();
    }

    @Override
    public Object getItem(int position) {
        return trees.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.tree_row, null);
            holder = new ViewHolder();
            holder.sensorId = (TextView)convertView.findViewById(R.id.tree_sensorId);
            holder.treeId = (TextView)convertView.findViewById(R.id.treeId);
            holder.status = (TextView) convertView.findViewById(R.id.treeStatus);
            holder.date = (TextView) convertView.findViewById(R.id.treeDate);
            holder.location = (TextView) convertView.findViewById(R.id.treeLocation);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.sensorId.setText(trees.get(position).getSensorId());
        holder.status.setText(trees.get(position).getStatus());
        holder.location.setText(trees.get(position).getLocation());
        holder.date.setText(trees.get(position).getDate());
        holder.treeId.setText(trees.get(position).getTreeId());


        return convertView;
    }

    public class ViewHolder {
        TextView sensorId;
        TextView location;
        TextView date;
        TextView treeId;
        TextView status;
    }
}
