package com.example.yazlab3;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Reklam extends AppCompatActivity {
    EditText kampanyaIcerik, kampanyaSuresi;
    String icerik, sure;
    String isim;
    public int i = 0;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference say = firebaseDatabase.getReference();
    Map<String, String> cocuk = new HashMap<String, String>();
    ArrayList<Firma> firmalar = new ArrayList<Firma>();
    ArrayList<String> yeni = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reklam);
        kampanyaIcerik = findViewById(R.id.editText11);
        kampanyaSuresi = findViewById(R.id.editText12);
        Bundle extras = getIntent().getExtras();
        isim = extras.getString("isimler");

        DatabaseReference dbRef = firebaseDatabase.getReference();
        dbRef.child("Firma/" + isim + "/reklam/").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int size = (int) dataSnapshot.getChildrenCount();
                System.out.println("BOYUT : "+size);
                i=size;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void ekle(View view) {

            say.child("Firma/").getKey().toString();
            icerik = kampanyaIcerik.getText().toString().trim();
            sure = kampanyaSuresi.getText().toString().trim();
            Map<String, String> yeniReklam = new HashMap<String, String>();
            yeniReklam.put("KampanyaIcerik", icerik);
            yeniReklam.put("KampanyaSuresi", sure);
            if (!icerik.equalsIgnoreCase("") && !sure.equalsIgnoreCase("")) {
                DatabaseReference databaseReference = firebaseDatabase.getReference("Firma/" + isim + "/reklam/" + i);
                databaseReference.setValue(yeniReklam);
            } else {
                Toast.makeText(this, "Hicbir alan boş birakilamaz", Toast.LENGTH_SHORT).show();
            }
            i++;
        }

    private void collectPhoneNumbers(Map<String,Object> users) {

        ArrayList<Long> phoneNumbers = new ArrayList<>();

        //iterate through each user, ignoring their UID
        for (Map.Entry<String, Object> entry : users.entrySet()){

            //Get user map
            Map singleUser = (Map) entry.getValue();
            //Get phone field and append to list
            phoneNumbers.add((Long) singleUser.get("phone"));
        }
        System.out.println("sssssssssssss"+users.get(1));
        System.out.println("aaaaaaaaaa"+phoneNumbers.get(0).toString());
    }
    }
