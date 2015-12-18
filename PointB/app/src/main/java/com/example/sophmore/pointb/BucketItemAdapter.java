package com.example.sophmore.pointb;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.sophmore.pointb.model.BucketItem;
import com.example.sophmore.pointb.model.DataManager;

import java.util.ArrayList;

/**
 * BucketItemAdapter
 *
 * The custom adapter for a user's bucket list. Holds a title, date (time string), a clear button, and a done button
 *
 * The clear button removes the item from the bucketlist and decrements the amount of ideas the user had
 * The done button removes the item from the bucketlist and increments the amount of done ideas the user had
 *
 */
public class BucketItemAdapter extends ArrayAdapter<BucketItem> {
    // View lookup cache
    private static class ViewHolder {
        TextView title;
        TextView date;
        ImageButton clearButton;
        ImageButton doneButton;
    }

    public BucketItemAdapter(Context context, ArrayList<BucketItem> notes) {
        super(context, R.layout.item_bucket, notes);
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
            convertView = inflater.inflate(R.layout.item_bucket, parent, false);
            viewHolder.title = (TextView) convertView.findViewById(R.id.title);
            viewHolder.date = (TextView) convertView.findViewById(R.id.time);
            viewHolder.clearButton = (ImageButton) convertView.findViewById(R.id.clearButton);
            viewHolder.doneButton = (ImageButton) convertView.findViewById(R.id.doneButton);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate the data into the template view using the data object
        viewHolder.title.setText(bucketItem.getTitle());
        viewHolder.date.setText(bucketItem.getTimeString());
        viewHolder.clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataManager.removeItemFromUser(bucketItem.getBucketItem().getObjectId());
            }
        });
        viewHolder.doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataManager.userFinishedItem(bucketItem.getBucketItem().getObjectId());
            }
        });
        // Return the completed view to render on screen
        return convertView;
    }
}
