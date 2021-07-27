package com.codepath.tripplannerapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class NewTripActivity extends AppCompatActivity {

    public static final String TAG = "NewTripActivity";
    private static final int RESULT_LOAD_IMAGE = 1;
    private EditText etTripName;
    private ImageView ivTripImage;
    private Button btnUploadTripImage;
    private Button btnNewTripSubmit;
    private File photoFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_trip);

        etTripName = findViewById(R.id.etTripName);
        ivTripImage = findViewById(R.id.ivTripImage);
        btnUploadTripImage = findViewById(R.id.btnUploadTripImage);
        btnNewTripSubmit = findViewById(R.id.btnNewTripSubmit);

        btnNewTripSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "submit button was clicked");
                String tripName = etTripName.getText().toString();
                if (tripName.isEmpty()) {
                    Toast.makeText(NewTripActivity.this, "You must add a trip name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (photoFile == null || ivTripImage.getDrawable() == null) {
                    Toast.makeText(NewTripActivity.this, "There is no image!", Toast.LENGTH_SHORT).show();
                    return;
                }
                ParseUser currentUser = ParseUser.getCurrentUser();
                saveTrip(tripName, currentUser, photoFile);
            }
        });

        btnUploadTripImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            photoFile = new File(selectedImage.getPath());
            ivTripImage.setImageURI(selectedImage);
        }
    }

    private void saveTrip(String tripName, ParseUser currentUser, File photoFile) {
        Log.i(TAG, "we are in save trip");
        Trip trip = new Trip();
        trip.setTripName(tripName);
        Drawable drawable = ivTripImage.getDrawable();
        Bitmap myLogo = ((BitmapDrawable) drawable).getBitmap();
        ByteArrayOutputStream myLogoStream = new ByteArrayOutputStream();
        myLogo.compress(Bitmap.CompressFormat.PNG, 100, myLogoStream);
        byte[] myLogoByteArray = myLogoStream.toByteArray();
        myLogo.recycle();
        trip.setTripImage(new ParseFile(myLogoByteArray));
        trip.setUser(currentUser);
        trip.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error while saving", e);
                    Toast.makeText(NewTripActivity.this, "Error while saving", Toast.LENGTH_SHORT).show();
                }
                Log.i(TAG, "Post save was successful");
                etTripName.setText("");
                ivTripImage.setImageResource(0);
            }
        });
    }
}