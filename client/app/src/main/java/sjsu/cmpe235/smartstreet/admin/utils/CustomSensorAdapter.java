package sjsu.cmpe235.smartstreet.admin.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

import sjsu.cmpe235.smartstreet.admin.models.Sensor;
import sjsu.cmpe235.smartstreet.user.R;

public class CustomSensorAdapter extends BaseAdapter {
    private ArrayList<Sensor> sensors;
    private LayoutInflater mInflater;

    public CustomSensorAdapter(Context context, ArrayList<Sensor> sensors) {
        this.sensors = sensors;
        this.mInflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return sensors.size();
    }

    @Override
    public Object getItem(int position) {
        return sensors.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.sensor_row, null);
            holder = new ViewHolder();
            holder.sensorId = (TextView)convertView.findViewById(R.id.sensorId);
            holder.status = (TextView)convertView.findViewById(R.id.sStatus);
            holder.type = (TextView) convertView.findViewById(R.id.sType);
            holder.date = (TextView) convertView.findViewById(R.id.sDate);
            holder.location = (TextView) convertView.findViewById(R.id.sLocation);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.sensorId.setText(sensors.get(position).getSensorId());
        holder.status.setText(sensors.get(position).getStatus());
        holder.location.setText(sensors.get(position).getLocation());
        holder.date.setText(sensors.get(position).getDate());
        holder.type.setText(sensors.get(position).getType());


        return convertView;
    }

    public class ViewHolder {
        TextView sensorId;
        TextView location;
        TextView date;
        TextView type;
        TextView status;
    }
}
