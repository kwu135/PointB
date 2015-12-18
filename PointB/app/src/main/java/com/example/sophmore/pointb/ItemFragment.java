package com.example.sophmore.pointb;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.sophmore.pointb.model.BucketItem;
import com.example.sophmore.pointb.model.BucketItemSingleton;
import com.example.sophmore.pointb.model.DataManager;
import com.parse.ParseUser;
import com.pkmmte.view.CircularImageView;

import java.io.File;
import java.util.ArrayList;

/**
 * ItemFragment
 *
 * Fragment representing the single user's bucket list
 *
 *  @note Tried to implement gallery for profile pictures, but did not correct work for all devices. Had an external
 * storage access error and could not fix it
 */
public class ItemFragment extends Fragment {
//
//    private OnFragmentInteractionListener mListener;

    private static final int SELECTED_PICTURE = 1;
    private ListView listView;
    private ArrayList<BucketItem> bucketItems;
    private BucketItemAdapter bucketItemAdapter;
    private CircularImageView profilePic;

    private TextView usernameLabel;
    private TextView descriptionLabel;
    private TextView groupsNum;
    private TextView ideasNum;
    private TextView doneNum;

    // TODO: Rename and change types of parameters
    public static ItemFragment newInstance() {
        ItemFragment fragment = new ItemFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_item, container, false);

        DataManager.getUserBucketList();

        // Inflate the layout for this fragment
        listView = (ListView) v.findViewById(R.id.listView);
        bucketItems = BucketItemSingleton.getInstance().getmBucketList();
        bucketItemAdapter = new BucketItemAdapter(getContext(), bucketItems);
        listView.setAdapter(bucketItemAdapter);
        BucketItemSingleton.getInstance().attachListViewToFAB(listView);
        profilePic = (CircularImageView) v.findViewById(R.id.profile_pic);
        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, SELECTED_PICTURE);
            }
        });

        usernameLabel = (TextView) v.findViewById(R.id.username_label);
        descriptionLabel = (TextView) v.findViewById(R.id.description_label);
        groupsNum = (TextView) v.findViewById(R.id.group_num);
        ideasNum = (TextView) v.findViewById(R.id.idea_num);
        doneNum = (TextView) v.findViewById(R.id.done_num);

        ParseUser currentUser = ParseUser.getCurrentUser();
        usernameLabel.setText("@" + currentUser.getUsername());
        descriptionLabel.setText((String) currentUser.get("description"));
        groupsNum.setText("" + currentUser.get("groupCounter") + " groups");
        ideasNum.setText("" + currentUser.get("itemCounter") + " ideas");
        doneNum.setText("" + currentUser.get("numFulfilledIdeas") + " done");

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode) {
            case SELECTED_PICTURE:
                if (resultCode == Activity.RESULT_OK) {
                    Uri uri = data.getData();
                    String[] projection = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(projection[0]);
                    String filePath = cursor.getString(columnIndex);
                    cursor.close();
                    File myFile = new File(filePath);
                    Bitmap selectedImage = BitmapFactory.decodeFile(myFile.getAbsolutePath());

                    if (selectedImage != null) {
                        profilePic.setImageBitmap(selectedImage);
                    }
                }
                break;
            default:
                break;
        }
    }

    public void updateUserInfo() {
        ParseUser currentUser = ParseUser.getCurrentUser();
        usernameLabel.setText("@" + currentUser.getUsername());
        descriptionLabel.setText((String) currentUser.get("description"));
        groupsNum.setText("" + currentUser.get("groupCounter") + " groups");
        ideasNum.setText("" + currentUser.get("itemCounter") + " ideas");
        doneNum.setText("" + currentUser.get("numFulfilledIdeas") + " done");
    }

    public void updateUIList() {
        Runnable run = new Runnable() {
            @Override
            public void run() {
                bucketItems = BucketItemSingleton.getInstance().getmBucketList();
                bucketItemAdapter.clear();
                bucketItemAdapter.addAll(bucketItems);
                bucketItemAdapter.notifyDataSetChanged();
            }
        };
        if (getActivity() != null) {
            getActivity().runOnUiThread(run);
        }
    }

}
