package com.example.yazlab3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class FirmaKayit extends AppCompatActivity {

    private String[] kategori={"GIDA","GİYİM","ELEKTRONİK","SPOR","KOZMETİK","MÜZİK"};
    private Spinner spinnerKategori;
    private ArrayAdapter<String> dataAdapterForKategori;
    EditText firmaID,firmaAdi,firmaLokasyon;
    String id,adi,yer,tur;
    FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firma_kayit);
        spinnerKategori = (Spinner) findViewById(R.id.spinner1);
        firmaID = findViewById(R.id.editText15);
        firmaAdi=findViewById(R.id.editText14);
        firmaLokasyon=findViewById(R.id.editText16);
        dataAdapterForKategori = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, kategori);
        dataAdapterForKategori.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerKategori.setAdapter(dataAdapterForKategori);
        spinnerKategori.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                tur=spinnerKategori.getSelectedItem().toString();

            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    public void kayit(View view){
        id=firmaID.getText().toString().trim();
        adi=firmaAdi.getText().toString().trim();
        yer=firmaLokasyon.getText().toString().trim();

        Map<String, String> yeniReklam = new HashMap<String, String>();
        yeniReklam.put("FirmaId", id);
        yeniReklam.put("FirmaAdi", adi);
        yeniReklam.put("Lokasyon",yer);
        yeniReklam.put("Kategori",tur);

        if (!id.equalsIgnoreCase("")&&!adi.equalsIgnoreCase("")&&!yer.equalsIgnoreCase("")){
            DatabaseReference databaseReference = firebaseDatabase.getReference("Firma");
            databaseReference.child(adi).setValue(yeniReklam);
            Intent intent=new Intent(getApplicationContext(),FirmaHarita.class);
            intent.putExtra("isimler",adi);
            startActivity(intent);
        }else{
            Toast.makeText(this,"Hicbir alan boş birakilamaz",Toast.LENGTH_SHORT).show();
        }

    }
}