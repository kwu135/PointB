package com.example.sophmore.pointb;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.sophmore.pointb.model.BucketItem;
import com.example.sophmore.pointb.model.DataManager;

import java.util.ArrayList;

/**
 * SearchedBucketItemAdapter
 *
 * Similar to the BucketItemAdapter except without the remove and done buttons on them, as you are only viewing
 * not editing other people's bucket lists
 *
 */
public class SearchedBucketItemAdapter extends ArrayAdapter<BucketItem> {
    // View lookup cache
    private static class ViewHolder {
        TextView title;
        TextView date;
    }

    public SearchedBucketItemAdapter(Context context, ArrayList<BucketItem> notes) {
        super(context, R.layout.item_bucket_searched, notes);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final BucketItem bucketItem = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_bucket_searched, parent, false);
            viewHolder.title = (TextView) convertView.findViewById(R.id.title);
            viewHolder.date = (TextView) convertView.findViewById(R.id.time);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate the data into the template view using the data object
        viewHolder.title.setText(bucketItem.getTitle());
        viewHolder.date.setText(bucketItem.getTimeString());

        // Return the completed view to render on screen
        return convertView;
    }
}
