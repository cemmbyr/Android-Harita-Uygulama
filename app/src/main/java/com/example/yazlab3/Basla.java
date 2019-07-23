package com.example.yazlab3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Basla extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basla);
    }
    public void kullaniciGirisi(View view){
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
    }
    public void firmaGiris(View view){
        startActivity(new Intent(getApplicationContext(),FirmaGirisi.class));

    }
    public void firmakayit(View view){
        startActivity(new Intent(getApplicationContext(),FirmaKayit.class));
    }
}
