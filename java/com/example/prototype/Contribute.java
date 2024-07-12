package com.example.prototype;


import androidx.appcompat.app.AppCompatActivity;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prototype.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Contribute extends AppCompatActivity {

    //defining view objects
    private EditText name;
    private EditText address;
    private EditText phone;
    private EditText website;
    private Button button;

    private Spinner spinnerService;
    private Spinner spinnerArea;


    public String[] mobileArray;
    //private ProgressDialog progressDialog;


    //defining firebaseauth object
    private FirebaseFirestore mStore;

    String [] locationAreaStr;
    String [] contributionServicetypeStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contribute);

        locationAreaStr = getResources().getStringArray(R.array.location);
        contributionServicetypeStr = getResources().getStringArray(R.array.serviceType);
        //initializing firebase auth object
        mStore = FirebaseFirestore.getInstance();

        //initializing views
        name = (EditText) findViewById(R.id.textName);
        address = (EditText) findViewById(R.id.textAddress);
        phone = (EditText) findViewById(R.id.textPhone);
        website = (EditText) findViewById(R.id.textWebsite);

        button = (Button) findViewById(R.id.buttonSubmit);

        spinnerService = (Spinner) findViewById(R.id.spinnerContributeServiceType);
        spinnerArea = (Spinner) findViewById(R.id.spinnerContributeArea);

        ArrayAdapter<String>  adapterSpinner = new ArrayAdapter<String>(Contribute.this, R.layout.spinner, R.id.spinnerShow, locationAreaStr);
        spinnerArea.setAdapter(adapterSpinner);

        ArrayAdapter<String>  adapterSpinner2 = new ArrayAdapter<String>(Contribute.this, R.layout.spinner, R.id.spinnerShow, contributionServicetypeStr);
        spinnerService.setAdapter(adapterSpinner2);


        //progressDialog = new ProgressDialog(this);

        //attaching listener to button
        button.setOnClickListener(new View.OnClickListener(){



            public  void  onClick (View v){


                String area = spinnerArea.getSelectedItem().toString();
                String service = spinnerService.getSelectedItem().toString();
                String n = name.getText().toString();
                String a = address.getText().toString();
                String p = phone.getText().toString();
                String w = website.getText().toString();

                if(n.equalsIgnoreCase(""))
                {
                    name.setHint("Please enter Name");//it gives user to hint
                    name.setError("Name Required");//it gives user to info message //use any one //
                }else if (a.equalsIgnoreCase("")){
                    address.setHint("Please enter Address");//it gives user to hint
                    address.setError("Address Required");//it gives user to info message //use any one //
                }else if (p.equalsIgnoreCase("")){
                    phone.setHint("Please enter Phone Number");//it gives user to hint
                    phone.setError("Phone Number Required");//it gives user to info message //use any one //
                }
                else
                {
                    Map<String, String> mData = new HashMap<>();
                    mData.put("serviceType", service);
                    mData.put("name", n);
                    mData.put("address", a);
                    mData.put("phone", p);
                    mData.put("website", w);
                    mData.put("tag", area);

                    mStore.collection("Temporary").add(mData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Toast.makeText(Contribute.this, "Thank you for your Contribution.", Toast.LENGTH_LONG).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Contribute.this, "Error Occured, Please try again", Toast.LENGTH_LONG).show();
                        }
                    });

                }




            }
        });





    }


}