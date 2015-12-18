package com.example.sophmore.pointb;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import org.w3c.dom.Text;

import java.util.HashMap;

/**
 * SignUpFragment
 *
 * Fragment from InitialActivity to sign up users
 * Parse automatically checks for same usernames and emails
 *
 */
public class SignUpFragment extends Fragment {

    private EditText emailEditText;
    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button signUpButton;

    // TODO: Rename and change types and number of parameters
    public static SignUpFragment newInstance() {
        SignUpFragment fragment = new SignUpFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public SignUpFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_sign_up, container, false);
        emailEditText = (EditText) v.findViewById(R.id.emailEditText);
        firstNameEditText = (EditText) v.findViewById(R.id.firstNameEditText);
        lastNameEditText = (EditText) v.findViewById(R.id.lastNameEditText);
        usernameEditText = (EditText) v.findViewById(R.id.usernameEditText_SignUp);
        passwordEditText = (EditText) v.findViewById(R.id.passwordEditText_SignUp);
        passwordEditText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE){
                    signUp();
                    Utility.hideSoftKeyboard(getActivity());
                    return true;  // mark the event as consumed
                }
                return false;
            }
        });

        signUpButton = (Button) v.findViewById(R.id.sign_up_button);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });
        return v;
    }

    private void signUp() {
        String email = emailEditText.getText().toString().trim();
        String firstName = firstNameEditText.getText().toString().trim();
        String lastName = lastNameEditText.getText().toString().trim();
        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        boolean fieldValid = true;
        if(email.length() == 0 || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            fieldValid = false;
        }
        if(firstName.length() == 0) {
            fieldValid = false;
        }
        if(lastName.length() == 0) {
            fieldValid = false;
        }
        if(username.length() == 0) {
            fieldValid = false;
        }
        if(password.length() == 0) {
            fieldValid = false;
        }
        if (fieldValid) {
            ParseUser user = new ParseUser();
            user.setUsername(username);
            user.setPassword(password);
            user.setEmail(email);
            user.put("firstName", firstName);
            user.put("lastName", lastName);
            // Call the Parse signup method
            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    // Handle the response
                    if (e == null) {
                        Intent intent = new Intent(getContext(), MainActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    } else {
                        String errorMsg = e.getMessage();
                        Toast.makeText(getContext(), errorMsg.substring(0, 1).toUpperCase() + errorMsg.substring(1), Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else {
            Toast.makeText(getContext(), "Incorrect Fields", Toast.LENGTH_LONG).show();
        }
    }

}
