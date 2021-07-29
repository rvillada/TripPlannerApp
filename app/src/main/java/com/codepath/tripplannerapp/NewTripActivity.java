package com.codepath.tripplannerapp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Calendar;

public class NewTripActivity extends AppCompatActivity {

    public static final String TAG = "NewTripActivity";
    private static final int RESULT_LOAD_IMAGE = 1;
    private EditText etTripName;
    private ImageView ivTripImage;
    private Button btnUploadTripImage;
    private Button btnNewTripSubmit;
    private ImageButton btnHome;
    private File photoFile;

    private DatePickerDialog datePickerDialogStart;
    private DatePickerDialog datePickerDialogEnd;

    private Button btnStartDate;
    private Button btnEndDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_trip);
        initDatePicker();

        etTripName = findViewById(R.id.etTripName);
        ivTripImage = findViewById(R.id.ivTripImage);
        btnUploadTripImage = findViewById(R.id.btnUploadTripImage);
        btnNewTripSubmit = findViewById(R.id.btnNewTripSubmit);
        btnHome = findViewById(R.id.btnHome);
        btnStartDate = findViewById(R.id.btnStartDate);
        btnEndDate = findViewById(R.id.btnEndDate);

        btnStartDate.setText(getTodaysDate());
        btnEndDate.setText(getTodaysDate());


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
                    Toast.makeText(NewTripActivity.this, "You must add a trip image", Toast.LENGTH_SHORT).show();
                    return;
                }
                ParseUser currentUser = ParseUser.getCurrentUser();
                saveTrip(tripName, currentUser, photoFile, btnStartDate.getText().toString(), btnEndDate.getText().toString());
            }
        });

        btnUploadTripImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
            }
        });

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(NewTripActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });

    }

    private String getTodaysDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(dayOfMonth, month, year);

    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListenerStart = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = makeDateString(dayOfMonth, month, year);
                btnStartDate.setText(date);
            }
        };

        DatePickerDialog.OnDateSetListener dateSetListenerEnd = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = makeDateString(dayOfMonth, month, year);
                btnEndDate.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialogStart = new DatePickerDialog(this, style, dateSetListenerStart, year, month, dayOfMonth);
        datePickerDialogEnd = new DatePickerDialog(this, style, dateSetListenerEnd, year, month, dayOfMonth);


    }

    private String makeDateString(int dayOfMonth, int month, int year) {
        return getMonthFormat(month) + " " + dayOfMonth + " " + year;
    }

    private String getMonthFormat(int month) {
        if (month == 1) {
            return "JAN";
        }
        if (month == 2) {
            return "FEB";
        }
        if (month == 3) {
            return "MAR";
        }
        if (month == 4) {
            return "APR";
        }
        if (month == 5) {
            return "MAY";
        }
        if (month == 6) {
            return "JUN";
        }
        if (month == 7) {
            return "JUL";
        }
        if (month == 8) {
            return "AUG";
        }
        if (month == 9) {
            return "SEP";
        }
        if (month == 10) {
            return "OCT";
        }
        if (month == 11) {
            return "NOV";
        }
        if (month == 12) {
            return "DEC";
        }
        return "JAN";
    }

    public void openDatePickerStart(View view) {
        datePickerDialogStart.show();
    }

    public void openDatePickerEnd(View view) {
        datePickerDialogEnd.show();
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

    private void saveTrip(String tripName, ParseUser currentUser, File photoFile, String tripStart, String tripEnd) {
        Trip trip = new Trip();
        trip.setTripName(tripName);
        trip.setTripStart(tripStart);
        trip.setTripEnd(tripEnd);


        // compresses image into manageable size
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
                Intent i = new Intent(NewTripActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}