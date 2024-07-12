package com.example.prototype;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class FormActivity extends AppCompatActivity {

    private EditText nameEditText;
    private EditText emailEditText;
    private EditText phoneEditText;
    private EditText ownPhoneEditText;
    private Button getLocationButton;
    private Button submitButton;
    private TextView locationTextView;
    private DatabaseReference databaseReference;
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.emergency_form);

        databaseReference = FirebaseDatabase.getInstance().getReference("Needs_help_Now");

        nameEditText = findViewById(R.id.name_edit_text);
        emailEditText = findViewById(R.id.email_edit_text);
        phoneEditText = findViewById(R.id.phone_edit_text);
        ownPhoneEditText = findViewById(R.id.own_phone_edit_text);
        getLocationButton = findViewById(R.id.get_location_button);
        submitButton = findViewById(R.id.submit_button);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent13 = new Intent(FormActivity.this, form_data.class);
                startActivity(intent13);
            }
        });
        locationTextView = findViewById(R.id.location_text_view);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        getLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(FormActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(FormActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(FormActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                } else {
                    getLocation();
                }
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String phone = phoneEditText.getText().toString();
                String ownPhone = ownPhoneEditText.getText().toString();
                String location = locationTextView.getText().toString();

                form_data needs_help_now = new form_data(name, email, phone,ownPhone, location);
                databaseReference.push().setValue(needs_help_now);

                Toast.makeText(FormActivity.this, "Form submitted successfully!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (location != null) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();

            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            try {
                List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                if (addresses.size() > 0) {
                    Address address = addresses.get(0);
                    String locality = address.getLocality(); // Get the locality (e.g., city, town)
                    String subLocality = address.getSubLocality(); // Get the sub-locality (e.g., neighborhood)
                    String addressLine = address.getAddressLine(0); // Get the full address

                    locationTextView.setText("District: " + locality + ", Local area: " + subLocality + ", Address: " + addressLine);
                } else {
                    locationTextView.setText("Unable to get location");
                }
            } catch (IOException e) {
                locationTextView.setText("Error: " + e.getMessage());
            }
        } else {
            locationTextView.setText("Unable to get location");
        }
    }
}