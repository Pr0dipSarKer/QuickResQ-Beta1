package com.example.prototype;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prototype.R;

import java.util.List;
import java.util.Locale;

public class ResultMap extends AppCompatActivity implements LocationListener {


    TextView locationText;

    LocationManager locationManager;

    String latlong;

    Button mapRedirect;
    WebView mapWebSite;
    String s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(com.example.prototype.R.layout.activity_result_map);

        locationText = (TextView) findViewById(R.id.locationTextt);

        Bundle bundle = getIntent().getExtras();
        latlong = bundle.getString("mapLL");


        mapRedirect = (Button) findViewById(R.id.buttonMapRedirect);
        mapWebSite = (WebView) findViewById(R.id.mapWebSiteView);






        mapWebSite.getSettings().setJavaScriptEnabled(true);
        mapWebSite.setWebViewClient(new WebViewClient());

        mapWebSite.loadUrl(s);

        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(ResultMap.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);

        }

        getLocation();


        mapRedirect.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {


                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(s));
                startActivity(intent);
            }
        });


    }

    void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, this);
        }
        catch(SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        locationText.setText("Latitude: " + location.getLatitude() + "\n Longitude: " + location.getLongitude());

        s="http://maps.google.com/maps?saddr="+location.getLatitude()+","+location.getLongitude()+"&daddr="+latlong;
        locationText.setText(s);
        mapWebSite.loadUrl(s);
    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(ResultMap.this, "Please Enable GPS and Internet", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }
}
