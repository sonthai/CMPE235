package sjsu.cmpe235.smartstreet.user.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import sjsu.cmpe235.smartstreet.user.R;
import sjsu.cmpe235.smartstreet.user.model.Comment;


public class CommentAdapter extends BaseAdapter {
    private ArrayList<Comment> comments;
    private LayoutInflater mInflater;
    //private ListView lv;

    public CommentAdapter(Context context,  ArrayList<Comment> comments) {
        this.comments = comments;
        //this.lv = lv;
        this.mInflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return comments.size();
    }

    @Override
    public Object getItem(int position) {
        return comments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.row, null);
            holder = new ViewHolder();
            holder.userName = (TextView)convertView.findViewById(R.id.user_textview);
            holder.comment = (TextView)convertView.findViewById(R.id.review);
            holder.rating = (RatingBar)convertView.findViewById(R.id.rating);
            holder.date = (TextView) convertView.findViewById(R.id.date_textview);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.userName.setText(comments.get(position).getUserName());
        holder.comment.setText(comments.get(position).getComment());
        holder.rating.setRating(comments.get(position).getRating());
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        holder.date.setText(format.format(comments.get(position).getCommentDate()));

        return convertView;
    }

    public class ViewHolder {
        TextView userName;
        TextView comment;
        TextView date;
        RatingBar rating;
    }
}
