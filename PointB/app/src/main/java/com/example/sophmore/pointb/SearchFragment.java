package com.example.sophmore.pointb;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.sophmore.pointb.model.BucketItem;
import com.example.sophmore.pointb.model.BucketItemSingleton;
import com.example.sophmore.pointb.model.DataManager;
import com.parse.ParseUser;
import com.pkmmte.view.CircularImageView;

import java.util.ArrayList;


/**
 * SearchFragment
 *
 * Fragment used to search usernames to view their bucket lists
 *
 */
public class SearchFragment extends Fragment {

    private EditText userField;
    private ImageButton searchButton;
    private ListView listView;
    private ArrayList<BucketItem> bucketItems;
    private SearchedBucketItemAdapter searchedBucketItemAdapter;

    private TextView usernameLabel;
    private TextView descriptionLabel;
    private TextView groupsNum;
    private TextView ideasNum;
    private TextView doneNum;

    public SearchFragment() {
        // Required empty public constructor
    }

    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_search, container, false);
        userField = (EditText) v.findViewById(R.id.search_name_field);
        searchButton = (ImageButton) v.findViewById(R.id.search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataManager.searchForUser(userField.getText().toString().trim());
                Utility.hideSoftKeyboard(getActivity());
            }
        });

        listView = (ListView) v.findViewById(R.id.listView_search);
        bucketItems = new ArrayList<BucketItem>();
        searchedBucketItemAdapter = new SearchedBucketItemAdapter(getContext(), bucketItems);
        listView.setAdapter(searchedBucketItemAdapter);

        usernameLabel = (TextView) v.findViewById(R.id.username_label_search);
        descriptionLabel = (TextView) v.findViewById(R.id.description_label_search);
        groupsNum = (TextView) v.findViewById(R.id.group_num_search);
        ideasNum = (TextView) v.findViewById(R.id.idea_num_search);
        doneNum = (TextView) v.findViewById(R.id.done_num_search);

        return v;
    }

    public void updateSearchedUserInfo(ParseUser user) {
        usernameLabel.setText("@" + user.getUsername());
        descriptionLabel.setText((String) user.get("description"));
        groupsNum.setText("" + user.get("groupCounter") + " groups");
        ideasNum.setText("" + user.get("itemCounter") + " ideas");
        doneNum.setText("" + user .get("numFulfilledIdeas") + " done");
    }

    public void updateSearchedBucketList(final ArrayList<BucketItem> bucketList) {
        Runnable run = new Runnable() {
            @Override
            public void run() {
                bucketItems = bucketList;
                searchedBucketItemAdapter.clear();
                searchedBucketItemAdapter.addAll(bucketItems);
                searchedBucketItemAdapter.notifyDataSetChanged();
            }
        };
        if (getActivity() != null) {
            getActivity().runOnUiThread(run);
        }
    }

}
