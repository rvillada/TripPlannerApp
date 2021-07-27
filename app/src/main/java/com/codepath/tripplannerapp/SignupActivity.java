package com.codepath.tripplannerapp;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignupActivity extends AppCompatActivity {

    public static final String TAG = "SignupActivity";
    private EditText etUsernameSignup;
    private EditText etPasswordSignup;
    private Button btnSignupOfficial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        etUsernameSignup = findViewById(R.id.etUsernameSignup);
        etPasswordSignup = findViewById(R.id.etPasswordSignup);
        btnSignupOfficial = findViewById(R.id.btnSignupOfficial);

        btnSignupOfficial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick signup button");

                String username = etUsernameSignup.getText().toString();
                String password = etPasswordSignup.getText().toString();

                // Create the ParseUser
                ParseUser user = new ParseUser();

                user.setUsername(username);
                user.setPassword(password);
                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e != null) {
                            Toast.makeText(SignupActivity.this, "Issue with login!", Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            goMainActivity();
                            Toast.makeText(SignupActivity.this, "Successfully signed up!", Toast.LENGTH_SHORT).show();
                            Log.i(TAG, "success with signup");
                        }
                    }
                });
            }
        });
    }

    private void goMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }
}