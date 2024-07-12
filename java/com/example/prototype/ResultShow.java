package com.example.prototype;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.prototype.R;

public class ResultShow extends AppCompatActivity {

    private Button btnCall, btnMap,btnWebsite;
    private TextView txtResult;
    String result, resultMap, resultCall, resultWeb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.prototype.R.layout.activity_result_show);

        btnCall = (Button) findViewById(R.id.resultCallButton);
        btnMap = (Button) findViewById(R.id.resultMapButton);
        btnWebsite = (Button) findViewById(R.id.resultWebsiteButton);

        txtResult = (TextView) findViewById(R.id.resultTextAll);

        Bundle bundle = getIntent().getExtras();
        result = bundle.getString("tag");
        resultMap = bundle.getString("tagMap");
        resultCall = bundle.getString("tagCall");
        resultWeb = bundle.getString("tagWeb");
        txtResult.setText(result);


        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String d = "tel:" + resultCall ;

                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse(d));
                if (ActivityCompat.checkSelfPermission(ResultShow.this,android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(ResultShow.this,
                            android.Manifest.permission.CALL_PHONE)) {
                    } else {

                    }
                }
                startActivity(callIntent);

            }
        });


        btnWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(ResultShow.this, ResultWebsite.class);
                intent.putExtra("webURL",resultWeb);

                startActivity(intent);


            }
        });


        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(ResultShow.this, ResultMap.class);
                intent.putExtra("mapLL",resultMap);

                startActivity(intent);


            }
        });


    }
}
