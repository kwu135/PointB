package com.example.sophmore.pointb;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * SignInFragment
 *
 * Fragment from Initial Activity for signing in
 * Uses SharedPreferences to store the last logged in username
 *
 */
public class SignInFragment extends Fragment {

    public static final String PREFERENCES_FILENAME="preferences";
    public static final String PREFERENCES_USERNAME="username";

    private Button continueButton;
    private EditText usernameEditText;
    private EditText passwordEditText;

    public static SignInFragment newInstance() {
        SignInFragment fragment = new SignInFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public SignInFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_sign_in, container, false);
        // Inflate the layout for this fragment
        continueButton = (Button) v.findViewById(R.id.continue_button);
        usernameEditText = (EditText) v.findViewById(R.id.usernameEditText_SignIn);
        SharedPreferences prefs = getActivity().getSharedPreferences(PREFERENCES_FILENAME, getActivity().MODE_PRIVATE);
        String pastUsername = prefs.getString(PREFERENCES_USERNAME, "");
        usernameEditText.setText(pastUsername);
        passwordEditText = (EditText) v.findViewById(R.id.passwordEditText_SignIn);
        passwordEditText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
           @Override
           public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
               if (actionId == EditorInfo.IME_ACTION_DONE){
                   signIn();
                   Utility.hideSoftKeyboard(getActivity());
                   return true;  // mark the event as consumed
               }
               return false;
           }
       });
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
        return v;
    }

    private void signIn() {
        ParseUser.logInInBackground(usernameEditText.getText().toString(), passwordEditText.getText()
                .toString(), new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                // Handle the response
                if (e == null) {
                    SharedPreferences prefs = getActivity().getSharedPreferences(PREFERENCES_FILENAME, getActivity().MODE_PRIVATE);
                    SharedPreferences.Editor prefEditor = prefs.edit();
                    String pastUsername = usernameEditText.getText().toString();
                    prefEditor.putString(PREFERENCES_USERNAME, pastUsername);
                    prefEditor.commit();
                    Intent intent = new Intent(getContext(), MainActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                } else {
                    Toast.makeText(getContext(), "Invalid Login", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}
