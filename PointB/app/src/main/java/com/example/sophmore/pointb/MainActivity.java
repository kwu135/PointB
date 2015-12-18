package com.example.sophmore.pointb;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.design.widget.TabLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SearchEvent;
import android.view.View;
import android.view.ViewGroup;

import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.sophmore.pointb.model.BucketItem;
import com.example.sophmore.pointb.model.BucketItemSingleton;
import com.example.sophmore.pointb.model.DataManager;
import com.melnykov.fab.FloatingActionButton;
import com.parse.Parse;
import com.parse.ParseUser;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Locale;

/**
 * MainActivity
 *
 * Activity that contains a tabbed activity containing 2 tabs to either the personal bucket list or the bucket list search fragments
 *
 */
public class MainActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    private int[] tabImgResId = {
            R.drawable.tab_1_icon,
            R.drawable.tab_2_icon,
            R.drawable.tab_3_icon,
            R.drawable.tab_4_icon
    };
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private ItemFragment itemFragment;
    private SearchFragment searchFragment;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FontsOverride.setDefaultFont(getApplicationContext(), "DEFAULT", "fonts/Karla-Bold.ttf");
        FontsOverride.setDefaultFont(getApplicationContext(), "MONOSPACE", "fonts/Karla-Bold.ttf");
        FontsOverride.setDefaultFont(getApplicationContext(), "SERIF", "fonts/Karla-Bold.ttf");
        FontsOverride.setDefaultFont(getApplicationContext(), "SANS_SERIF", "fonts/Karla-Bold.ttf");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setFillViewport(true);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.tabBackground));
        
        //tabLayout.getTabAt(0).setIcon(R.drawable.tab_1_icon);
        tabLayout.getTabAt(1).setIcon(R.drawable.tab_2_icon);
        //.getTabAt(2).setIcon(R.drawable.tab_3_icon);
        tabLayout.getTabAt(0).setIcon(R.drawable.tab_4_icon);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        //fab.setRippleColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                // 1. Instantiate an AlertDialog.Builder with its constructor
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                LayoutInflater inflater = getLayoutInflater();

                final View v = inflater.inflate(R.layout.dialog_create_item, null);
                builder.setView(v);
                // Add the buttons
                builder.setPositiveButton(R.string.create, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
//                        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                                .setAction("Action", null).show();
                        EditText itemTitle = (EditText) v.findViewById(R.id.item_name_field);
                        DataManager.createItem(itemTitle.getText().toString().trim());
                    }
                });

                // 3. Get the AlertDialog from create()
                AlertDialog dialog = builder.create();
                dialog.show();
//              Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });

        BucketItemSingleton.getInstance(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_log_out) {
            ParseUser.logOutInBackground();
            Intent intent = new Intent(this, InitialActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            BucketItemSingleton.getInstance().clean();
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void attachListViewToFAB(ListView lw) {
        fab.attachToListView(lw);
    }
    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            if (position == 1) {
                searchFragment = SearchFragment.newInstance();
                return searchFragment;
            }
            if (position == 0) {
                itemFragment = ItemFragment.newInstance();
                return itemFragment;
            }
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "";
        }
    }

    public void updateUserListTab() {
        if (itemFragment != null) {
            itemFragment.updateUIList();
        }
    }

    public void updateUserInfo() {
        if (itemFragment != null) {
            itemFragment.updateUserInfo();
        }
    }

    public void updateSearchedUserInfo(ParseUser user) {
        if (searchFragment != null) {
            searchFragment.updateSearchedUserInfo(user);
        }
    }

    public void updateSearchedBucketList(ArrayList<BucketItem> bucketList) {
        if (searchFragment != null) {
            searchFragment.updateSearchedBucketList(bucketList);
        }
    }
    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText("Will be completed in a future update!");
            return rootView;
        }
    }
}
