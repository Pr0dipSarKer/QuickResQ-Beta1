package com.example.prototype;


import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.annotation.NonNull;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prototype.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NearestHospital extends AppCompatActivity {
   //private static final String TAG = "MainActivity";

    //private static final String KEY_TITLE = "title";
    //private static final String KEY_DESCRIPTION = "description";
    private Button button;

    String [] locationStr;
    //private EditText editTextTitle;





    private TextView textViewData2;
    private Spinner spinner;

    ListView list;
    //public String search="";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference notebookRef = db.collection("NearestHospital");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.prototype.R.layout.activity_nearest_hospital);

        locationStr = getResources().getStringArray(R.array.location);

        spinner = (Spinner) findViewById(R.id.spinnerLocation);

        //editTextTitle = (EditText) findViewById(R.id.edit_text_title);
        button = (Button) findViewById(R.id.load);


        textViewData2 = (TextView) findViewById(R.id.text_view_data2);

        ArrayAdapter<String>  adapterSpinner = new ArrayAdapter<String>(this, R.layout.spinner, R.id.spinnerShow, locationStr);
        spinner.setAdapter(adapterSpinner);

        list= (ListView) findViewById(R.id.lv);



        button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                String search2 = spinner.getSelectedItem().toString();
                //String search = editTextTitle.getText().toString();
                textViewData2.setText("Search results for-"+search2);

                final ArrayList<String> al = new ArrayList<String>();
                final ArrayList<String> al3 = new ArrayList<String>();
                final ArrayList<String> al5 = new ArrayList<String>();
                final ArrayList<String> al7 = new ArrayList<String>();
                final ArrayAdapter<String> adapter = new ArrayAdapter<String>(NearestHospital.this, R.layout.sample_view, R.id.text11,al);

                notebookRef.whereEqualTo("tag", search2)
                        //.orderBy("name", Query.Direction.ASCENDING)
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {



                                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                    Note note = documentSnapshot.toObject(Note.class);
                                    //note.setDocumentId(documentSnapshot.getId());
                                    String data = "";
                                    //String documentId = note.getDocumentId();
                                    String name = note.getName();
                                    String address = note.getAddress();
                                    String phone = note.getPhone();
                                    String website = note.getWebsite();
                                    String map = note.getMap();


                                    //String tag = note.getTag();

                                    data += "Name: " + name + "\nAddress: " + address
                                            + "\nPhone Number: " + phone +"\nWebsite: " + website;
                                    al.add(data);
                                    al3.add(map);
                                    al5.add(phone);
                                    al7.add(website);

                                }


                                list.setAdapter(adapter);


                                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                                        String item = al.get(position);
                                        String itemMap = al3.get(position);
                                        String itemPhone = al5.get(position);
                                        String itemWeb = al7.get(position);


                                        Intent intent = new Intent(NearestHospital.this, ResultShow.class);
                                        intent.putExtra("tag",item);
                                        intent.putExtra("tagMap",itemMap);
                                        intent.putExtra("tagCall",itemPhone);
                                        intent.putExtra("tagWeb",itemWeb);
                                        startActivity(intent);


                                        //Toast.makeText(getBaseContext(), item, Toast.LENGTH_LONG).show();

                                    }
                                });


                            }
                        });


            }
        });


    }



    //On start

    @Override
    protected void onStart() {
        super.onStart();


        final ArrayList<String> al2 = new ArrayList<String>();
        final ArrayList<String> al4 = new ArrayList<String>();
        final ArrayList<String> al6 = new ArrayList<String>();
        final ArrayList<String> al8 = new ArrayList<String>();
        final ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(NearestHospital.this, R.layout.sample_view, R.id.text11,al2);
        notebookRef.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {
                if (e != null) {
                    return;
                }



                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    Note note = documentSnapshot.toObject(Note.class);
                    //note.setDocumentId(documentSnapshot.getId());
                    String data = "";
                    //String documentId = note.getDocumentId();
                    String name = note.getName();
                    String address = note.getAddress();
                    String phone = note.getPhone();
                    String website = note.getWebsite();
                    String map = note.getMap();



                    data += "Name: " + name + "\nAddress: " + address
                            + "\nPhone Number: " + phone +"\nWebsite: " + website;

                    al2.add(data);
                    al4.add(map);
                    al6.add(phone);
                    al8.add(website);

                }

                list.setAdapter(adapter2);
                //textViewData2.setText(al2.get(3));

                //
                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        String item = al2.get(position);
                        String itemMap = al4.get(position);
                        String itemPhone = al6.get(position);
                        String itemWeb = al8.get(position);


                        Intent intent = new Intent(NearestHospital.this, ResultShow.class);
                        intent.putExtra("tag",item);
                        intent.putExtra("tagMap",itemMap);
                        intent.putExtra("tagCall",itemPhone);
                        intent.putExtra("tagWeb",itemWeb);
                        startActivity(intent);

                        //Toast.makeText(getBaseContext(), item, Toast.LENGTH_SHORT).show();

                    }
                });


                //


            }
        });



    }


}