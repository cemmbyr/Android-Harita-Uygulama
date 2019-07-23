package com.example.yazlab3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class Kayit extends AppCompatActivity {

    EditText username,password,lokasyon;
    String kullaniciAdi,sifre,yer;
    FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kayit);
        username=(EditText)findViewById(R.id.editText3);
        password=(EditText)findViewById(R.id.editText4);
        lokasyon=(EditText)findViewById(R.id.editText5);

    }

    public void kaydet(View view){

        kullaniciAdi=username.getText().toString().trim();
        sifre=password.getText().toString().trim();
        yer=lokasyon.getText().toString().trim();

        Map<String, String> yeniUser = new HashMap<String, String>();
        yeniUser.put("KullaniciAdi", kullaniciAdi);
        yeniUser.put("Sifre", sifre);
        yeniUser.put("Lokasyon",yer);

        if (!kullaniciAdi.equalsIgnoreCase("")&& !sifre.equalsIgnoreCase("")&& !yer.equalsIgnoreCase("")){
            DatabaseReference databaseReference = firebaseDatabase.getReference("users/");
            databaseReference.child(kullaniciAdi).setValue(yeniUser);
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
        }
        else{
            Toast.makeText(this,"Hicbir alan boş birakilamaz",Toast.LENGTH_SHORT).show();
        }
    }
}
