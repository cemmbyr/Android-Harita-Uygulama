package com.example.yazlab3;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Kampanyalar extends AppCompatActivity {

    ArrayList<String> yeni = new ArrayList<>();
    public static ArrayList<String> icerik = new ArrayList<>();
    public static ArrayList<String> sure = new ArrayList<>();
    public static ArrayList<String> birlestir = new ArrayList<>();
    ListView listView;
    ArrayList<String> gelen = new ArrayList<>();
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kampanyalar);
        listView=(ListView)findViewById(R.id.listview1);
        Intent intent = getIntent();
        gelen=intent.getStringArrayListExtra("firma");
        for(int i=0;i<gelen.size();i++) {
            DatabaseReference dbRef = firebaseDatabase.getReference();
            dbRef.child("Firma/" + gelen.get(i) + "/reklam/").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    int size = (int) dataSnapshot.getChildrenCount();
                    for (DataSnapshot dsp :dataSnapshot.getChildren()){
                        yeni.add(dsp.getKey().toString());
                    }
                    for (int k =0;k<size;k++) {
                        icerik.add(dataSnapshot.child(yeni.get(k)).child("KampanyaIcerik").getValue().toString());
                        sure.add(dataSnapshot.child(yeni.get(k)).child("KampanyaSuresi").getValue().toString());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        for (int b=0;b<icerik.size();b++){
            birlestir.add("Kampanya Bilgisi: " + icerik.get(b) + " Kampanya Süresi: " + sure.get(b));
        }
        ArrayAdapter<String> genellAdapter=new ArrayAdapter<String>(Kampanyalar.this,android.R.layout.simple_list_item_2,android.R.id.text1,birlestir);
        listView.setAdapter(genellAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });

    }
}
