package com.example.yazlab3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class Arayuz extends AppCompatActivity {

    String isim,tur,firmaName;
    private String[] kategori={"SECİNİZ","GIDA","GİYİM","ELEKTRONİK","SPOR","KOZMETİK","MÜZİK"};
    private Spinner spinnerKategori;
    private ArrayAdapter<String> dataAdapterForKategori;
    EditText firmaIsim;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arayuz);
        Bundle extras = getIntent().getExtras();
        isim=extras.getString("isimler");
        spinnerKategori = (Spinner) findViewById(R.id.spinner1);
        firmaIsim = (EditText) findViewById(R.id.editText10);
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

    public void konumkullan(View view){
        firmaName=firmaIsim.getText().toString().trim();
        Intent intent=new Intent(getApplicationContext(),Harita.class);
        intent.putExtra("isimler",isim);
        intent.putExtra("kategori",tur);
        intent.putExtra("firmaisim",firmaName);
        startActivity(intent);
    }

}
